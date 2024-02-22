package ViewLogin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    // JDBC URL, username, and password for MySQL
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/merchandise";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "lifeisboring..123";

    public void start(Stage primaryStage) {
        try {
            // Load the FXML file
            Parent root = FXMLLoader.load(getClass().getResource("/ViewLogin/Login.fxml"));

            // Create the scene
            Scene scene = new Scene(root, 850.0, 570.0);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

            // Set up the stage
            primaryStage.setScene(scene);
            primaryStage.setResizable(true);
            primaryStage.show();

            System.out.println("Application started successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to connect to the database and create tables
    public static void connectToDB() {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // Create merchandiseTable
            String merchandiseTableQuery = "CREATE TABLE IF NOT EXISTS merchandiseTable " +
                    "(merchandiseId INT PRIMARY KEY, productName VARCHAR(255), " +
                    "manufacturer VARCHAR(255), model INT, year INT, price FLOAT, sellBy VARCHAR(255))";
            statement.executeUpdate(merchandiseTableQuery);

            // Create personTable (if not exists)
            String personTableQueryString = "CREATE TABLE IF NOT EXISTS personTable " +
                    "(firstName VARCHAR(255), lastName VARCHAR(255), " +
                    "userName VARCHAR(255) PRIMARY KEY, email VARCHAR(255), password VARCHAR(255))";
            statement.executeUpdate(personTableQueryString);

            // Create cartTable (if not exists)
            String cartTableQuery = "CREATE TABLE IF NOT EXISTS cartTable " +
                    "(userName VARCHAR(255), merchandiseId INT, productName VARCHAR(255), " +
                    "manufacturer VARCHAR(255), model INT, year INT, price FLOAT, sellBy VARCHAR(255), " +
                    "FOREIGN KEY (userName) REFERENCES personTable(userName), " +
                    "FOREIGN KEY (merchandiseId) REFERENCES merchandiseTable(merchandiseId))";
            statement.executeUpdate(cartTableQuery);

            // Create orderedTable (if not exists)
            String orderedQuery = "CREATE TABLE IF NOT EXISTS orderedTable " +
                    "(userName VARCHAR(255), merchandiseId INT, productName VARCHAR(255), " +
                    "manufacturer VARCHAR(255), model INT, year INT, price FLOAT, sellBy VARCHAR(255), " +
                    "FOREIGN KEY (userName) REFERENCES personTable(userName), " +
                    "FOREIGN KEY (merchandiseId) REFERENCES merchandiseTable(merchandiseId))";
            statement.executeUpdate(orderedQuery);


            // Create paymentTable (if not exists)
            String paymentTableQuery = "CREATE TABLE IF NOT EXISTS paymentTable " +
                    "(userName VARCHAR(255), paymentType VARCHAR(255), creditNumber VARCHAR(255), " +
                    "expireDateMM INT, expireDateYY INT, firstname VARCHAR(255), lastname VARCHAR(255), " +
                    "billingAddress1 VARCHAR(255), billingAddress2 VARCHAR(255), city VARCHAR(255), " +
                    "state VARCHAR(255), zipcode INT, phone VARCHAR(255))";
            statement.executeUpdate(paymentTableQuery);



            System.out.println("Tables created successfully.");
        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Connect to the database and create tables
        connectToDB();

        // Launch the JavaFX application
        launch(args);
    }
}
