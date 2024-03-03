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

public class EditQuestionController {
    @FXML
    private TextField QuestionTextField;

    @FXML
    private RadioButton Answer1;

    @FXML
    private TextField Answer1TextField;

    @FXML
    private RadioButton Answer2;

    @FXML
    private TextField Answer2TextField;

    @FXML
    private RadioButton Answer3;

    @FXML
    private TextField Answer3TextField;

    @FXML
    private RadioButton Answer4;

    @FXML
    private TextField Answer4TextField;

    @FXML
    private Button SubmitButton;


    @FXML
    private Button BackButton1;

    @FXML
    private ChoiceBox<String> LevelChoiceBox;
    private ToggleGroup answersToggleGroup;
    private String questionSelected;


    @FXML
    private void initialize() {
        LevelChoiceBox.getItems().addAll("Easy", "Medium", "Hard");
        answersToggleGroup = new ToggleGroup();
        Answer1.setToggleGroup(answersToggleGroup);
        Answer2.setToggleGroup(answersToggleGroup);
        Answer3.setToggleGroup(answersToggleGroup);
        Answer4.setToggleGroup(answersToggleGroup);
    }
    public void setQuestionDetails(Question question) {
        QuestionTextField.setText(question.getQuestionText());

        // Set the corresponding radio button as selected
        int correctAnswer = question.getCorrectAns();
        RadioButton selectedRadioButton = (RadioButton) answersToggleGroup.getToggles().get(correctAnswer - 1);
        selectedRadioButton.setSelected(true);

        // Populate answer text fields
        HashMap<Integer, String> answers = question.getAnswers();
        Answer1TextField.setText(answers.get(1));
        Answer2TextField.setText(answers.get(2));
        Answer3TextField.setText(answers.get(3));
        Answer4TextField.setText(answers.get(4));

        // Set difficulty in ChoiceBox
        String difficulty;
        switch (question.getDifficulty()) {
            case 1: difficulty = "Easy"; break;
            case 2: difficulty = "Medium"; break;
            case 3: difficulty = "Hard"; break;
            default: difficulty = "Unknown"; break;
        }
        LevelChoiceBox.setValue(difficulty);
    }




    @FXML
    private void onSubmitButtonClicked(ActionEvent event) {
        // Validate input fields
        if (!isInputValid()) {
            showAlert("Invalid Input", "Please fill in all fields and select a correct answer.");
            return;
        }

        // Get values from text fields
        String questionText = QuestionTextField.getText();
        String answer1 = Answer1TextField.getText();
        String answer2 = Answer2TextField.getText();
        String answer3 = Answer3TextField.getText();
        String answer4 = Answer4TextField.getText();

        // Determine the correct answer based on selected RadioButton
        RadioButton selectedRadioButton = (RadioButton) answersToggleGroup.getSelectedToggle();
        int correctAnswerIndex = answersToggleGroup.getToggles().indexOf(selectedRadioButton) + 1;

        // Get difficulty level from ChoiceBox
        int difficulty = getDifficultyLevel(LevelChoiceBox.getValue());

        // Create a new question object
        HashMap<Integer, String> answers = new HashMap<>();
        answers.put(1, answer1);
        answers.put(2, answer2);
        answers.put(3, answer3);
        answers.put(4, answer4);

        Question updatedQuestion = new Question(questionText, answers, correctAnswerIndex, difficulty);

        // Update the question in SysData
        SysData.getInstance().updateQuestion(updatedQuestion); // Implement this method in SysData

        // Show confirmation alert
        showAlert("Success", "Question updated successfully.");

        // Optionally, close the window or redirect
        // ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }

    private boolean isInputValid() {
        return !QuestionTextField.getText().isEmpty() &&
                !Answer1TextField.getText().isEmpty() &&
                !Answer2TextField.getText().isEmpty() &&
                !Answer3TextField.getText().isEmpty() &&
                !Answer4TextField.getText().isEmpty() &&
                answersToggleGroup.getSelectedToggle() != null;
    }

    private int getDifficultyLevel(String difficulty) {
        switch (difficulty) {
            case "Easy": return 1;
            case "Medium": return 2;
            case "Hard": return 3;
            default: return 0; // Handle unknown case
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    void BackButtonFunc(ActionEvent event) throws IOException {
        // Create a confirmation alert
        Stage stage = (Stage) BackButton1.getScene().getWindow();
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.initOwner(stage);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText("Are you sure you want to back to the managing questions?");
        confirmationAlert.setContentText("");

        // Add OK and Cancel buttons to the alert
        confirmationAlert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        // Show the alert and wait for user input
        confirmationAlert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                // User clicked OK, proceed with transferring to the main menu

                try {
                    // Back to the main menu
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/snakesandladdersviper/QuestionsPage.fxml"));
                    Parent root = loader.load();
                    Scene nextScene = new Scene(root);

                    // Get the current stage and set the new scene
                    Stage currentStage = (Stage) BackButton1.getScene().getWindow();
                    currentStage.setScene(nextScene);
                    currentStage.setFullScreen(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                // User clicked Cancel, do nothing or handle accordingly
            }
        });
    }



}



