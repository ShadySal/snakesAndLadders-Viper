package com.example.snakesandladdersviper.Controller;

import com.example.snakesandladdersviper.Utils.SceneUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;

public class InstructionsController {

    @FXML
    public Button BackButton;
    @FXML
    private Button StartGameBtn;

    @FXML
    void BackButton(ActionEvent event) {
        Stage stage = (Stage) BackButton.getScene().getWindow();

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.initOwner(stage);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText("Are you sure you want to back to the Main Menu?");
        confirmationAlert.setContentText("");
        confirmationAlert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        // Show the alert and wait for user input
        confirmationAlert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                SceneUtils.changeScene(stage, "/com/example/snakesandladdersviper/hello-view.fxml", true);
            }
        });
    }

    public void StartGame(ActionEvent event) throws IOException {
        Stage stage = (Stage) StartGameBtn.getScene().getWindow();
        SceneUtils.changeScene(stage, "/com/example/snakesandladdersviper/InitializeGame.fxml", true);
    }
}
