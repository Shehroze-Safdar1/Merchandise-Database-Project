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

public class EditAll {
    private String username;

    @FXML
    private TextField newPassField;

    @FXML
    private PasswordField oldPassField;

    @FXML
    private TextField confirmNewPassField;

    @FXML
    private TextField oldEmailField;

    @FXML
    private TextField newEmailField;

    public EditAll() {
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
        String password = Security.hashPassword(oldPassField.getText());
        User existedUser = getUserInDB(username, password, oldEmailField.getText());
        Alert alert;
        if (newPassField.getText().equals(confirmNewPassField.getText())) {
            if (existedUser != null) {
                String pass = Security.hashPassword(newPassField.getText());
                changeAction(existedUser.userName, pass, newEmailField.getText());
                Alert alert1 = new Alert(AlertType.NONE, "Password Changed Successfully!", new ButtonType[]{ButtonType.OK});
                alert1.showAndWait();
                if (alert1.getResult() == ButtonType.OK) {
                    FXMLLoader editPage = new FXMLLoader(getClass().getResource("AccountEdit.fxml"));
                    Parent editParent = editPage.load();
                    Scene editScene = new Scene(editParent);
                    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    AccountDetails newUserName = editPage.getController();
                    newUserName.sendData(username);
                    window.setScene(editScene);
                    window.show();
                }
            } else {
                alert = new Alert(AlertType.NONE, "Change Failed! Please Check your previous password or email",
                        new ButtonType[]{ButtonType.YES});
                alert.show();
            }
        } else {
            alert = new Alert(AlertType.NONE, "Change Failed! New Password and Confirm New Password does not match!",
                    new ButtonType[]{ButtonType.YES});
            alert.show();
        }
    }

    private User getUserInDB(String userName, String password, String email) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://your-mysql-host:3306/merchandise",
                "root", "lifeisboring..123");
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

    private void changeAction(String userName, String pass, String email) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/merchandise","root","lifeisboring..123");
             PreparedStatement statement = connection
                     .prepareStatement("UPDATE personTable SET password = ?, email = ? WHERE userName = ?")) {

            statement.setString(1, pass);
            statement.setString(2, email);
            statement.setString(3, userName);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void sendData(String text) {
        this.username = text;
        System.out.println(this.username);
    }
}
