package com.example.snakesandladdersviper.Controller;
import com.example.snakesandladdersviper.Enums.Difficulty;
import com.example.snakesandladdersviper.Model.GameBoard;
import com.example.snakesandladdersviper.Model.Player;
import com.example.snakesandladdersviper.Model.PlayerCreator;
import com.example.snakesandladdersviper.Utils.SceneUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
public class PlayerSelectionController {

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
    private PlayerCreator playerCreator = new PlayerCreator.DefaultPlayerCreator();


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
        ObjectSelect.getItems().addAll("Red", "Blue", "Pink", "Yellow", "Orange", "Green");
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
    void BackButtonFunc(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Create a confirmation alert
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.initOwner(stage);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText("Are you sure you want to Cancel Initializing the game and back to the Main Menu?");
        confirmationAlert.setContentText("Any unsaved changes may be lost.");

        // Add OK and Cancel buttons to the alert
        confirmationAlert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        // Show the alert and wait for user input
        confirmationAlert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                // User clicked OK, proceed with transferring to the main menu

                // Remove the last added player from the list if any
                if (!players.isEmpty()) {
                    players.remove(players.size() - 1);
                }

                // Use SceneUtils to change the scene smoothly
                SceneUtils.changeScene(stage, "/com/example/snakesandladdersviper/hello-view.fxml", true);
            } else {
                // User clicked Cancel, do nothing or handle accordingly
            }
        });
    }


    private void updateUIForNextPlayer() {
        // Clear previous player's selections

        PlayerName.clear();
        ObjectSelect.getSelectionModel().clearSelection();
        PlayerSelectionTurn.setText("Player " + currentPlayerNumber);

        /* TO DO!!!!
          Name Check if it has been chosen or not
         */
        // Update UI elements to indicate the next player's turn (if needed)
        // You can update labels or display messages here.
    }

    private void startGame() {
        try {
            // Load and initialize the game board
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/snakesandladdersviper/Gameboard.fxml"));
            Parent gameBoardRoot = loader.load();
            GameBoardController gameBoardController = loader.getController();

            System.out.println(players);
            // Assuming 'difficulty' and 'players' are already defined and initialized
            gameBoardController.initializeBoard(difficulty, players);

            // Replace contentPane content with the game board
            contentPane.getChildren().setAll(gameBoardRoot);

            // Initialize the game logic
            //gameBoardController.startGame(players);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions...
        }
    }

    @FXML
    public void savePlayerSelection() {
        String playerName = PlayerName.getText();
        String selectedColorName = ObjectSelect.getValue();
        System.out.println(selectedColorName);
        // Check if playerName or selectedColorName is empty
        if (playerName.isEmpty() || selectedColorName == null) {
            showAlert("Error", "Please enter a username and select a color.");
            return; // Stop further execution
        }

        // Check if playerName is already used by another player
        for (Player existingPlayer : players) {
            if (existingPlayer.getName().equalsIgnoreCase(playerName) && existingPlayer.getPlayerNumber() != currentPlayerNumber) {
                showAlert("Error", "This username is already taken. Please choose another one.");
                return; // Stop further execution
            }
        }

        Color color = getColorFromString(selectedColorName);
        Player player;
        if (currentPlayerNumber <= players.size()) {
            // Update existing player
            player = players.get(currentPlayerNumber - 1); // -1 because list is 0-indexed
        } else {
            // Create a new player if not already existent

            Player p = playerCreator.createPlayer(playerName, currentPlayerNumber, selectedColorName);

            player = new Player(playerName, currentPlayerNumber, selectedColorName);
            players.add(player);
        }

        // Set or update the player's name, selected color, and object
        player.setName(playerName);
        player.setPlayerColor(selectedColorName);
        player.setSelectedObject(selectedColorName);
        ObjectSelect.getItems().remove(selectedColorName);

        System.out.println(player);

        currentPlayerNumber++;
        if (currentPlayerNumber <= totalPlayers) {
            updateUIForNextPlayer();
        } else {
            startGame();
        }
    }


    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        // Set the owner of the alert
        Stage primaryStage = (Stage) contentPane.getScene().getWindow();
        alert.initOwner(primaryStage);

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


}