package com.example.snakesandladdersviper.Controller;

import com.example.snakesandladdersviper.Enums.Difficulty;
import com.example.snakesandladdersviper.Model.GameBoard;
import com.example.snakesandladdersviper.Model.Player;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class InitializeGame {
    @FXML
    private Pane MainPane;

    @FXML
    private ComboBox<Integer> PlayersNum;

    @FXML
    private ComboBox<Difficulty> SelectDifficulty;

    @FXML
    private Button NextButton;
    @FXML
    private Button BackButton;
    @FXML
    private TextField PlayerName;


    private List<Player> players;
    private int currentPlayerNumber;

    private Difficulty difficulty;

    public void initialize() {
        PlayersNum.getItems().clear();
        for (int i = 2; i <= 6; i++) {
            PlayersNum.getItems().add(i);
        }
        PlayersNum.setValue(2);
        SelectDifficulty.setItems(FXCollections.observableArrayList(Difficulty.values()));
        SelectDifficulty.setValue(Difficulty.EASY);
        players = new ArrayList<>();
        currentPlayerNumber = 1;
    }

    //doesnt work
    @FXML
    void BackButton(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/snakesandladdersviper/hello-view.fxml"));
            Parent root = loader.load();
            Scene nextScene = new Scene(root);
// Get the current stage and set the new scene
            Stage currentStage = (Stage) BackButton.getScene().getWindow();
            currentStage.setScene(nextScene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void SubmitChoices(ActionEvent event) {
        int numberOfPlayers = PlayersNum.getValue();
        difficulty = SelectDifficulty.getValue();

        // Load the player selection scene for each player

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/snakesandladdersviper/player_selection.fxml"));
            Parent root = loader.load();

            // Get the controller for the player selection scene (PlayerSelectionController)
            PlayerSelectionController playerSelectionController = loader.getController();

            // Create a new Player instance for this player
            Player player = new Player("Player " + currentPlayerNumber);
            player.setPlayerNumber(currentPlayerNumber);
            players.add(player);
            playerSelectionController.setDifficulty(difficulty);
            // Pass the player number and player instance to the controller
            playerSelectionController.setPlayerData(currentPlayerNumber, player, 1, numberOfPlayers);

            // Create a new scene and set it on a new stage for each player
            MainPane.getChildren().clear();
            MainPane.getChildren().add(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public ComboBox<Integer> getPlayersNum() {
        return PlayersNum;
    }

    public ComboBox<Difficulty> getSelectDifficulty() {
        return SelectDifficulty;
    }
    public List<Player> getPlayers() {
        return players;
    }
}