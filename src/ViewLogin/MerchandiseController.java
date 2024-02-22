package ViewLogin;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class MerchandiseController implements Initializable {
    ObservableList<Merchandise> data = FXCollections.observableArrayList();
    @FXML
    TableView<Merchandise> table;
    @FXML
    private TableColumn<?, ?> manufacturerCol;
    @FXML
    private TableColumn<?, ?> productNameCol;
    @FXML
    private Label merchandiseIdLabel;
    @FXML
    private Label productNameLabel;
    @FXML
    private Label manufacturerLabel;
    @FXML
    private Label modelLabel;
    @FXML
    private Label yearLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private Button newButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button addButton;
    @FXML
    private Button viewButton;
    @FXML
    private Label soldByLabel;
    @FXML
    private Button mapButton;
    @FXML
    private Label sellByLabel;
    private String username;

    public void initialize(URL location, ResourceBundle resources) {
        this.manufacturerCol.setCellValueFactory(new PropertyValueFactory<>("manufacturer"));
        this.productNameCol.setCellValueFactory(new PropertyValueFactory<>("productName"));
        this.table.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                Merchandise selectedMerchandise = MerchandiseController.this.table.getSelectionModel().getSelectedItem();
                MerchandiseController.this.showMerchandiseDetails(selectedMerchandise);
            }
        });
    }

    public void showMerchandiseDetails(Merchandise merchandise) {
        this.merchandiseIdLabel.setText("" + merchandise.getMerchandiseId());
        this.productNameLabel.setText(merchandise.getProductName());
        this.manufacturerLabel.setText(merchandise.getManufacturer());
        this.modelLabel.setText("" + merchandise.getModel());
        this.yearLabel.setText("" + merchandise.getYear());
        this.priceLabel.setText("$" + merchandise.getPrice());
        this.sellByLabel.setText(merchandise.getSellBy());
    }

    public void addToCartButton() {
        if (this.table.getSelectionModel().getSelectedItem() != null) {
            Merchandise selectedMerchandise = this.table.getSelectionModel().getSelectedItem();
            Alert alert;
            if (this.saveToCart(selectedMerchandise)) {
                alert = new Alert(AlertType.NONE, selectedMerchandise.getProductName() + " Added Successfully!", new ButtonType[]{ButtonType.OK});
                alert.showAndWait();
            } else {
                alert = new Alert(AlertType.NONE, "Merchandise Adding Failed!", new ButtonType[]{ButtonType.OK});
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(AlertType.NONE, "Please Select Merchandise To Add!", new ButtonType[]{ButtonType.OK});
            alert.showAndWait();
        }
    }

    private boolean saveToCart(Merchandise cart) {
        Connection connection = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/merchandise","root","lifeisboring..123");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            String var10000 = this.username;
            String addToCart = "insert into cartTable values('" + var10000 + "', '" + cart.getMerchandiseId() + "', '" + cart.getProductName() + "', '" + cart.getManufacturer() + "', '" + cart.getModel() + "', '" + cart.getYear() + "', '" + cart.getPrice() + "', '" + cart.getSellBy() + "')";
            statement.executeUpdate(addToCart);
            return true;
        } catch (SQLException var15) {
            System.err.println(var15.getMessage());
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

    public void loadDatabase() {
        Connection connection = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/merchandise","root","lifeisboring..123");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            String query = "select * from merchandiseTable";
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                Merchandise existedMerchandise = new Merchandise(
                        rs.getInt("merchandiseId"),
                        rs.getString("productName"),
                        rs.getString("manufacturer"),
                        rs.getInt("model"),
                        rs.getInt("year"),
                        rs.getFloat("price"),
                        rs.getString("sellBy")
                );
                this.data.add(existedMerchandise);
                this.table.setItems(this.data);
            }

            statement.close();
            rs.close();
        } catch (SQLException var14) {
            System.err.println(var14.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException var13) {
                System.err.println(var13);
            }

        }

    }

    public MerchandiseController() {
    }

    public void mapButtonAction(ActionEvent event) throws IOException {
        FXMLLoader mapPage = new FXMLLoader(this.getClass().getResource("Map.fxml"));
        Parent mapParent = (Parent) mapPage.load();
        Scene mapScene = new Scene(mapParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        MapController newUserName = (MapController) mapPage.getController();
        newUserName.sendData(this.username);
        window.setScene(mapScene);
        window.show();
    }

    public void logoutButtonAction(ActionEvent event) throws IOException {
        FXMLLoader logoutPage = new FXMLLoader(this.getClass().getResource("Login.fxml"));
        Parent logoutParent = (Parent) logoutPage.load();
        Scene logoutScene = new Scene(logoutParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(logoutScene);
        window.show();
    }

    public void ShoppingCartButton(ActionEvent event) throws IOException {
        FXMLLoader shoppingCartPage = new FXMLLoader(this.getClass().getResource("ShoppingCart.fxml"));
        Parent shoppingCartParent = (Parent) shoppingCartPage.load();
        Scene shoppingCartScene = new Scene(shoppingCartParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        ShoppingCartController newUserName = (ShoppingCartController) shoppingCartPage.getController();
        newUserName.sendData(this.username);
        window.setScene(shoppingCartScene);
        window.show();
    }

    public void sellButton(ActionEvent event) throws IOException {
        FXMLLoader sellingPage = new FXMLLoader(this.getClass().getResource("AddingMerch.fxml"));
        Parent sellingParent = (Parent) sellingPage.load();
        Scene sellingScene = new Scene(sellingParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        AddingController newUserName = (AddingController) sellingPage.getController();
        newUserName.sendData(this.username);
        window.setScene(sellingScene);
        window.show();
    }

    public void editButton(ActionEvent event) throws IOException {
        FXMLLoader editPage = new FXMLLoader(this.getClass().getResource("AccountEdit.fxml"));
        Parent editParent = (Parent) editPage.load();
        Scene editScene = new Scene(editParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        AccountDetails newUserName = (AccountDetails) editPage.getController();
        newUserName.sendData(this.username);
        window.setScene(editScene);
        window.show();
    }

    @FXML
    public void deleteMerchandiseFromTable(ActionEvent event) throws IOException {
        this.table.getItems().removeAll(new Merchandise[]{this.table.getSelectionModel().getSelectedItem()});
    }

    public void sendData(String text) {
        this.username = text;
        System.out.println(this.username);
        if (!Objects.equals(this.username, "admin")) {
            this.deleteButton.setVisible(false);
        } else {
            this.addButton.setVisible(false);
            this.viewButton.setVisible(false);
        }

        this.loadDatabase();
    }
}
