package com.example.snakesandladdersviper.Controller;

import com.example.snakesandladdersviper.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

    public void StartGame() throws IOException {
        Stage currentStage = (Stage) StartGameBtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/snakesandladdersviper/InitializeGame.fxml"));
        currentStage.setTitle("Game Initialization");
        currentStage.setScene(new Scene(root, 800, 800));
    }
}
