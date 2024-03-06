package com.example.snakesandladdersviper.Controller;

import com.example.snakesandladdersviper.Enums.Difficulty;
import com.example.snakesandladdersviper.Model.Player;
import com.example.snakesandladdersviper.Utils.SceneUtils;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InitializeGameBotController {
    @FXML
    private BorderPane MainPane;

    @FXML
    public ComboBox<Integer> PlayersNum;

    @FXML
    public ComboBox<Difficulty> SelectDifficulty;

    @FXML
    public Button NextButton;
    @FXML
    public Button BackButton;
    @FXML
    public TextField PlayerName;


    private List<Player> players;
    private int currentPlayerNumber;

    private Difficulty difficulty;

    public void initialize() {
        PlayersNum.getItems().clear();
        for (int i = 1; i <= 5; i++) {
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
    void BackButton(ActionEvent event) {
        // Get the current stage from the event source
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Use SceneUtils to change the scene smoothly while keeping full screen
        SceneUtils.changeScene(stage, "/com/example/snakesandladdersviper/gameMode.fxml", true);
    }


    @FXML
    public void SubmitChoices(ActionEvent event) {
        int numberOfPlayers = PlayersNum.getValue();
        difficulty = SelectDifficulty.getValue();

        // Load the player selection scene for each player

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/snakesandladdersviper/player_selectionVsBot.fxml"));
            Parent root = loader.load();


            // Get the controller for the player selection scene (PlayerSelectionController)
            PlayerSelectionVsBotController playerSelectionController = loader.getController();

            // Create a new Player instance for this player
            Player player = new Player("Player " + currentPlayerNumber);
            player.setPlayerNumber(currentPlayerNumber);
            players.add(player);
            playerSelectionController.setNumOfBots(numberOfPlayers);
            playerSelectionController.setDifficulty(difficulty);
            // Pass the player number and player instance to the controller
            playerSelectionController.setPlayerData(currentPlayerNumber, player, 1, numberOfPlayers);

            // Create a new scene and set it on a new stage for each player
            MainPane.getChildren().clear();
            MainPane.setCenter(root);

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