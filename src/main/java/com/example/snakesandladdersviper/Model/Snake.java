package com.example.snakesandladdersviper.Model;

import com.example.snakesandladdersviper.Model.Player;

public class Snake {
    private int startPosition;
    private int endPosition;
    private String Color;

    public int getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }

    public int getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(int endPosition) {
        this.endPosition = endPosition;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }

    public Snake(int startPosition, int endPosition) {
        // Initialize snake attributes here
    }

    public void triggerEffect(Player player) {
        // Move the player to the endPosition when they land on the snake
    }
    public int getStart(){
        return this.startPosition;
    }
}
