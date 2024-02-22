//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package ViewLogin;

import java.io.IOException;
import java.sql.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class TransactionController {
    private String username;
    @FXML
    private TextField creditNumField;
    @FXML
    private TextField firstnameField;
    @FXML
    private TextField lastnameField;
    @FXML
    private TextField monthField;
    @FXML
    private TextField yearField;
    @FXML
    private TextField billingAddress1Field;
    @FXML
    private TextField billingAddress2Field;
    @FXML
    private TextField cityField;
    @FXML
    private TextField stateField;
    @FXML
    private TextField zipField;
    @FXML
    private TextField phoneField;
    @FXML
    private RadioButton visaRadio;
    @FXML
    private RadioButton masterCardRadio;
    @FXML
    private RadioButton amexRadio;
    @FXML
    private RadioButton discoveryRadio;
    @FXML
    private Button payButton;
    @FXML
    private Button cancelButton;
    private String cardType;

    public TransactionController() {
    }

    public void showAlert(String alertString) {
        Alert errorAlert = new Alert(AlertType.ERROR);
        errorAlert.setTitle("Form Error!");
        errorAlert.setHeaderText(alertString);
        errorAlert.show();
    }

    public void selectVisaRadio(ActionEvent event) {
        this.masterCardRadio.setSelected(false);
        this.amexRadio.setSelected(false);
        this.discoveryRadio.setSelected(false);
        this.cardType = "Visa";
    }

    public void selectMasterRadio(ActionEvent event) {
        this.visaRadio.setSelected(false);
        this.amexRadio.setSelected(false);
        this.discoveryRadio.setSelected(false);
        this.cardType = "MasterCard";
    }

    public void selectAmericanRadio(ActionEvent event) {
        this.visaRadio.setSelected(false);
        this.masterCardRadio.setSelected(false);
        this.discoveryRadio.setSelected(false);
        this.cardType = "AMex";
    }

    public void selectDiscoveryRadio(ActionEvent event) {
        this.visaRadio.setSelected(false);
        this.masterCardRadio.setSelected(false);
        this.amexRadio.setSelected(false);
        this.cardType = "Discovery";
    }

    @FXML
    public void pay(ActionEvent event) throws IOException {
        String creditString = this.creditNumField.getText();
        if (creditString.isEmpty()) {
            this.showAlert("Please enter your credit card numbers");
        } else if (creditString.length() == 16 && creditString.matches("[0-9]+")) {
            if (!this.monthField.getText().isEmpty() && !this.yearField.getText().isEmpty()) {
                if (this.monthField.getText().length() == 2 && this.monthField.getText().matches("0[1-9]|1[012]")) {
                    if (this.yearField.getText().length() == 2 && this.yearField.getText().matches("1[8-9]|[2-9][1-9]")) {
                        if (!this.monthField.getText().isEmpty() && !this.yearField.getText().isEmpty()) {
                            if (this.firstnameField.getText().isEmpty()) {
                                this.showAlert("Please enter your first name");
                            } else if (this.lastnameField.getText().isEmpty()) {
                                this.showAlert("Please enter your last name");
                            } else if (this.billingAddress1Field.getText().isEmpty()) {
                                this.showAlert("Please enter your billing address");
                            } else if (this.cityField.getText().isEmpty()) {
                                this.showAlert("Please enter your city");
                            } else if (this.stateField.getText().isEmpty()) {
                                this.showAlert("Please enter your state");
                            } else if (this.zipField.getText().isEmpty()) {
                                this.showAlert("Please enter your zipcode");
                            } else if (this.phoneField.getText().isEmpty()) {
                                this.showAlert("Please enter your phone");
                            } else {
                                CreditCard card = new CreditCard();
                                card.setCardType(this.cardType);
                                card.setBillingAddress1Field(this.billingAddress1Field.getText());
                                card.setBillingAddress2Field(this.billingAddress2Field.getText());
                                card.setCityField(this.cityField.getText());
                                card.setCreditNumField(this.creditNumField.getText());
                                card.setMonthField(Integer.parseInt(this.monthField.getText()));
                                card.setYearField(Integer.parseInt(this.yearField.getText()));
                                card.setFirstnameField(this.firstnameField.getText());
                                card.setLastnameField(this.lastnameField.getText());
                                card.setPhoneField(this.phoneField.getText());
                                card.setStateField(this.stateField.getText());
                                card.setZipField(Integer.parseInt(this.zipField.getText()));
                                card.setUsername(this.username);
                                Alert Confirmation;
                                if (this.saveCreditCard(card)) {
                                    Confirmation = new Alert(AlertType.NONE, "Order placed Successfully!", new ButtonType[]{ButtonType.YES});
                                    Confirmation.showAndWait();
                                    FXMLLoader bookStoreLoader = new FXMLLoader(this.getClass().getResource("MerchandiseStore.fxml"));
                                    Parent bookStore = (Parent)bookStoreLoader.load();
                                    Scene registrationScene = new Scene(bookStore);
                                    Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
                                    MerchandiseController username1 = (MerchandiseController) bookStoreLoader.getController();
                                    username1.sendData(this.username);
                                    window.setScene(registrationScene);
                                    window.show();
                                } else {
                                    Confirmation = new Alert(AlertType.CONFIRMATION);
                                    Confirmation.setTitle("Payment failed");
                                    Confirmation.setHeaderText("Transaction Fail");
                                }
                            }
                        } else {
                            this.showAlert("Please enter your month and date");
                        }
                    } else {
                        this.showAlert("Please enter the year in the format of yy and it must be year of 18 or after 18 for example: 18 or 22");
                    }
                } else {
                    this.showAlert("Please enter the month in the format of mm for example: 02 or 11");
                }
            } else {
                this.showAlert("Please enter your month and date");
            }
        } else {
            this.showAlert("Your card credit card number must be 16 digits and cannot contain letter");
        }
    }

    @FXML
    public void CancelButtontoLogin(ActionEvent event) throws IOException {
        FXMLLoader merchandiseStoreLoader = new FXMLLoader(getClass().getResource("MerchandiseStore.fxml"));
        Parent merchandiseStore = merchandiseStoreLoader.load();
        Scene merchandiseStoreScene = new Scene(merchandiseStore);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        MerchandiseController merchandiseController = merchandiseStoreLoader.getController();
        merchandiseController.sendData(this.username); // Assuming you need to send data to MerchandiseController

        window.setScene(merchandiseStoreScene);
        window.show();
    }


    public void sendData(String username) {
        this.username = username;
        System.out.println("Hello " + username);
    }
    public boolean saveCreditCard(CreditCard card) {
        Connection connection = null;
        PreparedStatement insertCreditCard = null;
        PreparedStatement insertOrdered = null;
        PreparedStatement deleteFromCart = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/merchandise", "root", "lifeisboring..123");

            String insertCreditCardQuery = "INSERT INTO paymentTable (username, paymentType, creditNumber, expireDateMM, expireDateYY, firstName, lastName, billingAddress1, billingAddress2, city, state, zipcode, phone) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            insertCreditCard = connection.prepareStatement(insertCreditCardQuery);
            insertCreditCard.setString(1, card.getUsername());
            insertCreditCard.setString(2, card.getCardType());
            insertCreditCard.setString(3, card.getCreditNumField());
            insertCreditCard.setInt(4, card.getMonthField());
            insertCreditCard.setInt(5, card.getYearField());
            insertCreditCard.setString(6, card.getFirstnameField());
            insertCreditCard.setString(7, card.getLastnameField());
            insertCreditCard.setString(8, card.getBillingAddress1Field());
            insertCreditCard.setString(9, card.getBillingAddress2Field());
            insertCreditCard.setString(10, card.getCityField());
            insertCreditCard.setString(11, card.getStateField());
            insertCreditCard.setInt(12, card.getZipField());
            insertCreditCard.setString(13, card.getPhoneField());

            insertCreditCard.executeUpdate();

            String selectCartQuery = "SELECT * FROM carttable WHERE username = ?";
            PreparedStatement selectCartStatement = connection.prepareStatement(selectCartQuery);
            selectCartStatement.setString(1, card.getUsername());
            ResultSet rs = selectCartStatement.executeQuery();

            String insertOrderedQuery = "INSERT INTO orderedTable (username, merchandiseId, productName, manufacturer, model, year, price, sellBy) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            String deleteFromCartQuery = "DELETE FROM carttable WHERE username = ? AND merchandiseId = ?";
            insertOrdered = connection.prepareStatement(insertOrderedQuery);
            deleteFromCart = connection.prepareStatement(deleteFromCartQuery);

            while (rs.next()) {
                Merchandise existedBook = new Merchandise(rs.getInt("merchandiseId"), rs.getString("productName"), rs.getString("manufacturer"), rs.getInt("model"), rs.getInt("year"), rs.getFloat("price"), rs.getString("sellBy"));

                insertOrdered.setString(1, card.getUsername());
                insertOrdered.setInt(2, existedBook.getMerchandiseId());
                insertOrdered.setString(3, existedBook.getProductName());
                insertOrdered.setString(4, existedBook.getManufacturer());
                insertOrdered.setInt(5, existedBook.getModel());
                insertOrdered.setInt(6, existedBook.getYear());
                insertOrdered.setFloat(7, existedBook.getPrice());
                insertOrdered.setString(8, existedBook.getSellBy());
                insertOrdered.executeUpdate();

                deleteFromCart.setString(1, card.getUsername());
                deleteFromCart.setInt(2, existedBook.getMerchandiseId());
                deleteFromCart.executeUpdate();
            }

            insertCreditCard.close();
            insertOrdered.close();
            deleteFromCart.close();
            selectCartStatement.close();
            rs.close();

            return true; // Transaction succeeded
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Transaction failed
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
