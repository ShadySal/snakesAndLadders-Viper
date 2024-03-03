package com.example.snakesandladdersviper.Model;



import com.example.snakesandladdersviper.Enums.Difficulty;
import com.example.snakesandladdersviper.Enums.GameEventType;

import java.util.*;

public class GameBoard implements GameSubject{
    private int rows;
    private int columns;
    private final Map<Integer, Integer> snakes; // Map from start position to end position of snakes
    private final Map<Integer, Integer> ladders; // Map from start position to end position of ladders
    private final Map<Integer, Tile> specialTiles; // Map of special tiles (question, surprise, etc.)
    private Map<Player, Integer> playerPositions;

    private ArrayList<Player> players;


    private Dice dice;
    private Difficulty difficulty;
    private List<GameObserver> observers = new ArrayList<>();

    @Override
    public void registerObserver(GameObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(GameEvent event) {
        for (GameObserver observer : observers) {
            observer.update(event);
        }
    }

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
    public void movePlayer(Player player, int roll) {
        int currentPosition = playerPositions.getOrDefault(player, 1);
        int newPosition = currentPosition + roll;

        int totalTiles = rows * columns;
        if (newPosition > totalTiles) {
            newPosition = totalTiles;
        }

        newPosition = adjustPosition(newPosition);

        // Update the player's position
        playerPositions.put(player, newPosition);

        // Notify observers about the player movement
        GameEvent event = new GameEvent(GameEventType.PLAYER_MOVED, player, newPosition, "Player moved");
        notifyObservers(event);
    }


    public void initializePlayerPositions(List<Player> players) {
        for (Player player : players) {
            playerPositions.put(player, 1); // Starting position is 1
        }
        this.players = new ArrayList<>(players);
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


    public int adjustPosition(int newPosition) {
        if (ladders.containsKey(newPosition)) {
            return ladders.get(newPosition); // Move up the ladder
        } else if (snakes.containsKey(newPosition)) {
            return snakes.get(newPosition); // Move down the snake
        }
        return newPosition; // Return the adjusted position
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
    public void setPlayerPosition(Player player, int newPosition) {
        // Logic to set player's position
        playerPositions.put(player, newPosition);
    }
    public void checkForGameEnd() {
        for (Player player : players) {
            if (checkPlayerWin(player)) {
                GameEvent event = new GameEvent(GameEventType.GAME_ENDED, player, playerPositions.get(player), "Player won the game");
                notifyObservers(event);
                break;
            }
        }
    }

    public boolean checkPlayerWin(Player player) {
        int winningPosition = rows * columns;
        if (playerPositions.get(player) >= winningPosition) {
            GameEvent winEvent = new GameEvent(GameEventType.PLAYER_WON, player, winningPosition, "Player won the game");
            notifyObservers(winEvent);
            return true;
        }
        return false;
    }

    public void isPlayerOnSpecialTiles(Player player) {
        Integer position = playerPositions.get(player);
        if (specialTiles.containsKey(position)) {
            GameEvent event = new GameEvent(GameEventType.PLAYER_ON_SPECIAL_TILE, player, position, "Player on special tile");
            notifyObservers(event);
        }
    }
}
