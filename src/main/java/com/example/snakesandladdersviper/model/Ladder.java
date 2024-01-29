package com.example.snakesandladdersviper.model;

public class Ladder {
    private int start; // Starting position of the ladder
    private int end;   // Ending position of the ladder

    public Ladder(int start, int end) {
        this.start = start;
        this.end = end;
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
