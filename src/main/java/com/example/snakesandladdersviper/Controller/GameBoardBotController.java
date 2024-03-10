package com.example.snakesandladdersviper.Controller;

import com.example.snakesandladdersviper.Enums.Difficulty;
import com.example.snakesandladdersviper.Model.GameBoard;
import com.example.snakesandladdersviper.Model.Player;
import com.example.snakesandladdersviper.Model.SysData;
import com.example.snakesandladdersviper.Model.Tile;
import com.example.snakesandladdersviper.Utils.SceneUtils;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Box;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

public class GameBoardBotController extends GameBoardController{
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
    @FXML
    private Button MainMenuButton;
    @FXML
    private Pane diceImageContainer;
    private Timeline timeline;
    private long startTime;
    // Example values for probabilities
    final double EASY_GAME_QUESTION_PROBABILITY = 0.1; // 10% chance for a question
    final double MEDIUM_GAME_QUESTION_PROBABILITY = EASY_GAME_QUESTION_PROBABILITY * 2; // 20% chance
    final double HARD_GAME_QUESTION_PROBABILITY = 0.25;
    final double REGULAR_NUMBER_PROBABILITY = 0.25; // 25% for a regular number
    final double EASY_MEDIUM_QUESTION_PROBABILITY = 0.50; // 50% for easy/medium questions

    @FXML
    private Box dice;
    private GameBoard gameBoard;
    private Difficulty difficulty;
    @FXML
    private Button diceRollButton;
    private int diceRollValue = 0;
    private Timeline diceRollAnimation;
    @FXML
    private Label currentPlayerLabel;
    private int currentPlayerIndex = 0;
    private List<Player> players = new ArrayList<Player>();
    private Map<Player, Circle> playerCircles;
    private Map<Player, ImageView> playerImages;
    private Set<Integer> occupiedPositions;
    private int bsize;
    private int globalSize;
    private Map<Integer, Integer> snakePositions = new HashMap<>();
    private Map<Integer, Integer> ladderPositions = new HashMap<>();
    private Map<Integer, String> SpecialTiles = new HashMap<>();


    public void initializeBoard(Difficulty difficulty, List<Player> players) {
        super.initializeBoard(difficulty, players);
    }

    private String generateRandomNumber(Difficulty difficulty) {
        Random random = new Random();
        // Always return a regular dice roll between 1 and 6, regardless of the difficulty
        return String.valueOf(random.nextInt(6) + 1);
    }
    private void setupDiceRollAnimation() {
        diceRollAnimation = new Timeline(new KeyFrame(Duration.millis(100), e -> {
            String diceOutcome = generateRandomNumber(difficulty);
            // Check if the outcome is a number or a question
            try {
                // If it's a number, parse it and update the button text
                diceRollValue = Integer.parseInt(diceOutcome);
                diceRollButton.setText(String.valueOf(diceRollValue));
            } catch (NumberFormatException ex) {
                // If it's not a number, it must be a question or another string outcome
                // Update the button text with the string outcome (like "EASY_QUESTION")
                diceRollButton.setText(diceOutcome);
            }
        }));
        diceRollAnimation.setCycleCount(10); // Adjust the cycle count to control the speed of the animation
    }
    @FXML
    private void onDiceRoll() {
        setupDiceRollAnimation();

        // Start the dice roll animation
        diceRollAnimation.play();

        // Handle the completion of the dice roll
        diceRollAnimation.setOnFinished(e -> {
            String finalOutcome = diceRollButton.getText();

            // Check if the final outcome is a number or a question
            if (isNumeric(finalOutcome)) {
                // If it's a number, parse it and handle it as a dice number
                int finalNumber = Integer.parseInt(finalOutcome);
                if (finalNumber == 0) {
                    Image image = new Image("/com/example/snakesandladdersviper/Images/" + finalNumber + ".jpg");
                    ImageView imageView = new ImageView(image);
                    imageView.setFitHeight(150); // Match the size of the diceImageContainer
                    imageView.setFitWidth(150);

                    // Clear previous content and add the new ImageView
                    diceImageContainer.getChildren().clear();
                    diceImageContainer.getChildren().add(imageView);
                    updatePlayerTurn();

                }
                if (finalNumber == 1) {
                    Image image = new Image("/com/example/snakesandladdersviper/Images/" + finalNumber + ".jpg");
                    ImageView imageView = new ImageView(image);
                    imageView.setFitHeight(150); // Match the size of the diceImageContainer
                    imageView.setFitWidth(150);

                    // Clear previous content and add the new ImageView
                    diceImageContainer.getChildren().clear();
                    diceImageContainer.getChildren().add(imageView);
                }
                if (finalNumber == 2) {
                    Image image = new Image("/com/example/snakesandladdersviper/Images/" + finalNumber + ".jpeg");
                    ImageView imageView = new ImageView(image);
                    imageView.setFitHeight(150); // Match the size of the diceImageContainer
                    imageView.setFitWidth(150);

                    // Clear previous content and add the new ImageView
                    diceImageContainer.getChildren().clear();
                    diceImageContainer.getChildren().add(imageView);
                }
                if (finalNumber == 3) {
                    Image image = new Image("/com/example/snakesandladdersviper/Images/" + finalNumber + ".jpeg");
                    ImageView imageView = new ImageView(image);
                    imageView.setFitHeight(150); // Match the size of the diceImageContainer
                    imageView.setFitWidth(150);

                    // Clear previous content and add the new ImageView
                    diceImageContainer.getChildren().clear();
                    diceImageContainer.getChildren().add(imageView);
                }
                if (finalNumber == 4) {
                    Image image = new Image("/com/example/snakesandladdersviper/Images/" + finalNumber + ".jpeg");
                    ImageView imageView = new ImageView(image);
                    imageView.setFitHeight(150); // Match the size of the diceImageContainer
                    imageView.setFitWidth(150);

                    // Clear previous content and add the new ImageView
                    diceImageContainer.getChildren().clear();
                    diceImageContainer.getChildren().add(imageView);
                }
                if (finalNumber == 5) {
                    Image image = new Image("/com/example/snakesandladdersviper/Images/" + finalNumber + ".jpeg");
                    ImageView imageView = new ImageView(image);
                    imageView.setFitHeight(150); // Match the size of the diceImageContainer
                    imageView.setFitWidth(150);

                    // Clear previous content and add the new ImageView
                    diceImageContainer.getChildren().clear();
                    diceImageContainer.getChildren().add(imageView);
                }
                if (finalNumber == 6) {
                    Image image = new Image("/com/example/snakesandladdersviper/Images/" + finalNumber + ".jpg");
                    ImageView imageView = new ImageView(image);
                    imageView.setFitHeight(150); // Match the size of the diceImageContainer
                    imageView.setFitWidth(150);

                    // Clear previous content and add the new ImageView
                    diceImageContainer.getChildren().clear();
                    diceImageContainer.getChildren().add(imageView);
                }
                // Handle the number outcome (e.g., move the player)
                movePlayer(finalNumber);
                // updatePlayerTurn();
            } else {
                // If it's not a number, it must be a question or another string outcome
                Image image = new Image("/com/example/snakesandladdersviper/Images/QuestionIcon.jpg");
                ImageView imageView = new ImageView(image);
                imageView.setFitHeight(150); // Match the size of the diceImageContainer
                imageView.setFitWidth(150);
                // Clear previous content and add the new ImageView
                diceImageContainer.getChildren().clear();
                diceImageContainer.getChildren().add(imageView);

            }
        });
    }

    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private void updatePlayerTurn() {
        if (players == null || players.isEmpty()) {
            System.out.println("Player list is empty or not initialized.");
            return;
        }
        // Increment the currentPlayerIndex and wrap around if it exceeds the size of the players list
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        System.out.println(currentPlayerIndex + " In updatePlayerTurn");

        // Get the current player based on the updated index
        Player currentPlayer = players.get(currentPlayerIndex);

        // Update the currentPlayerLabel with the current player's name
        if (currentPlayer != null) {
            currentPlayerLabel.setText("Player " + currentPlayer.getPlayerColor() + "'s Turn ");
        } else {
            System.out.println("Current player is null.");
        }
    }
    private void movePlayer(int steps) {

        Player currentPlayer = players.get(currentPlayerIndex);
        System.out.println(currentPlayerIndex + " in movePlayer");
        int newPosition = gameBoard.getPlayerPosition(currentPlayer) + steps;
        int totalTiles = getTotalTilesForDifficulty(difficulty);
        int temp = newPosition;
        if (steps == 0) {
            updatePlayerTurn();
        } else {
            if (newPosition < 1) {
                newPosition = 1;
            } else if (newPosition > totalTiles) {
                newPosition = totalTiles;
                handlePlayerWin(currentPlayer);
            }
            String tileColor = SpecialTiles.get(newPosition);
            if (tileColor != null) {
                // Special tile logic (questions, etc.)
                //handleSpecialTile(newPosition, tileColor);
            } else if (snakePositions.containsKey(newPosition)) {
                newPosition = snakePositions.get(newPosition);
                System.out.println("Player landed on a snake! Moved to position " + newPosition);
                temp = checkNewPosition(newPosition);
            } else if (ladderPositions.containsKey(newPosition)) {
                newPosition = ladderPositions.get(newPosition);
                System.out.println("Player landed on a ladder! Moved to position " + newPosition);
                temp = checkNewPosition(newPosition);
            }
        }
        if (temp != newPosition) {
            newPosition = temp;
        }
        gameBoard.setPlayerPosition(currentPlayer, newPosition);
        updatePlayerPositionOnBoard(currentPlayer);
        if (newPosition >= totalTiles) {
            handlePlayerWin(currentPlayer);
        } else {
            updatePlayerTurn();
        }
    }
    private void handlePlayerWin(Player currentPlayer) {
        // Get the game duration as a string
        String gameDurationStr = timeLabel.getText();
       // long gameDurationMillis = convertDurationToMillis(gameDurationStr);
        // Difficulty of the match
        String gameDifficulty = difficulty.toString();
        // Add the game data to history.json
        SysData.getInstance().addGameHistory(currentPlayer.getName(), gameDurationStr, difficulty.toString());

        Platform.runLater(() -> {
            // Capture the current full screen state
            Stage stage = (Stage) contentPane.getScene().getWindow();
            boolean wasFullScreen = stage.isFullScreen();

            Alert winAlert = new Alert(Alert.AlertType.INFORMATION);
            winAlert.setTitle("Congratulations!");
            winAlert.setHeaderText(null);
            winAlert.setContentText(currentPlayer.getName() + " has won the game!");

            // Add a "Home Screen" button
            ButtonType homeScreenButton = new ButtonType("End Game", ButtonBar.ButtonData.OK_DONE);
            winAlert.getButtonTypes().setAll(homeScreenButton);

            // Temporarily exit full screen to show the alert, then restore the state
            stage.setFullScreen(false);
            winAlert.showAndWait().ifPresent(response -> {
                if (response == homeScreenButton) {
                    // Navigate to the home screen
                    SceneUtils.changeScene(stage, "/com/example/snakesandladdersviper/hello-view.fxml", wasFullScreen);
                }
            });

            // Restore the full-screen mode after the alert is dismissed
            stage.setFullScreen(wasFullScreen);
        });
    }
    private int getTotalTilesForDifficulty(Difficulty difficulty) {
        switch (difficulty) {
            case EASY:
                return 7 * 7; // Assuming a 7x7 board for easy difficulty
            case MEDIUM:
                return 10 * 10; // Assuming a 10x10 board for medium difficulty
            case HARD:
                return 13 * 13; // Assuming a 13x13 board for hard difficulty
            default:
                throw new IllegalArgumentException("Unrecognized difficulty level");
        }
    }
    private void updatePlayerPositionOnBoard(Player player) {
        int newPosition = gameBoard.getPlayerPosition(player);

        // Convert this position to row and column on the grid
        int size = determineBoardSize(difficulty);
        int row = size - 1 - (newPosition / size);
        int column = (row % 2 == size % 2) ? size - 1 - (newPosition % size) : newPosition % size;

        // Find the corresponding tile
        Tile newTile = getTileByNumber(newPosition);

        if (newTile != null) {
            ImageView playerImage = playerImages.get(player);
            double newX = newTile.getX() + newTile.getWidth() / 2 - playerImage.getFitWidth() / 2;
            double newY = newTile.getY() + newTile.getHeight() / 2 - playerImage.getFitHeight() / 2;

            // If playerImage is not already in the scene, add it first to avoid null reference during animation
            if (!contentPane.getChildren().contains(playerImage)) {
                playerImage.setX(newX); // Set initial position to new position to prevent it from popping elsewhere
                playerImage.setY(newY);
                contentPane.getChildren().add(playerImage);
            } else {
                // Create and play the animation
                TranslateTransition transition = new TranslateTransition(Duration.seconds(1), playerImage);
                transition.setToX(newX - playerImage.getX());
                transition.setToY(newY - playerImage.getY());
                transition.play();
            }
        }
    }
    private Tile getTileByNumber(int tileNumber) {
        for (Node node : contentPane.getChildren()) { // If tiles are directly added to the gamepane
            if (node instanceof Tile) {
                Tile tile = (Tile) node;
                if (tile.getNumber() == tileNumber) {
                    return tile;
                }
            }
        }
        return null; // Return null if no matching tile is found
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


}
