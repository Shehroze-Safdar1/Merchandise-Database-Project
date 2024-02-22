//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package ViewLogin;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class RegisterController {
    private User user;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField userName;
    @FXML
    private TextField email;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField confirm;
    @FXML
    private Button Create;
    @FXML
    private Button Cancel;

    public RegisterController() {
    }

    @FXML
    public void Create(ActionEvent event) {
        this.Create.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
            }
        });
    }

    @FXML
    public void showAlert(String alertString) {
        Alert errorAlert = new Alert(AlertType.ERROR);
        errorAlert.setTitle("Form Error!");
        errorAlert.setHeaderText(alertString);
        errorAlert.show();
    }

    @FXML
    public void createButtonClick(ActionEvent event) throws IOException, NoSuchAlgorithmException {
        if (this.firstName.getText().isEmpty()) {
            this.showAlert("Please enter your first name");
        } else if (this.lastName.getText().isEmpty()) {
            this.showAlert("Please enter your last name");
        } else if (this.userName.getText().isEmpty()) {
            this.showAlert("Please enter your username");
        } else if (this.email.getText().isEmpty()) {
            this.showAlert("Please enter your email");
        } else if (this.password.getText().isEmpty()) {
            this.showAlert("Please enter your password");
        } else if (this.confirm.getText().isEmpty()) {
            this.showAlert("Please enter your confirm password");
        } else if (!this.password.getText().equals(this.confirm.getText())) {
            this.showAlert("Your password and confirm password don't match");
        } else {
            User newUser = new User();
            newUser.firstName = this.firstName.getText();
            newUser.lastName = this.lastName.getText();
            newUser.userName = this.userName.getText();
            newUser.email = this.email.getText();
            newUser.password = Security.hashPassword(this.password.getText());
            User existedUser = this.getUserInDB(this.userName.getText());
            if (existedUser == null) {
                boolean isSavedSuccessfully = this.saveUserToDB(newUser);
                Alert alert;
                if (isSavedSuccessfully) {
                    alert = new Alert(AlertType.NONE, "Registration Succesful! Welcome to Youtube Merchandise", new ButtonType[]{ButtonType.OK});
                    alert.showAndWait();
                    if (alert.getResult() == ButtonType.OK) {
                        FXMLLoader bookStoreLoader = new FXMLLoader(this.getClass().getResource("MerchandiseStore.fxml"));
                        Parent bookStore = (Parent)bookStoreLoader.load();
                        Scene registrationScene = new Scene(bookStore);
                        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
                        MerchandiseController username = (MerchandiseController) bookStoreLoader.getController();
                        username.sendData(this.userName.getText());
                        window.setScene(registrationScene);
                        window.show();
                    }
                } else {
                    alert = new Alert(AlertType.ERROR, "Registration Failed!", new ButtonType[]{ButtonType.YES});
                    alert.show();
                }
            } else {
                Alert alert = new Alert(AlertType.ERROR, "Please enter different username!", new ButtonType[]{ButtonType.YES});
                alert.show();
            }

        }
    }

    private boolean saveUserToDB(User user) {
        Connection connection = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/merchandise","root","lifeisboring..123");
            String insertQuery = "INSERT INTO personTable (firstName, lastName, userName, email, password) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                ((PreparedStatement) preparedStatement).setString(1, user.firstName);
                preparedStatement.setString(2, user.lastName);
                preparedStatement.setString(3, user.userName);
                preparedStatement.setString(4, user.email);
                preparedStatement.setString(5, user.password);
                preparedStatement.executeUpdate();
            }
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

    private User getUserInDB(String userName) {
        Connection connection = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/merchandise","root","lifeisboring..123");
            String selectQuery = "SELECT * FROM personTable WHERE userName = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                preparedStatement.setString(1, userName);
                ResultSet rs = preparedStatement.executeQuery();
                if (!rs.next()) {
                    return null;
                }

                User existedUser = new User();
                existedUser.firstName = rs.getString("firstName");
                existedUser.lastName = rs.getString("lastName");
                existedUser.userName = rs.getString("userName");
                existedUser.email = rs.getString("email");
                existedUser.password = rs.getString("password");
                return existedUser;
            }
        } catch (SQLException var18) {
            System.err.println(var18.getMessage());
            return null;
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException var17) {
                System.err.println(var17);
            }
        }
    }

    @FXML
    public void CancelButtontoLogin(ActionEvent event) throws IOException {
        Parent Login = (Parent)FXMLLoader.load(this.getClass().getResource("Login.fxml"));
        Scene LoginScene = new Scene(Login);
        Stage window2 = (Stage)((Node)event.getSource()).getScene().getWindow();
        window2.setScene(LoginScene);
        window2.show();
    }
}
