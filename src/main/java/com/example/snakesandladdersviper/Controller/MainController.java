package com.example.snakesandladdersviper.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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

    @FXML
    private Button InstructionsBtn;

    public void StartGame() throws IOException {
        Stage currentStage = (Stage) StartGameBtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/snakesandladdersviper/InitializeGame.fxml"));
        currentStage.setTitle("Game Initialization");
        currentStage.setScene(new Scene(root, 800, 800));
    }

    public void InstructionsOfGame(ActionEvent event) throws IOException {
        Parent InstructionPage = FXMLLoader.load(getClass().getResource("/com/example/snakesandladdersviper/Instructions.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(InstructionPage));
        stage.show();
    }
    @FXML
    private void onQuestionManagementBtnClicked(ActionEvent event) throws IOException {
        // Load QuestionsPage.fxml
        Parent questionsPage = FXMLLoader.load(getClass().getResource("/com/example/snakesandladdersviper/QuestionsPage.fxml"));
        // Get the stage from the event source
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        // Set the new scene on the stage
        stage.setScene(new Scene(questionsPage));

        // Show the stage
        stage.show();
    }
}
