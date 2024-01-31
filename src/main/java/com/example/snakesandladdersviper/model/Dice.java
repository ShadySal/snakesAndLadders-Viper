package com.example.snakesandladdersviper.Model;
import java.util.Random;

public class Dice {
    private final Random random;
    private final int maxRoll;
    private final double questionProbability;

    public Dice(int maxRoll, double questionProbability) {
        this.random = new Random();
        this.maxRoll = maxRoll;
        this.questionProbability = questionProbability;
    }

    public int roll() {
        // Roll a number between 0 and maxRoll (inclusive)
        return random.nextInt(maxRoll + 1);
    }

    public boolean shouldAskQuestion() {
        // Determine if a question should be asked based on probability
        return random.nextDouble() < questionProbability;
    }
}