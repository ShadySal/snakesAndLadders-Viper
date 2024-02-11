package com.example.snakesandladdersviper.Controller;
import com.example.snakesandladdersviper.Model.Question;
import com.example.snakesandladdersviper.Model.SysData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.*;

import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
public class QuestionsPageController {

    @FXML
    private ListView<String> QuestionsView;

    @FXML
    private ListView<String> answersView;

    @FXML
    private Button editButton;
    @FXML
    private Button BackButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TextField QuestionDifficulty;

    @FXML
    private TextField CorrectAnswer;

    private SysData sysData;


    @FXML
    private Button addButton;


    @FXML
    void BackButton(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/snakesandladdersviper/hello-view.fxml"));
            Parent root = loader.load();
            Scene nextScene = new Scene(root);
            Stage currentStage = (Stage) BackButton.getScene().getWindow();
            currentStage.setScene(nextScene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void initialize() {
        sysData = SysData.getInstance();
        QuestionDifficulty.setEditable(false);
        CorrectAnswer.setEditable(false);
        loadQuestions();

        // Set listener for question selection
        QuestionsView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                displayAnswers(newValue);
            }
        });
    }
    private void loadQuestions() {
        QuestionsView.getItems().clear();
        for (Question question : sysData.getQuestions()) {
            QuestionsView.getItems().add(question.getQuestionText());
        }
    }
    @FXML
    void BackButtonFunc(ActionEvent event) throws IOException {
        // Create a confirmation alert
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText("Are you sure you want to Cancel Initializing the game and back to the Main Menu?");
        confirmationAlert.setContentText("Any unsaved changes may be lost.");

        // Add OK and Cancel buttons to the alert
        confirmationAlert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        // Show the alert and wait for user input
        confirmationAlert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                // User clicked OK, proceed with transferring to the main menu

                try {
                    // Back to the main menu
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/snakesandladdersviper/hello-view.fxml"));
                    Parent root = loader.load();
                    Scene nextScene = new Scene(root);

                    // Get the current stage and set the new scene
                    Stage currentStage = (Stage) BackButton.getScene().getWindow();
                    currentStage.setScene(nextScene);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                // User clicked Cancel, do nothing or handle accordingly
            }
        });
    }
    private void displayAnswers(String selectedQuestion) {
        answersView.getItems().clear();
        Question selected = sysData.getQuestions().stream()
                .filter(q -> q.getQuestionText().equals(selectedQuestion))
                .findFirst().orElse(null);

        if (selected != null) {

            selected.getAnswers().values().forEach(answersView.getItems()::add);

            // Display correct answer and difficulty
            CorrectAnswer.setText(selected.getAnswers().get(selected.getCorrectAns()));
            QuestionDifficulty.setText(String.valueOf(selected.getDifficulty()));
        }
    }


    @FXML
    void onEditButtonClicked(ActionEvent event) {
        // TO-DO
    }

    @FXML
    void onDeleteButtonClicked(ActionEvent event) {
        String selectedQuestionText = QuestionsView.getSelectionModel().getSelectedItem();
        if (selectedQuestionText != null) {
            sysData.removeQuestion(selectedQuestionText);
            loadQuestions();
        }
    }

    @FXML
    void onAddButtonClicked(ActionEvent event) {
    //To-Do

    }

}
