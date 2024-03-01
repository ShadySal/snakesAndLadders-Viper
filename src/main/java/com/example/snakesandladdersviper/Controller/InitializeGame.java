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
import javafx.scene.layout.BorderPane;
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
        for (int i = 2; i <= 6; i++) {
            PlayersNum.getItems().add(i);
        }
        PlayersNum.setValue(2);

        SelectDifficulty.setItems(FXCollections.observableArrayList(Difficulty.values()));
        SelectDifficulty.setValue(Difficulty.EASY);

        players = new ArrayList<>();

    }

    //doesnt work

    @FXML
    void BackButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/snakesandladdersviper/gameMode.fxml"));
            Parent root = loader.load();
            Scene nextScene = new Scene(root);
            Stage currentStage = (Stage) BackButton.getScene().getWindow();
            currentStage.setScene(nextScene);
            currentStage.setFullScreen(true); // Keep full screen
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void SubmitChoices(ActionEvent event) {
        int numberOfPlayers = PlayersNum.getValue();
        difficulty = SelectDifficulty.getValue();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/snakesandladdersviper/player_selection.fxml"));
            Parent root = loader.load();
            PlayerSelectionController playerSelectionController = loader.getController();
            Player player = new Player("Player " + currentPlayerNumber);
            player.setPlayerNumber(currentPlayerNumber);
            players.add(player);
            playerSelectionController.setDifficulty(difficulty);
            playerSelectionController.setPlayerData(currentPlayerNumber, player, 1, numberOfPlayers);
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