package com.example.snakesandladdersviper.Controller;
import com.example.snakesandladdersviper.Enums.Difficulty;
import com.example.snakesandladdersviper.Model.Dice;
import com.example.snakesandladdersviper.Model.GameBoard;
import com.example.snakesandladdersviper.Model.Player;
import com.example.snakesandladdersviper.Controller.GameBoardController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class PlayerSelectionController {

    @FXML
    private Pane contentPane;

    @FXML
    private Text PlayerSelectionTurn;

    @FXML
    private TextField PlayerName;

    @FXML
    private ComboBox<String> ObjectSelect;

    private int currentPlayerNumber;
    private int totalPlayers;
    private List<Player> players;
    @FXML
    private Button BackButton;
    private Difficulty difficulty;
    final double EASY_GAME_QUESTION_PROBABILITY = 0.1; // 10% chance for a question
    final double MEDIUM_GAME_QUESTION_PROBABILITY = EASY_GAME_QUESTION_PROBABILITY * 2; // 20% chance
    private GameBoard gameBoard;


    public void initialize() {
        ObjectSelect.getItems().addAll("Red", "Blue", "Green", "Yellow", "Orange", "White");
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

//    @FXML
//    void BackButton(ActionEvent event) throws IOException {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/snakesandladdersviper/InitializeGame.fxml"));
//            Parent root = loader.load();
//            Scene nextScene = new Scene(root);
//// Get the current stage and set the new scene
//            Stage currentStage = (Stage) BackButton.getScene().getWindow();
//            currentStage.setScene(nextScene);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
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
            gameBoardController.initializeBoard(difficulty, players);

            // Replace contentPane content with the game board
            contentPane.getChildren().setAll(gameBoardRoot);

            // Initialize the game logic
            gameBoardController.startGame(players);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions...
        }
    }

    @FXML
    private void savePlayerSelection() {
        String playerName = PlayerName.getText();
        String selectedColorName = ObjectSelect.getValue();
        Color color = getColorFromString(selectedColorName);

        Player player;
        if (currentPlayerNumber <= players.size()) {
            // Update existing player
            player = players.get(currentPlayerNumber - 1); // -1 because list is 0-indexed
        } else {
            // Create a new player if not already existent
            player = new Player(playerName, currentPlayerNumber);
            players.add(player);
        }

        // Set or update the player's name, selected color, and object
        player.setName(playerName);
        player.setPlayerColor(color);
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
