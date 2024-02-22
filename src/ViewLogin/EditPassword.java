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

public class EditPassword {
    private String username;

    @FXML
    private TextField newPassField;

    @FXML
    private TextField oldPassField;

    @FXML
    private TextField confirmNewPassField;

    public EditPassword() {
    }

    @FXML
    public void Cancel(ActionEvent event) throws IOException {
        FXMLLoader editPassPage = new FXMLLoader(getClass().getResource("AccountEdit.fxml"));
        Parent editPassParent = editPassPage.load();
        Scene editPassScene = new Scene(editPassParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        AccountDetails newUsername = editPassPage.getController();
        newUsername.sendData(this.username);
        window.setScene(editPassScene);
        window.show();
    }

    @FXML
    public void Accept(ActionEvent event) throws IOException, NoSuchAlgorithmException {
        System.out.println("accept: " + this.username);
        String password = Security.hashPassword(oldPassField.getText());
        User existedUser = getUserInDB(username, password);
        Alert alert;
        if (newPassField.getText().equals(confirmNewPassField.getText())) {
            if (existedUser != null) {
                String pass = Security.hashPassword(newPassField.getText());
                changeAction(existedUser.userName, pass);
                Alert alert1 = new Alert(AlertType.NONE, "Password Changed Successfully!",
                        new ButtonType[]{ButtonType.OK});
                alert1.showAndWait();
                if (alert1.getResult() == ButtonType.OK) {
                    FXMLLoader editPage = new FXMLLoader(getClass().getResource("AccountEdit.fxml"));
                    Parent editParent = editPage.load();
                    Scene editScene = new Scene(editParent);
                    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    AccountDetails newUserName = editPage.getController();
                    newUserName.sendData(this.username);
                    window.setScene(editScene);
                    window.show();
                }
            } else {
                alert = new Alert(AlertType.NONE, "Change Failed! Please Check your previous password",
                        new ButtonType[]{ButtonType.YES});
                alert.show();
            }
        } else {
            alert = new Alert(AlertType.NONE, "Change Failed! New Password and Confirm New Password does not match!",
                    new ButtonType[]{ButtonType.YES});
            alert.show();
        }
    }

    private User getUserInDB(String userName, String password) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/merchandise","root","lifeisboring..123");
             PreparedStatement statement = connection
                     .prepareStatement("SELECT * FROM personTable WHERE userName = ? AND password = ?")) {

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
            e.printStackTrace();
            return null;
        }
    }

    private void changeAction(String userName, String pass) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/merchandise","root","lifeisboring..123");
             PreparedStatement statement = connection
                     .prepareStatement("UPDATE personTable SET password = ? WHERE userName = ?")) {

            statement.setString(1, pass);
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
