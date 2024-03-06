package com.example.snakesandladdersviper.Controller;

import com.example.snakesandladdersviper.Utils.SceneUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import static com.example.snakesandladdersviper.Utils.SceneUtils.showAlert;

public class AdminLoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";
    private boolean loggedIn = false;

    @FXML
    private Button loginButton;

    public boolean isLoggedIn() {
        return loggedIn;
    }
    @FXML
    public void loginButtonClicked(ActionEvent event) {
        String enteredUsername = usernameField.getText().trim();
        String enteredPassword = passwordField.getText().trim();

        if (ADMIN_USERNAME.equals(enteredUsername) && ADMIN_PASSWORD.equals(enteredPassword)) {
            loggedIn = true;
            // Close the login window/dialog here
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.close(); // Close the login window
            // Optionally, notify the main application that the login was successful if needed
        }
        else {
            Stage stage = (Stage) usernameField.getScene().getWindow();
            // Assuming SceneUtils is accessible and showAlert has been properly defined
            SceneUtils.showAlert("Login Error", "Incorrect username or password. Please try again.", stage, true);
        }
    }


}
