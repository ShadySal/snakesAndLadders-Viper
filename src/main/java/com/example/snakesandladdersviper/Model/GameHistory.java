package com.example.snakesandladdersviper.Model;

public class GameHistory {
    private String winnerName;
    private String duration; // Assuming duration is a string like "30 minutes". Change to appropriate type if needed (e.g., int for minutes).
    private String difficulty;

    public GameHistory(String winnerName, String duration, String difficulty) {
        this.winnerName = winnerName;
        this.duration = duration;
        this.difficulty = difficulty;
    }

    // Getters and Setters
    public String getWinnerName() {
        return winnerName;
    }

    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public String toString() {
        return "GameHistory{" +
                "winnerName='" + winnerName + '\'' +
                ", duration='" + duration + '\'' +
                ", difficulty='" + difficulty + '\'' +
                '}';
    }
}
