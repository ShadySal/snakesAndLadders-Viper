package com.example.snakesandladdersviper.Controller;
import com.example.snakesandladdersviper.Model.Question;
import com.example.snakesandladdersviper.Model.SysData;
import com.example.snakesandladdersviper.Utils.SceneUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.*;

import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
public class QuestionsPageController {

    @FXML
    private AnchorPane MainPane;

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
        Stage stage = (Stage) BackButton.getScene().getWindow();
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.initOwner(stage);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText("Are you sure you want to back to the Main Menu?");
        confirmationAlert.setContentText("");

        // Add OK and Cancel buttons to the alert
        confirmationAlert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        // Show the alert and wait for user input
        confirmationAlert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                // User clicked OK, proceed with transferring to the main menu
                SceneUtils.changeScene(stage, "/com/example/snakesandladdersviper/hello-view.fxml", true);
            }
            // If User clicked Cancel, do nothing or handle accordingly
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

    private void onEditButtonClicked(ActionEvent event) throws IOException {
        String selectedQuestionText = QuestionsView.getSelectionModel().getSelectedItem();
        if (selectedQuestionText == null) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            SceneUtils.showAlert("No Question Selected", "Please select a question to edit.", stage, true);
            return;
        }
        Question selectedQuestion = findQuestionByText(selectedQuestionText);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/snakesandladdersviper/editQuestion.fxml"));
        Parent editQuestionRoot = loader.load();

        EditQuestionController editQuestionController = loader.getController();
        editQuestionController.setQuestionDetails(selectedQuestion);
        // Ensure MainPane is cleared and ready for new content
        MainPane.getChildren().clear(); // Clear existing content

        // Set the new content as the only child of MainPane
        AnchorPane.setTopAnchor(editQuestionRoot, 0.0);
        AnchorPane.setRightAnchor(editQuestionRoot, 0.0);
        AnchorPane.setBottomAnchor(editQuestionRoot, 0.0);
        AnchorPane.setLeftAnchor(editQuestionRoot, 0.0);
        MainPane.getChildren().add(editQuestionRoot);
    }
    private Question findQuestionByText(String questionText) {
        for (Question q : SysData.getInstance().getQuestions()) {
            if (q.getQuestionText().equals(questionText)) {
                return q;
            }
        }
        return null;
    }





    @FXML
    void onAddButtonClicked(ActionEvent event) throws IOException{
        // Load addQuestion.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/snakesandladdersviper/addQuestion.fxml"));
        Parent addQuestionParent = loader.load();
        // Ensure MainPane is cleared and ready for new content
        MainPane.getChildren().clear(); // Clear existing content

        // Set the new content as the only child of MainPane
        AnchorPane.setTopAnchor(addQuestionParent, 0.0);
        AnchorPane.setRightAnchor(addQuestionParent, 0.0);
        AnchorPane.setBottomAnchor(addQuestionParent, 0.0);
        AnchorPane.setLeftAnchor(addQuestionParent, 0.0);
        MainPane.getChildren().add(addQuestionParent);
    }
    @FXML
    void onDeleteButtonClicked(ActionEvent event) {
        String selectedQuestionText = QuestionsView.getSelectionModel().getSelectedItem();
        if (selectedQuestionText == null) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            SceneUtils.showAlert("No Question Selected", "Please select a question to delete.", stage, true);
            return;

        }
        if (selectedQuestionText != null) {
            // Create a confirmation alert
            Stage stage = (Stage) deleteButton.getScene().getWindow();
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.initOwner(stage);
            confirmationAlert.setTitle("Confirmation");
            confirmationAlert.setHeaderText("Are you sure you want to delete the selected question?");
            confirmationAlert.setContentText("This action cannot be undone.");

            // Add OK and Cancel buttons to the alert
            confirmationAlert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

            // Show the alert and wait for user input
            confirmationAlert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == ButtonType.OK) {
                    // User confirmed, proceed with deleting the question
                    sysData.removeQuestion(selectedQuestionText);
                    loadQuestions();
                    answersView.getItems().clear();
                }
            });
        }
    }


}
