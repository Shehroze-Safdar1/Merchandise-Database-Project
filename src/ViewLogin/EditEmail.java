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
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class EditEmail {
    private String username;

    @FXML
    private TextField newEmailField;

    @FXML
    private TextField previousEmailField;

    @FXML
    private TextField passwordField;

    public EditEmail() {
    }

    @FXML
    public void Cancel(ActionEvent event) throws IOException {
        FXMLLoader editPage = new FXMLLoader(getClass().getResource("AccountEdit.fxml"));
        Parent editParent = editPage.load();
        Scene editScene = new Scene(editParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        AccountDetails newUserName = editPage.getController();
        newUserName.sendData(this.username);
        window.setScene(editScene);
        window.show();
    }

    @FXML
    public void Accept(ActionEvent event) throws IOException, NoSuchAlgorithmException {
        System.out.println("accept: " + this.username);
        String password = Security.hashPassword(passwordField.getText());
        User existedUser = getUserInDB(username, password, previousEmailField.getText());
        Alert alert;
        if (existedUser != null) {
            change(username, newEmailField.getText());
            alert = new Alert(AlertType.NONE, "Email Changed Successfully!", new ButtonType[]{ButtonType.YES});
            alert.show();
            FXMLLoader editPage = new FXMLLoader(getClass().getResource("AccountEdit.fxml"));
            Parent editParent = editPage.load();
            Scene editScene = new Scene(editParent);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            AccountDetails newUserName = editPage.getController();
            newUserName.sendData(username);
            window.setScene(editScene);
            window.show();
        } else {
            alert = new Alert(AlertType.ERROR, "Change Failed! Please Check your previous email or password",
                    new ButtonType[]{ButtonType.YES});
            alert.show();
        }
    }

    private User getUserInDB(String userName, String password, String email) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/merchandise","root","lifeisboring..123");
             PreparedStatement statement = connection
                     .prepareStatement("SELECT * FROM personTable WHERE userName = ? AND password = ? AND email = ?")) {

            statement.setString(1, userName);
            statement.setString(2, password);
            statement.setString(3, email);

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
            e.printStackTrace();
            return null;
        }
    }

    private void change(String userName, String email) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/merchandise","root","lifeisboring..123");
             PreparedStatement statement = connection
                     .prepareStatement("UPDATE personTable SET email = ? WHERE userName = ?")) {

            statement.setString(1, email);
            statement.setString(2, userName);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void sendData(String username) {
        this.username = username;
        System.out.println(username);
    }
}
