package com.example.snakesandladdersviper.Model;

import java.time.LocalDate;

public class GameHistory {
    private String playerName;
    private int score;
    private LocalDate date;

    public GameHistory(String playerName, int score, LocalDate date) {
        this.playerName = playerName;
        this.score = score;
        this.date = date;
    }

    // Getters and Setters
    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "GameHistory{" +
                "playerName='" + playerName + '\'' +
                ", score=" + score +
                ", date=" + date +
                '}';
    }
}
