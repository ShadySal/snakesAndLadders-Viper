package com.example.snakesandladdersviper.model;

import com.almasb.fxgl.entity.level.tiled.Tile;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GameBoard {
    private int rows;
    private int columns;
    private final Map<Integer, Integer> snakes; // Map from start position to end position of snakes
    private final Map<Integer, Integer> ladders; // Map from start position to end position of ladders
    private final Map<Integer, Tile> specialTiles; // Map of special tiles (question, surprise, etc.)

    public GameBoard(int difficulty) {
        // Initialize the board based on difficulty
        switch (difficulty) {
            case 1: // Easy
                this.rows = 7;
                this.columns = 7;
                break;
            case 2: // Medium
                this.rows = 10;
                this.columns = 10;
                break;
            case 3: // Hard
                this.rows = 13;
                this.columns = 13;
                break;

        }

        this.snakes = new HashMap<>();
        this.ladders = new HashMap<>();
        this.specialTiles = new HashMap<>();

        initializeBoard();
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

}

