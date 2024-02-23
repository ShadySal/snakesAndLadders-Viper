package com.example.snakesandladdersviper.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML
    private Button GameHistoryBtn;

    @FXML
    private Button QuestionManagementBtn;

    @FXML
    private Button StartGameBtn;

    @FXML
    private Button InstructionsBtn;

    @FXML
    private Button BackButton;

    public void StartGame() throws IOException {
        Stage currentStage = (Stage) StartGameBtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/snakesandladdersviper/gameMode.fxml"));
        currentStage.setTitle("Select Game Mode");
        currentStage.setScene(new Scene(root, 800, 800));

        // Set full-screen mode
        currentStage.setFullScreen(true);

        // Show the stage
        currentStage.show();
    }


    public void InstructionsOfGame(ActionEvent event) throws IOException {
        Parent InstructionPage = FXMLLoader.load(getClass().getResource("/com/example/snakesandladdersviper/Instructions.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(InstructionPage));
        stage.show();
    }

    @FXML
    void BackButton(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/snakesandladdersviper/hello-view.fxml"));
            Parent root = loader.load();
            Scene nextScene = new Scene(root);
// Get the current stage and set the new scene
            Stage currentStage = (Stage) BackButton.getScene().getWindow();
            currentStage.setScene(nextScene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void onQuestionManagementBtnClicked(ActionEvent event) throws IOException {
        // Load AdminLogin.fxml
        FXMLLoader adminLoader = new FXMLLoader(getClass().getResource("/com/example/snakesandladdersviper/AdminLogin.fxml"));
        Parent adminLoginParent = adminLoader.load();

        // Get the controller to set up any necessary communication
        AdminLoginController adminLoginController = adminLoader.getController();

        // Create a new stage for the admin login dialog
        Stage adminLoginStage = new Stage();
        adminLoginStage.setScene(new Scene(adminLoginParent));
        adminLoginStage.setTitle("Admin Login");
        adminLoginStage.showAndWait(); // Show the login dialog and wait for it to close

        // After login dialog is closed, check if the admin login was successful
        if (adminLoginController.isLoggedIn()) {
            // Admin successfully logged in, proceed to question management
            Parent questionsPage = FXMLLoader.load(getClass().getResource("/com/example/snakesandladdersviper/QuestionsPage.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(questionsPage));
            stage.show();
        } else {
            // Admin login unsuccessful, show an error message to the user
            showAlert("Access Denied", "You must log in as an admin to access this feature.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }



}
