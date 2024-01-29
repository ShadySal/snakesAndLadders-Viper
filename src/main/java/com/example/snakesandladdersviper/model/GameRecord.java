package com.example.snakesandladdersviper.model;

public class GameRecord {
    private String winnerName;
    private long timePlayed;
    private String difficultyLevel;

    public GameRecord(String winnerName, long timePlayed, String difficultyLevel) {
        this.winnerName = winnerName;
        this.timePlayed = timePlayed;
        this.difficultyLevel = difficultyLevel;
    }

    // Getters and setters for the attributes

    public String getWinnerName() {
        return winnerName;
    }

    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName;
    }

    public long getTimePlayed() {
        return timePlayed;
    }

    public void setTimePlayed(long timePlayed) {
        this.timePlayed = timePlayed;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    @Override
    public String toString() {
        return "Winner: " + winnerName + ", Time Played: " + timePlayed + " seconds, Difficulty: " + difficultyLevel;
    }
}
