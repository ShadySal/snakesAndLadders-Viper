package com.example.snakesandladdersviper.Controller;
import com.example.snakesandladdersviper.Model.Question;
import com.example.snakesandladdersviper.Model.SysData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import java.util.HashMap;
public class QuestionsPageController {

    @FXML
    private ListView<String> QuestionsView;

    @FXML
    private ListView<String> answersView;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TextField QuestionDifficulty;

    @FXML
    private TextField CorrectAnswer;

    private SysData sysData;


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
        // Add your edit logic here
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
        // Add your add logic here
    }

}
