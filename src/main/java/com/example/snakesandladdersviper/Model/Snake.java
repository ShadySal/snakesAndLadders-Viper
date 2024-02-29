package com.example.snakesandladdersviper.Model;

public class Snake {
    private int startPosition;
    private int endPosition;
    private String Type;


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

    public Snake(int startPosition, int endPosition, String type) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.Type = type;

    }

    public void triggerEffect(Player player) {
        // Move the player to the endPosition when they land on the snake
    }
    public int getStart(){
        return this.startPosition;
    }

    public String getType() {
        return this.Type;
    }
}
