package ViewLogin;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;


public class LoginController {

    @FXML
    private TextField userField;

    @FXML
    private PasswordField passField;

    public LoginController() {
    }

    private User getUserInDB(String userName, String password) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/merchandise","root","lifeisboring..123");
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM personTable WHERE userName = ? AND password = ?")) {

            statement.setString(1, userName);
            statement.setString(2, password);

            try (ResultSet rs = statement.executeQuery()) {
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
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    @FXML
    public void Login(ActionEvent event) throws IOException, NoSuchAlgorithmException {
        String password = Security.hashPassword(passField.getText());
        User existedUser = getUserInDB(userField.getText(), password);
        Alert alert;

        if (existedUser != null) {
            alert = new Alert(AlertType.NONE, "Login Successful! Welcome to Youtube Merchandise",
                    new ButtonType[]{ButtonType.OK});
            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {
                FXMLLoader bookStoreLoader = new FXMLLoader(getClass().getResource("MerchandiseStore.fxml"));
                Parent bookStore = bookStoreLoader.load();
                Scene registrationScene = new Scene(bookStore);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                MerchandiseController username = bookStoreLoader.getController();
                username.sendData(userField.getText());
                window.setScene(registrationScene);
                window.show();
            }
        } else {
            alert = new Alert(AlertType.ERROR, "Login Failed! User does not exist or password is incorrect",
                    new ButtonType[]{ButtonType.YES});
            alert.show();
        }
    }

    @FXML
    public void Create(ActionEvent event) throws IOException {
        Parent Registration = FXMLLoader.load(getClass().getResource("Registration.fxml"));
        Scene RegistrationScene = new Scene(Registration);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(RegistrationScene);
        window.show();
    }
}