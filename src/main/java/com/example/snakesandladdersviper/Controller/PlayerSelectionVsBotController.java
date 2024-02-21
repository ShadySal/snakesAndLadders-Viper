package com.example.snakesandladdersviper.Controller;

import com.example.snakesandladdersviper.Enums.Difficulty;
import com.example.snakesandladdersviper.Model.GameBoard;
import com.example.snakesandladdersviper.Model.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

import com.example.snakesandladdersviper.Enums.Difficulty;
import com.example.snakesandladdersviper.Model.GameBoard;
import com.example.snakesandladdersviper.Model.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class PlayerSelectionVsBotController {



        @FXML
        private Pane contentPane;

        @FXML
        public Text PlayerSelectionTurn;

        @FXML
        public TextField PlayerName;

        @FXML
        public ComboBox<String> ObjectSelect;

        private int currentPlayerNumber;
        private int totalPlayers;
        private List<Player> players;
        @FXML
        private Button BackButton;
        private Difficulty difficulty;
        final double EASY_GAME_QUESTION_PROBABILITY = 0.1; // 10% chance for a question
        final double MEDIUM_GAME_QUESTION_PROBABILITY = EASY_GAME_QUESTION_PROBABILITY * 2; // 20% chance
        private GameBoard gameBoard;
        private int numberOfBots;


    public List<Player> getPlayers() {
            return players;
        }
        public TextField getPlayerName() {
            // Implement the logic to return the player name TextField
            return PlayerName;
        }
        public ComboBox<String> getObjectSelect() {
            // Implement the logic to return the ObjectSelect ComboBox
            return ObjectSelect;
        }
        public Text getPlayerSelectionTurn() {
            // Implement the logic to return the PlayerSelectionTurn Text
            return PlayerSelectionTurn;
        }

        public void initialize() {
            ObjectSelect.getItems().addAll("Red", "Blue", "Black", "Yellow", "Orange", "Green");
            players = new ArrayList<>();
            currentPlayerNumber = 1;
            PlayerSelectionTurn.setText("Player " + currentPlayerNumber);
        }

        public void setDifficulty(Difficulty diff){
            this.difficulty = diff;
        }

        public void setPlayerData(int playerNumber, Player player, int currentPlayerNumber, int totalPlayers) {
            this.currentPlayerNumber = currentPlayerNumber;
            this.totalPlayers = totalPlayers;
            players.add(player);
        }
        @FXML
        void BackButtonFunc(ActionEvent event) throws IOException {
            // Create a confirmation alert
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation");
            confirmationAlert.setHeaderText("Are you sure you want to Cancel Initializing the game and back to the Main Menu?");
            confirmationAlert.setContentText("Any unsaved changes may be lost.");

            // Add OK and Cancel buttons to the alert
            confirmationAlert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

            // Show the alert and wait for user input
            confirmationAlert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == ButtonType.OK) {
                    // User clicked OK, proceed with transferring to the main menu

                    // Remove the last added player from the list
                    if (!players.isEmpty()) {
                        players.remove(players.size() - 1);
                    }

                    try {
                        // Back to the main menu
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/snakesandladdersviper/hello-view.fxml"));
                        Parent root = loader.load();
                        Scene nextScene = new Scene(root);

                        // Get the current stage and set the new scene
                        Stage currentStage = (Stage) BackButton.getScene().getWindow();
                        currentStage.setScene(nextScene);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    // User clicked Cancel, do nothing or handle accordingly
                }
            });
        }



    private void startGame() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/snakesandladdersviper/Gameboard.fxml"));
            Parent gameBoardRoot = loader.load();
            GameBoardController gameBoardController = loader.getController();
            gameBoardController.initializeBoard(difficulty, players);
            contentPane.getChildren().setAll(gameBoardRoot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        @FXML
        public void savePlayerSelection() {

                String playerName = PlayerName.getText();
                String selectedColorName = ObjectSelect.getValue();

                if (playerName.isEmpty() || selectedColorName == null) {
                    showAlert("Error", "Please enter a username and select a color.");
                    return;
                }

                // Create real player
                Player realPlayer = new Player(playerName, 1, selectedColorName);
                players.add(realPlayer);
                ObjectSelect.getItems().remove(selectedColorName);

                // Create bots based on difficulty
                createBots();

                // Start the game
                startGame();

        }
    private void createBots() {
        List<String> availableColors = ObjectSelect.getItems();
        Random random = new Random();
        int botCount = determineBotCountBasedOnDifficulty(difficulty);

        for (int i = 1; i <= botCount; i++) {
            String botColor = availableColors.remove(random.nextInt(availableColors.size()));
            players.add(new Player("Bot" + i, i + 1, botColor));
        }
    }

    private int determineBotCountBasedOnDifficulty(Difficulty difficulty) {
        switch (difficulty) {
            case EASY:   return 3; // Example: 3 bots for easy level
            case MEDIUM: return 5; // Example: 5 bots for medium level
            case HARD:   return 7; // Example: 7 bots for hard level
            default:     return 3; // Default to 3 bots
        }
    }
        private void showAlert(String title, String content) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.showAndWait();
        }

        //gets color of the player
        private Color getColorFromString(String colorName) {
            switch (colorName.toLowerCase()) {
                case "red":
                    return Color.RED;
                case "blue":
                    return Color.BLUE;
                case "green":
                    return Color.GREEN;
                case "yellow":
                    return Color.YELLOW;
                case "orange":
                    return Color.ORANGE;
                case "white":
                    return Color.WHITE;
                default:
                    return Color.BLACK; // Default color or throw an exception
            }
        }

        public int getBoardDifficulty(Difficulty diff){
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


    public void setNumOfBots(int numberOfPlayers) {
            this.numberOfBots = numberOfPlayers;
    }
}

