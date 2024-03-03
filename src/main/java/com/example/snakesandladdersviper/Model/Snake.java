package com.example.snakesandladdersviper.Model;

import javafx.scene.paint.Color;

public class Snake {
    private int startPosition;
    private int endPosition;
    private String Type;


    public Snake(int startPosition, int endPosition, String type) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.Type = type;

    }

    public Color getColor() {
        switch (this.Type.toLowerCase()) {
            case "yellow":
                return Color.YELLOW;
            case "green":
                return Color.GREEN;
            case "blue":
                return Color.BLUE;
            case "red":
                return Color.RED;
            default:
                return Color.BLACK; // Default color if no type matches
        }
    }
    public void triggerEffect(Player player) {
        // Move the player to the endPosition when they land on the snake
    }
    public int getStart(){
        return this.startPosition;
    }

    @Override
    public String toString() {
        return "Snake{" +
                "startPosition=" + startPosition +
                ", endPosition=" + endPosition +
                ", Type='" + Type + '\'' +
                '}';
    }

    public String getType() {
        return this.Type;
    }


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



    public void setType(String type) {
        this.Type = type;
    }

}
