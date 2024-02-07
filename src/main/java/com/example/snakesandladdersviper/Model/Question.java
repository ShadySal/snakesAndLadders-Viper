package com.example.snakesandladdersviper.Model;
import java.util.HashMap;
/**
 * A class representing a question for the KnightMove game.
 * It includes the question's level, text, and answer.
 */


import java.util.HashMap;

public class Question {
    private String questionText;
    private HashMap<Integer, String> answers;
    private int correctAns;
    private int difficulty;

    public Question(String questionText, HashMap<Integer, String> answers, int correctAns, int difficulty) {
        this.questionText = questionText;
        this.answers = answers;
        this.correctAns = correctAns;
        this.difficulty = difficulty;

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
