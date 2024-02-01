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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

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

    private Difficulty difficulty;
    final double EASY_GAME_QUESTION_PROBABILITY = 0.1; // 10% chance for a question
    final double MEDIUM_GAME_QUESTION_PROBABILITY = EASY_GAME_QUESTION_PROBABILITY * 2; // 20% chance
    private GameBoard gameBoard;


    public void initialize() {
        ObjectSelect.getItems().addAll("Object 1", "Object 2", "Object 3", "Object 4", "Object 5", "Object 6");
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
        String selectedObject = ObjectSelect.getValue();

        if (currentPlayerNumber <= totalPlayers) {
            Player player = new Player(playerName, currentPlayerNumber);
            players.add(player);

            player.setName(playerName);
            player.setSelectedObject(selectedObject);
            ObjectSelect.getItems().remove(selectedObject);

            System.out.println(player);

            currentPlayerNumber++;
            if (currentPlayerNumber <= totalPlayers) {
                updateUIForNextPlayer();
            } else {
                startGame();
            }
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
