package com.example.snakesandladdersviper.Model;

import java.util.HashMap;

public interface IQuestion {

        String getQuestionText();
        HashMap<Integer, String> getAnswers();
        int getCorrectAns();
        int getDifficulty();
        String getQuestionId();
        // Other common methods


}
