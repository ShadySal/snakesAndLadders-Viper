package com.example.snakesandladdersviper.Model;

public class Ladder {
    private int start; // Starting position of the ladder
    private int end;   // Ending position of the ladder
    private int length;

    public void setStart(int start) {
        this.start = start;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Ladder(int start, int end, int length) {
        this.start = start;
        this.end = end;
        this.length = length;
    }

    // Getter methods for start and end positions
    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }
    public void triggerEffect(Player player) {
        // Move the player to the endPosition when they land on the ladder
    }


    // Other methods related to the Ladder class
}
