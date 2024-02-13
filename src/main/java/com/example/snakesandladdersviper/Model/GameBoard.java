package com.example.snakesandladdersviper.Model;



import com.example.snakesandladdersviper.Enums.Difficulty;

import java.util.*;

public class GameBoard {
    private int rows;
    private int columns;
    private final Map<Integer, Integer> snakes; // Map from start position to end position of snakes
    private final Map<Integer, Integer> ladders; // Map from start position to end position of ladders
    private final Map<Integer, Tile> specialTiles; // Map of special tiles (question, surprise, etc.)
    private Map<Player, Integer> playerPositions;

    private ArrayList<Player> players;


    private Dice dice;
    private Difficulty difficulty;


    public GameBoard(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.snakes = new HashMap<>();
        this.ladders = new HashMap<>();
        this.specialTiles = new HashMap<>();
        this.playerPositions = new HashMap<>();
        this.players = new ArrayList<>();
        initializeBoard();
    }
//    public GameBoard(int difficulty) {
//        // Initialize the board based on difficulty
//        switch (difficulty) {
//            case 1: // Easy
//                this.rows = 7;
//                this.columns = 7;
//                break;
//            case 2: // Medium
//                this.rows = 10;
//                this.columns = 10;
//                break;
//            case 3: // Hard
//                this.rows = 13;
//                this.columns = 13;
//                break;
//
//        }
//
//        this.snakes = new HashMap<>();
//        this.ladders = new HashMap<>();
//        this.specialTiles = new HashMap<>();
//
//        initializeBoard();
//    }

    public void initializePlayerPositions(List<Player> players) {
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            int position = i; // If players are placed in different rows in the first column
            playerPositions.put(player, position);
        }
        this.players = (ArrayList<Player>) players;
        System.out.println(this.players);
    }


    private void initializeBoard() {
        // Initialize snakes, ladders, and special tiles
        placeSnakes();
        placeLadders();
        placeSpecialTiles();
    }

    private void placeSnakes() {
        /* TO DO*/
    }

    private void placeLadders() {
        /* TO DO*/
    }

    private void placeSpecialTiles() {
        /* TO DO*/
    }

    public void movePlayer(Player player, int roll) {
        int currentPosition = playerPositions.getOrDefault(player, 1); // Starting position is 1
        int newPosition = currentPosition + roll;

        int totalTiles = rows * columns;
        // Check if the new position is beyond the board
        if (newPosition > totalTiles) {
            // You can either move the player to the last tile or not move them at all
            newPosition = totalTiles;
        }

        // Check for ladders and snakes
        if (ladders.containsKey(newPosition)) {
            newPosition = ladders.get(newPosition); // Move the player up the ladder
        } else if (snakes.containsKey(newPosition)) {
            newPosition = snakes.get(newPosition); // Move the player down the snake
        }

        // Check for special tiles
        if (specialTiles.containsKey(newPosition)) {
            // Handle special tile logic (e.g., trigger a question or event)
            // For example, you might want to call a method like handleSpecialTile(player, specialTiles.get(newPosition));
        }

        // Update the player's position
        playerPositions.put(player, newPosition);
    }



    public boolean isPlayerOnSpecialTile(Player player) {
        Integer position = playerPositions.get(player);
        return specialTiles.containsKey(position);
    }



    public boolean isSnakeHead(int position) {
        return snakes.containsKey(position);
    }

    public boolean isLadderBottom(int position) {
        return ladders.containsKey(position);
    }

    public boolean isSpecialTile(int position) {
        return specialTiles.containsKey(position);
    }

    public Tile  getSpecialTileType(int position) {
        return specialTiles.get(position);
    }

    public int getSnakeTail(int position) {
        return snakes.get(position);
    }

    public int getLadderTop(int position) {
        return ladders.get(position);
    }
    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public Map<Integer, Integer> getSnakes() {
        return snakes;
    }

    public Map<Integer, Integer> getLadders() {
        return ladders;
    }

    public Map<Integer, Tile> getSpecialTiles() {
        return specialTiles;
    }

    public Map<Player, Integer> getPlayerPositions() {
        return playerPositions;
    }

    public void setPlayerPositions(Map<Player, Integer> playerPositions) {
        this.playerPositions = playerPositions;
    }

    public Dice getDice() {
        return dice;
    }

    public void setDice(Dice dice) {
        this.dice = dice;
    }


    public void setPlayerPositions(Player player, int position) {
        playerPositions.put(player,position);

    }


    public int getPlayerPosition(Player player) {
        return playerPositions.getOrDefault(player, 0); // Default to position 0 if not found
}
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }
}
