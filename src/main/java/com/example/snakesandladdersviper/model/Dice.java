package com.example.snakesandladdersviper.Model;
import java.util.Random;
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


    // checks if the player will be asked a question
    public RollResult roll() {
        int rollValue = random.nextInt(maxRoll + 1);

        // Determine if a question should be asked
        boolean isQuestion = random.nextDouble() < questionProbability;

        return new RollResult(rollValue, isQuestion);
    }

    //static class to check roll result
    public static class RollResult {
        public final int value;
        public final boolean isQuestion;

        public RollResult(int value, boolean isQuestion) {
            this.value = value;
            this.isQuestion = isQuestion;
        }
    }
}