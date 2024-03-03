package com.example.snakesandladdersviper.Controller;

import com.example.snakesandladdersviper.Model.IQuestion;
import com.example.snakesandladdersviper.Model.Question;

import java.util.HashMap;

public class QuestionFactory {

    public static Question createQuestion(String questionText, HashMap<Integer, String> answers, int correctAns, int difficulty) {

        return new Question(questionText, answers, correctAns, difficulty);
    }

    private static String generateQuestionId(String questionText) {
        // Logic to generate a unique ID based on questionText
        return questionText.hashCode() + "_" + System.currentTimeMillis();
    }
}