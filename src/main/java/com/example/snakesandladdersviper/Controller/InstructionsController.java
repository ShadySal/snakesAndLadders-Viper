package com.example.snakesandladdersviper.Controller;

import com.example.snakesandladdersviper.Utils.SceneUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneUtils.changeScene(currentStage, "/com/example/snakesandladdersviper/hello-view.fxml", true);
    }


    public void StartGame(ActionEvent event) throws IOException {
        Stage stage = (Stage) StartGameBtn.getScene().getWindow();
        SceneUtils.changeScene(stage, "/com/example/snakesandladdersviper/gameMode.fxml", true);
    }
}
