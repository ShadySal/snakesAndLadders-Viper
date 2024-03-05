package com.example.snakesandladdersviper.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

public class AdminLoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";
    private boolean loggedIn = false;

    @FXML
    private Button loginButton; // You might not need to explicitly define this unless you're doing more than just the FXML's onAction

    public boolean isLoggedIn() {
        return loggedIn;
    }

    @FXML
    public void loginButtonClicked(ActionEvent event) {
        String enteredUsername = usernameField.getText().trim();
        String enteredPassword = passwordField.getText().trim();

        if (ADMIN_USERNAME.equals(enteredUsername) && ADMIN_PASSWORD.equals(enteredPassword)) {
            loggedIn = true;
            // Assuming you have some logic here to close the login window or dialog
            closeWindow();
        } else {
            showLoginErrorAlert();
        }
    }

    private void closeWindow() {
        // Close the login window. You might need a reference to the Stage.
        // If this controller is used in a dialog, you don't need to manually close the window here.
        // This is just a placeholder for whatever logic you need to execute after a successful login.
    }

    private void showLoginErrorAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Error");
        alert.setHeaderText(null);
        alert.setContentText("Incorrect username or password. Please try again.");
        alert.showAndWait();
    }
}
