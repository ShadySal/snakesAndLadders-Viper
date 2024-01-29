package com.example.snakesandladdersviper;

public class Player {
    private String name;
    private int position;
    private int PlayerNumber;
    public Player(String name, int PlayerNumber) {
        this.name = name;
        this.PlayerNumber = PlayerNumber;
        this.position = 1;
    }


    // Constructor
    public Player(String name) {
        this.name = name;
        this.position = 1;
    }


    public boolean isWinner() {
        // Check if the player has won
        return false;
    }


    public void chooseMove() {
        // Allow the player to choose their move (roll the dice, answer a question)
    }

    public void move(int steps) {
        // Move the player on the game board
    }

    public boolean answerQuestion(String difficulty) {
        // Check if the player answers a question correctly
        return false;
    }

    public void applySurprise(Tile tile) {
        // Handle the effects of landing on a surprise tile
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }


    public int getPlayerNumber() {
        return PlayerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        PlayerNumber = playerNumber;
    }


    public void setSelectedObject(String selectedObject) {
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", position=" + position +
                ", PlayerNumber=" + PlayerNumber +
                '}';
    }
}

