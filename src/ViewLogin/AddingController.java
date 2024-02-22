package ViewLogin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class AddingController {
    @FXML
    private TextField merchandiseIdField;
    @FXML
    private TextField productNameField;
    @FXML
    private TextField manufacturerField;
    @FXML
    private TextField modelField;
    @FXML
    private TextField yearField;
    @FXML
    private TextField priceField;

    @FXML
    private TextField sellBy;

    @FXML
    private Button Sell;
    @FXML
    private Button Cancel;
    private String username;

    public AddingController() {
    }

    public void showAlert(String alertString) {
        Alert errorAlert = new Alert(AlertType.ERROR);
        errorAlert.setTitle("Form Error!");
        errorAlert.setHeaderText(alertString);
        errorAlert.show();
    }

    public void successfulAlert(String alertString) {
        Alert success = new Alert(AlertType.CONFIRMATION);
        success.setTitle("Item Posted");
        success.setHeaderText(alertString);
        success.show();
    }

    @FXML
    public void sellAction(ActionEvent event) throws IOException {
        if (merchandiseIdField.getText().isEmpty() || productNameField.getText().isEmpty() || manufacturerField.getText().isEmpty()
                || modelField.getText().isEmpty() || yearField.getText().isEmpty() || priceField.getText().isEmpty() ||sellBy.getText().isEmpty()) {
            showAlert("Please fill in all the fields");
        } else {
            Merchandise newMerchandise = new Merchandise();
            newMerchandise.setMerchandiseId(Integer.parseInt(merchandiseIdField.getText()));
            newMerchandise.setProductName(productNameField.getText());
            newMerchandise.setManufacturer(manufacturerField.getText());
            newMerchandise.setModel(Integer.parseInt(modelField.getText()));
            newMerchandise.setYear(Integer.parseInt(yearField.getText()));
            newMerchandise.setPrice(Float.parseFloat(priceField.getText()));
            newMerchandise.setSellBy(""); // or set it to a default value if needed


            boolean isSavedSuccessfully = saveMerchandiseToDB(newMerchandise);
            Alert alert;
            if (isSavedSuccessfully) {
                alert = new Alert(AlertType.NONE, "Item successfully listed for sale", new ButtonType[]{ButtonType.OK});
                alert.showAndWait();
                if (alert.getResult() == ButtonType.OK) {
                    FXMLLoader bookstorePage = new FXMLLoader(getClass().getResource("MerchandiseStore.fxml"));
                    Parent bookstoreParent = bookstorePage.load();
                    Scene bookstoreScene = new Scene(bookstoreParent);
                    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    MerchandiseController newUserName = bookstorePage.getController();
                    newUserName.sendData(username);
                    window.setScene(bookstoreScene);
                    window.show();
                }
            } else {
                alert = new Alert(AlertType.ERROR, "Item Sale Failed!", new ButtonType[]{ButtonType.YES});
                alert.show();
            }
        }
    }

    private boolean saveMerchandiseToDB(Merchandise merchandise) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/merchandise", "root", "lifeisboring..123");
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO merchandiseTable (merchandiseId, productName, manufacturer, model, year, price, sellBy) VALUES (?, ?, ?, ?, ?, ?, ?)")) {

            statement.setInt(1, merchandise.getMerchandiseId());
            statement.setString(2, merchandise.getProductName());
            statement.setString(3, merchandise.getManufacturer());
            statement.setInt(4, merchandise.getModel());
            statement.setInt(5, merchandise.getYear());
            statement.setFloat(6, merchandise.getPrice());
            statement.setString(7, merchandise.getSellBy());

            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }

    }

    @FXML
    public void Cancel(ActionEvent event) throws IOException {
        FXMLLoader bookstorePage = new FXMLLoader(getClass().getResource("MerchandiseStore.fxml"));
        Parent bookstoreParent = bookstorePage.load();
        Scene bookstoreScene = new Scene(bookstoreParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        MerchandiseController newUserName = bookstorePage.getController();
        newUserName.sendData(username);
        window.setScene(bookstoreScene);
        window.show();
    }

    public void sendData(String username) {
        this.username = username;
        System.out.println("Selling: " + username);
    }
}
