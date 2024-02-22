//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package ViewLogin;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AccountDetails {
    private String username;

    public AccountDetails() {
    }

    public void EditEmail(ActionEvent event) throws IOException {
        FXMLLoader editEmailPage = new FXMLLoader(this.getClass().getResource("EditEmail.fxml"));
        Parent editEmailParent = (Parent)editEmailPage.load();
        Scene editEmailScene = new Scene(editEmailParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        EditEmail email = (EditEmail)editEmailPage.getController();
        email.sendData(this.username);
        window.setScene(editEmailScene);
        window.show();
    }

    public void EditPassword(ActionEvent event) throws IOException {
        FXMLLoader editPassPage = new FXMLLoader(this.getClass().getResource("EditPassword.fxml"));
        Parent editPassParent = (Parent)editPassPage.load();
        Scene editPassScene = new Scene(editPassParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        EditPassword email = (EditPassword)editPassPage.getController();
        email.sendData(this.username);
        window.setScene(editPassScene);
        window.show();
    }

    public void EditAll(ActionEvent event) throws IOException {
        FXMLLoader editAllPage = new FXMLLoader(this.getClass().getResource("EditAll.fxml"));
        Parent editAllarent = (Parent)editAllPage.load();
        Scene editAllScene = new Scene(editAllarent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        EditAll newUserName = (EditAll)editAllPage.getController();
        newUserName.sendData(this.username);
        window.setScene(editAllScene);
        window.show();
    }

    public void CheckoutButton(ActionEvent event) throws IOException {
        FXMLLoader creditPage = new FXMLLoader(this.getClass().getResource("CreditCardTransaction.fxml"));
        Parent creditParent = (Parent)creditPage.load();
        Scene creditScene = new Scene(creditParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        TransactionController newUserName = (TransactionController)creditPage.getController();
        newUserName.sendData(this.username);
        window.setScene(creditScene);
        window.show();
    }

    public void sellButton(ActionEvent event) throws IOException {
        Parent Login = (Parent)FXMLLoader.load(this.getClass().getResource("Selling.fxml"));
        Scene LoginScene = new Scene(Login);
        Stage window2 = (Stage)((Node)event.getSource()).getScene().getWindow();
        window2.setScene(LoginScene);
        window2.show();
    }

    public void homeButton(ActionEvent event) throws IOException {
        FXMLLoader homePage = new FXMLLoader(this.getClass().getResource("MerchandiseStore.fxml"));
        Parent homeParent = (Parent)homePage.load();
        Scene homeScene = new Scene(homeParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        MerchandiseController newUserName = (MerchandiseController) homePage.getController();
        newUserName.sendData(this.username);
        window.setScene(homeScene);
        window.show();
    }

    public void ShoppingCartButton(ActionEvent event) throws IOException {
        FXMLLoader shoppingCartPage = new FXMLLoader(this.getClass().getResource("ShoppingCart.fxml"));
        Parent shoppingCartParent = (Parent)shoppingCartPage.load();
        Scene shoppingCartScene = new Scene(shoppingCartParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        ShoppingCartController newUserName = (ShoppingCartController)shoppingCartPage.getController();
        newUserName.sendData(this.username);
        window.setScene(shoppingCartScene);
        window.show();
    }

    public void sendData(String text) {
        this.username = text;
        System.out.println(this.username);
    }
}
