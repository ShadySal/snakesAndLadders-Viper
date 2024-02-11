package com.example.snakesandladdersviper.Controller;

import com.example.snakesandladdersviper.Model.Question;
import com.example.snakesandladdersviper.Model.SysData;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.HashMap;

public class AddQuestionController {

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
    private Button BackButton;

    @FXML
    private ChoiceBox<String> LevelChoiceBox;
    private ToggleGroup answersToggleGroup;


    public void initialize() {
        answersToggleGroup = new ToggleGroup();
        Answer1.setToggleGroup(answersToggleGroup);
        Answer2.setToggleGroup(answersToggleGroup);
        Answer3.setToggleGroup(answersToggleGroup);
        Answer4.setToggleGroup(answersToggleGroup);

        LevelChoiceBox.getItems().addAll("Easy","Medium","Hard"); // Assuming 3 difficulty levels
        LevelChoiceBox.setValue("Easy"); // Default value
    }

    @FXML
    private void onSubmitButtonClicked() {
        if (QuestionTextField.getText().isEmpty() ||
                Answer1TextField.getText().isEmpty() ||
                Answer2TextField.getText().isEmpty() ||
                Answer3TextField.getText().isEmpty() ||
                Answer4TextField.getText().isEmpty() ||
                answersToggleGroup.getSelectedToggle() == null) {

            showAlert("Error", "Please fill all fields and select the correct answer.");
            return;
        }

        RadioButton selectedAnswer = (RadioButton) answersToggleGroup.getSelectedToggle();
        int correctAnswerIndex = getAnswerIndex(selectedAnswer);

        HashMap<Integer, String> answers = new HashMap<>();
        answers.put(1, Answer1TextField.getText());
        answers.put(2, Answer2TextField.getText());
        answers.put(3, Answer3TextField.getText());
        answers.put(4, Answer4TextField.getText());

        Question newQuestion = new Question(
                QuestionTextField.getText(),
                answers,
                correctAnswerIndex,
                QuestionLevelNum(LevelChoiceBox.getValue())
        );

        SysData.getInstance().addQuestion(newQuestion);
        clearForm();

    }

    private int QuestionLevelNum(String value) {
        switch (value.toLowerCase()) {
            case "easy":
                return 1;
            case "medium":
                return 2;
            case "hard":
                return 3;
            default:
                return 1; // Default level if no match is found
        }
    }

    private void clearForm() {
        QuestionTextField.clear();
        Answer1TextField.clear();
        Answer2TextField.clear();
        Answer3TextField.clear();
        Answer4TextField.clear();
        answersToggleGroup.getSelectedToggle().setSelected(false);
        LevelChoiceBox.setValue("Easy");
    }
    private int getAnswerIndex(RadioButton selectedAnswer) {
        if (selectedAnswer == Answer1) return 1;
        if (selectedAnswer == Answer2) return 2;
        if (selectedAnswer == Answer3) return 3;
        return 4; // Default to Answer4
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }



}
