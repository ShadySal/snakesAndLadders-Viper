package com.example.snakesandladdersviper.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AdminLoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";
    private boolean loggedIn = false;

    // Existing methods...

    public boolean isLoggedIn() {
        return loggedIn;
    }
    @FXML
    private void loginButtonClicked() {
        String enteredUsername = usernameField.getText();
        String enteredPassword = passwordField.getText();

        if (ADMIN_USERNAME.equals(enteredUsername) && ADMIN_PASSWORD.equals(enteredPassword)) {
            loggedIn = true; // Set login status to true
            closeStage();
        } else {
            showLoginErrorAlert(); // Display error alert for incorrect credentials
        }
    }


    private void showLoginErrorAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Error");
        alert.setHeaderText(null);
        alert.setContentText("Incorrect username or password. Please try again.");
        alert.showAndWait();
    }
    private void closeStage() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }
}
