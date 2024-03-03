package com.example.snakesandladdersviper.Model;
import java.util.HashMap;
/**
 * It includes the question's level, text, and answer.
 */


import java.util.HashMap;

public class Question implements IQuestion {
    private String questionText;
    private HashMap<Integer, String> answers;
    private int correctAns;
    private int difficulty;
    private String questionId;

    public Question(String questionText, HashMap<Integer, String> answers,
                    int correctAns, int difficulty) {
        this.questionText = questionText;
        this.answers = answers;
        this.correctAns = correctAns;
        this.difficulty = difficulty;
        this.questionId = generateQuestionId(questionText); // Generate a unique ID based on questionText
    }
    private String generateQuestionId(String questionText) {
        // Logic to generate a unique ID
        return questionText.hashCode() + "_" + System.currentTimeMillis();
    }
    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public HashMap<Integer, String> getAnswers() {
        return answers;
    }

    public void setAnswers(HashMap<Integer, String> answers) {
        this.answers = answers;
    }

    public int getCorrectAns() {
        return correctAns;
    }

    public void setCorrectAns(int correctAns) {
        this.correctAns = correctAns;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }



    @Override
    public String toString() {
        return "Question{" +
                "questionText='" + questionText + '\'' +
                ", answers=" + answers +
                ", correctAns=" + correctAns +
                ", difficulty=" + difficulty +
                '}';
    }


}
