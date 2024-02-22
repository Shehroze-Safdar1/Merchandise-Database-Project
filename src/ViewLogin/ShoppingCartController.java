package ViewLogin;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ShoppingCartController implements Initializable {
    ObservableList<Merchandise> data = FXCollections.observableArrayList();

    @FXML
    private TableView<Merchandise> table;
    @FXML
    private TableColumn<Merchandise, String> productNameCol;
    @FXML
    private TableColumn<Merchandise, Float> priceCol;
    @FXML
    private Label productNameLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private Label subtotalLabel;
    @FXML
    private Label shippingLabel;
    @FXML
    private Label totalLabel;
    @FXML
    private Label taxLabel;

    private String username;
    private float totalPrice = 0.0F;
    private final float tax = 0.07F;

    public void sendData(String username) {
        this.username = username;
        System.out.println("VERIFY " + this.username);
        this.loadDatabase();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("productName"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        table.setOnMousePressed((MouseEvent event) -> {
            // Handle mouse click events if needed
        });
    }

    public void showMerchandiseDetails(Merchandise merchandise) {
        productNameLabel.setText(merchandise.getProductName());
        priceLabel.setText("$" + merchandise.getPrice());
    }

    public void loadDatabase() {
        Connection connection = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/merchandise", "root", "lifeisboring..123");

            String selectCartQuery = "SELECT * FROM carttable WHERE username = ?";
            try (PreparedStatement selectCartStatement = connection.prepareStatement(selectCartQuery)) {
                selectCartStatement.setString(1, this.username);
                ResultSet rs = selectCartStatement.executeQuery();

                while (rs.next()) {
                    Merchandise existedMerchandise = new Merchandise(
                            rs.getInt("merchandiseId"),
                            rs.getString("productName"),
                            rs.getString("manufacturer"),
                            rs.getInt("model"),
                            rs.getInt("year"),
                            rs.getFloat("price"),
                            rs.getString("sellBy"));

                    this.totalPrice += existedMerchandise.getPrice();
                    this.data.add(existedMerchandise);
                    this.table.setItems(this.data);
                }

                DecimalFormat decimalFormat = new DecimalFormat("#.00");
                String taxAmount = decimalFormat.format(this.totalPrice * this.tax);
                String totalAmount = decimalFormat.format(this.totalPrice + (this.totalPrice * this.tax));
                String total = decimalFormat.format(this.totalPrice);

                taxLabel.setText("$" + taxAmount);
                totalLabel.setText("$" + totalAmount);
                subtotalLabel.setText("$" + total);

                rs.close();
            }
        } catch (SQLException var17) {
            System.err.println(var17.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException var16) {
                System.err.println(var16);
            }
        }
    }

    public void removeButton(ActionEvent event) throws IOException {
        Merchandise selectedMerchandise = table.getSelectionModel().getSelectedItem();
        Alert alert;
        if (selectedMerchandise != null) {
            if (removeSelected(selectedMerchandise)) {
                alert = new Alert(Alert.AlertType.NONE, "Removed Successfully!", new ButtonType[]{ButtonType.OK});
                alert.showAndWait();
                FXMLLoader shoppingCartPage = new FXMLLoader(getClass().getResource("ShoppingCart.fxml"));
                Parent shoppingCartParent = shoppingCartPage.load();
                Scene shoppingCartScene = new Scene(shoppingCartParent);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                ShoppingCartController newUserName = shoppingCartPage.getController();
                newUserName.sendData(this.username);
                window.setScene(shoppingCartScene);
                window.show();
            } else {
                alert = new Alert(Alert.AlertType.NONE, "Removal Failed!", new ButtonType[]{ButtonType.OK});
                alert.showAndWait();
            }
        } else {
            alert = new Alert(Alert.AlertType.NONE, "Please select a merchandise for removal!", new ButtonType[]{ButtonType.OK});
            alert.showAndWait();
        }
    }

    public void backButton(ActionEvent event) throws IOException {
        FXMLLoader bookstorePage = new FXMLLoader(getClass().getResource("MerchandiseStore.fxml"));
        Parent bookstoreParent = bookstorePage.load();
        Scene bookstoreScene = new Scene(bookstoreParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        MerchandiseController newUserName = bookstorePage.getController();
        newUserName.sendData(this.username);
        window.setScene(bookstoreScene);
        window.show();
    }

    private boolean removeSelected(Merchandise cart) {
        Connection connection = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/merchandise", "root", "lifeisboring..123");

            String removeCartQuery = "DELETE FROM carttable WHERE username = ? AND merchandiseId = ?";
            try (PreparedStatement removeCartStatement = connection.prepareStatement(removeCartQuery)) {
                removeCartStatement.setString(1, this.username);
                removeCartStatement.setInt(2, cart.getMerchandiseId());

                int rowsAffected = removeCartStatement.executeUpdate();

                if (rowsAffected > 0) {
                    return true;
                } else {
                    System.err.println("No rows were affected by the deletion.");
                }
            }
        } catch (SQLException var15) {
            System.err.println("Error during removal: " + var15.getMessage());
            var15.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException var14) {
                System.err.println(var14);
            }
        }

        return false;
    }

    @FXML
    public void CheckoutButton(ActionEvent event) throws IOException {
        FXMLLoader creditCardPage = new FXMLLoader(getClass().getResource("CreditCardTransaction.fxml"));
        Parent creditCardParent = creditCardPage.load();
        Scene creditCardScene = new Scene(creditCardParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        TransactionController newUserName = creditCardPage.getController();
        newUserName.sendData(this.username);
        window.setScene(creditCardScene);
        window.show();
    }
}
