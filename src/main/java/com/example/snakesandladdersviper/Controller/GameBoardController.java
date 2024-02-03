package com.example.snakesandladdersviper.Controller;

import com.example.snakesandladdersviper.Model.Dice;
import com.example.snakesandladdersviper.Model.GameBoard;
import com.example.snakesandladdersviper.Model.Player;
import com.example.snakesandladdersviper.Model.Tile;
import com.example.snakesandladdersviper.Enums.Difficulty;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import javafx.scene.paint.Color;
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
    // Example values for probabilities
    final double EASY_GAME_QUESTION_PROBABILITY = 0.1; // 10% chance for a question
    final double MEDIUM_GAME_QUESTION_PROBABILITY = EASY_GAME_QUESTION_PROBABILITY * 2; // 20% chance

    private GameBoard gameBoard;
    private Difficulty difficulty;

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

    public void initializeBoard(Difficulty difficulty, List<Player> players) {
        int size = determineBoardSize(difficulty);

        gameBoard = new GameBoard(size, size);
        if (size == 7) {
            Dice easyGameDice = new Dice(6, EASY_GAME_QUESTION_PROBABILITY);
            this.difficulty = difficulty;

            gameBoard.setDice(easyGameDice);

        } else if (size == 10) {
            Dice mediumGameDice = new Dice(9, MEDIUM_GAME_QUESTION_PROBABILITY); // 0-6 for movement, 7-9 for questions
            gameBoard.setDice(mediumGameDice);
            this.difficulty = difficulty;
        } else if (size == 13) {
            // TO DO
            this.difficulty = difficulty;

        }

        gameBoard.initializePlayerPositions(players);

        System.out.println(players);

        setupGridConstraints(size); // Set up grid constraints
        LevelLabel.setText("Level: " + difficulty);
        BoardGrid.getChildren().clear(); // Clear existing content

        BoardGrid.prefWidthProperty().bind(gamepane.widthProperty());
        BoardGrid.prefHeightProperty().bind(gamepane.heightProperty());
        gamepane.setPadding(new Insets(0, 0, 0, 0)); // Remove padding if not needed
        GridPane.setMargin(BoardGrid, new Insets(0)); // Remove margin if not needed
        for (int row = size - 1; row >= 0; row--) {
            for (int col = 0; col < size; col++) {
                // Calculate tile number starting from bottom left
                int number = (size * row) + col + 1;  // Adjusted calculation
                Tile tile = new Tile(col, size - row - 1);

                // Alternate between two colors (e.g., green and white)
                String backgroundColor;
                if ((row + col) % 2 == 0) {
                    backgroundColor = "-fx-background-color: green";
                } else {
                    backgroundColor = "-fx-background-color: white";
                }

                // Customize the tile...
                tile.setStyle(backgroundColor + "; -fx-border-color: black;");

                // Create a label to display the number
                Label label = new Label(Integer.toString(number));
                label.setStyle("-fx-text-fill: black; -fx-font-size: 12;");  // Adjust font color and size

                // Add the label to the upper left corner of the tile
                tile.getChildren().add(label);
                GridPane.setValignment(label, VPos.TOP);
                GridPane.setHalignment(label, HPos.LEFT);

                BoardGrid.add(tile, col, size - row - 1);
            }
        }
        displayPlayers(players);
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
//
//    public void initializeBoard(Difficulty difficulty, List<Player> players) {
//        int size = determineBoardSize(difficulty);
//
//        gameBoard = new GameBoard(size, size);
//        if (size == 7) {
//            Dice easyGameDice = new Dice(6, EASY_GAME_QUESTION_PROBABILITY);
//            this.difficulty = difficulty;
//
//            gameBoard.setDice(easyGameDice);
//
//        } else if (size == 10) {
//            Dice mediumGameDice = new Dice(9, MEDIUM_GAME_QUESTION_PROBABILITY); // 0-6 for movement, 7-9 for questions
//            gameBoard.setDice(mediumGameDice);
//            this.difficulty = difficulty;
//        } else if (size == 13) {
//            // TO DO
//            this.difficulty = difficulty;
//
//        }
//
//        gameBoard.initializePlayerPositions(players);
//
//        System.out.println(players);
//
//        setupGridConstraints(size); // Set up grid constraints
//        LevelLabel.setText("Level: " + difficulty);
//        BoardGrid.getChildren().clear(); // Clear existing content
//
//        BoardGrid.prefWidthProperty().bind(gamepane.widthProperty());
//        BoardGrid.prefHeightProperty().bind(gamepane.heightProperty());
//        gamepane.setPadding(new Insets(0, 0, 0, 0)); // Remove padding if not needed
//        GridPane.setMargin(BoardGrid, new Insets(0)); // Remove margin if not needed
//        for (int row = size - 1; row >= 0; row--) {
//            for (int col = 0; col < size; col++) {
//                // Calculate tile number starting from bottom left
//                int number = (size * row) + col + 1;  // Adjusted calculation
//                Tile tile = new Tile(col, size - row - 1);
//
//                // Alternate between two colors (e.g., green and white)
//                String backgroundColor;
//                if ((row + col) % 2 == 0) {
//                    backgroundColor = "-fx-background-color: green";
//                } else {
//                    backgroundColor = "-fx-background-color: white";
//                }
//
//                // Customize the tile...
//                tile.setStyle(backgroundColor + "; -fx-border-color: black;");
//
//                // Create a label to display the number
//                Label label = new Label(Integer.toString(number));
//                label.setStyle("-fx-text-fill: black; -fx-font-size: 12;");  // Adjust font color and size
//
//                // Add the label to the tile
//                tile.getChildren().add(label);
//
//                BoardGrid.add(tile, col, size - row - 1);
//            }
//        }
//        displayPlayers(players);
//    }



//    private void setupGridConstraints(int size) {
//        BoardGrid.getColumnConstraints().clear();
//        BoardGrid.getRowConstraints().clear();
//
//        // Define percentage-based constraints to make the grid cells fill the pane
//        for (int i = 0; i < size; i++) {
//            ColumnConstraints colConst = new ColumnConstraints();
//            colConst.setPercentWidth(100.0 / size); // Each column takes an equal share
//            BoardGrid.getColumnConstraints().add(colConst);
//
//            RowConstraints rowConst = new RowConstraints();
//            rowConst.setPercentHeight(100.0 / size); // Each row takes an equal share
//            BoardGrid.getRowConstraints().add(rowConst);
//        }
//    }
//    public void initializeBoard(Difficulty difficulty, List<Player> players) {
//        int size = determineBoardSize(difficulty);
//
//        gameBoard = new GameBoard(size,size);
//        if(size == 7){
//            Dice easyGameDice = new Dice(6, EASY_GAME_QUESTION_PROBABILITY);
//            this.difficulty=difficulty;
//
//            gameBoard.setDice(easyGameDice);
//
//        }
//        if(size == 10){
//            Dice mediumGameDice = new Dice(9, MEDIUM_GAME_QUESTION_PROBABILITY); // 0-6 for movement, 7-9 for questions
//            gameBoard.setDice(mediumGameDice);
//            this.difficulty=difficulty;
//        }
//        if(size == 13){
//            // TO DO
//            this.difficulty=difficulty;
//
//        }
//        gameBoard.initializePlayerPositions(players);
//
//        System.out.println(players);
//
//        setupGridConstraints(size); // Set up grid constraints
//        LevelLabel.setText("Level: "+ difficulty);
//        BoardGrid.getChildren().clear(); // Clear existing content
//
//        BoardGrid.prefWidthProperty().bind(gamepane.widthProperty());
//        BoardGrid.prefHeightProperty().bind(gamepane.heightProperty());
//        gamepane.setPadding(new Insets(0, 0, 0, 0)); // Remove padding if not needed
//        GridPane.setMargin(BoardGrid, new Insets(0)); // Remove margin if not needed
//        for (int row = 0; row < size; row++) {
//            for (int col = 0; col < size; col++) {
//                // Calculate tile number starting from bottom left
//                int number = (size * (size - row)) - col;
//                Tile tile = new Tile(col, size - row - 1);
//
//                // Alternate between two colors (e.g., green and white)
//                String backgroundColor;
//                if ((row + col) % 2 == 0) {
//                    backgroundColor = "-fx-background-color: green";
//                } else {
//                    backgroundColor = "-fx-background-color: white";
//                }
//
//                // Customize the tile...
//                tile.setStyle(backgroundColor + "; -fx-border-color: black;");
//                BoardGrid.add(tile, col, size - row - 1); // Place tile in the grid
//            }
//        }
//        displayPlayers(players);
//
//    }
//        for (int row = 0; row < size; row++) {
//            for (int col = 0; col < size; col++) {
//                // Calculate tile number starting from bottom left
//                int number = (size * (size - row)) - col;
//                Tile tile = new Tile(col, size - row - 1);
//                // Customize the tile...
//                tile.setStyle("-fx-background-color: green" + "; -fx-border-color: black;");
//                BoardGrid.add(tile, col, size - row - 1); // Place tile in the grid
//            }
//        }




    // Create a dice instance for a medium game

    private int determineBoardSize(Difficulty difficulty) {
        switch (difficulty) {
            case EASY:
                // Create a die instance for an easy game
                Dice easyGameDice = new Dice(6, EASY_GAME_QUESTION_PROBABILITY);
                return 7;
            case MEDIUM:
                Dice mediumGameDice = new Dice(9, MEDIUM_GAME_QUESTION_PROBABILITY); // 0-6 for movement, 7-9 for questions
                return 10;
            case HARD:
                return 13;
            default:
                throw new IllegalArgumentException("Unrecognized difficulty level");
        }
    }

    public void startGame(List<Player> players) {
        // Initialize game logic here

        // Display players on the board
        displayPlayers(players);

        // Other game start logic...
    }


    //display players on Gameboard
    public void displayPlayers(List<Player> players) {
        for (Player player : players) {
            Circle playerCircle = new Circle(10); // Radius of 10, adjust as needed
            playerCircle.setFill(player.getPlayerColor());

            // Get the tile for the player
            Pane tile = getTileForPlayer(player);

            // Check if the tile is valid
            if (tile != null) {
                // Add the circle to the tile
                tile.getChildren().add(playerCircle);
 }}
    }


    private Pane getTileForPlayer(Player player) {
        // Retrieve the player's position on the board
        int position = gameBoard.getPlayerPosition(player) - 1; // Adjust if your positions start at 1

        // Calculate the row and column from the position
        int size = determineBoardSize(difficulty);
        int row = position / size;
        int column = position % size;

        // Find the corresponding tile Pane
        for (Node node : BoardGrid.getChildren()) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                return (Pane) node; // Cast to Pane, assuming each tile is a Pane
            }
        }
        return null; // Tile not found or invalid position
    }


}
