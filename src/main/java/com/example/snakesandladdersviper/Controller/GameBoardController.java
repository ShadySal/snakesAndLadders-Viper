package com.example.snakesandladdersviper.Controller;

import com.example.snakesandladdersviper.Model.Player;
import com.example.snakesandladdersviper.Model.Tile;
import com.example.snakesandladdersviper.Enums.Difficulty;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.util.Duration;

import java.util.List;


public class GameBoardController {

    @FXML
    private GridPane BoardGrid;

    @FXML
    private Pane contentPane;

    @FXML
    private Pane gamepane;

    @FXML
    private Pane gameDataPane;

    @FXML
    private Label timeLabel;

    @FXML
    private Label LevelLabel;

    private Timeline timeline;
    private long startTime;


    public void initialize() {
        startTime = System.currentTimeMillis();
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateClock()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void updateClock() {
        long now = System.currentTimeMillis();
        long elapsedMillis = now - startTime;
        int elapsedSeconds = (int) (elapsedMillis / 1000);
        int seconds = elapsedSeconds % 60;
        int minutes = (elapsedSeconds / 60) % 60;
        int hours = elapsedSeconds / 3600;

        timeLabel.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
    }


    public void initializeBoard(Difficulty difficulty) {
        int size = determineBoardSize(difficulty);
        setupGridConstraints(size); // Set up grid constraints
        LevelLabel.setText("Level: "+ difficulty);
        BoardGrid.getChildren().clear(); // Clear existing content

        BoardGrid.prefWidthProperty().bind(gamepane.widthProperty());
        BoardGrid.prefHeightProperty().bind(gamepane.heightProperty());
        gamepane.setPadding(new Insets(0, 0, 0, 0)); // Remove padding if not needed
        GridPane.setMargin(BoardGrid, new Insets(0)); // Remove margin if not needed


        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                // Calculate tile number starting from bottom left
                int number = (size * (size - row)) - col;
                Tile tile = new Tile(col, size - row - 1);
                // Customize the tile...
                tile.setStyle("-fx-background-color: green" + "; -fx-border-color: black;");
                BoardGrid.add(tile, col, size - row - 1); // Place tile in the grid
            }
        }
    }

    private void setupGridConstraints(int size) {
        BoardGrid.getColumnConstraints().clear();
        BoardGrid.getRowConstraints().clear();

        // Define percentage-based constraints to make the grid cells fill the pane
        for (int i = 0; i < size; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / size); // Each column takes an equal share
            BoardGrid.getColumnConstraints().add(colConst);

            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / size); // Each row takes an equal share
            BoardGrid.getRowConstraints().add(rowConst);
        }
    }


    private int determineBoardSize(Difficulty difficulty) {
        switch (difficulty) {
            case EASY:
                return 7;
            case MEDIUM:
                return 10;
            case HARD:
                return 13;
            default:
                throw new IllegalArgumentException("Unrecognized difficulty level");
        }
    }

    public void startGame(List<Player> players) {

        for (Player player : players) {
            // For each player, perform necessary setup
            // This could include placing player tokens on the board,
            // setting player scores, etc.

            // Example: Initialize player position on the board
            initializePlayerPosition(player);
        }

    }
    private void initializePlayerPosition(Player player) {
        // Example implementation - adjust as per your game logic
        // Place the player at the start position (e.g., tile number 1)
        int startPosition = 1; // Define the start position for your game
        // Locate the start tile on the board and place the player token
        // This could be done by updating the UI of the Tile, adding a marker, etc.
    }

    // Other methods...
}
