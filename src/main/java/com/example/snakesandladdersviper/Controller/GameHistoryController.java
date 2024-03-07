package com.example.snakesandladdersviper.Controller;

import com.example.snakesandladdersviper.Model.GameHistory;
import com.example.snakesandladdersviper.Model.SysData;
import com.example.snakesandladdersviper.Utils.SceneUtils;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;


public class GameHistoryController {

    @FXML
    private TableView<GameHistory> historyTable;

    @FXML
    private TableColumn<GameHistory, String> winnerColumn;

    @FXML
    private TableColumn<GameHistory, String> durationColumn;

    @FXML
    private TableColumn<GameHistory, String> difficultyColumn;

    @FXML
    public Button BackButton;


    @FXML
    private TableView<GameHistory> gameHistoryTable;


    @FXML
    public void initialize() {
        // Bind the table columns to the properties of the GameHistory class
        winnerColumn.setCellValueFactory(new PropertyValueFactory<>("winnerName"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        difficultyColumn.setCellValueFactory(new PropertyValueFactory<>("difficulty"));

        // Load the game history data into the table
        loadGameHistoryData();
    }

    private void loadGameHistoryData() {
        // Assuming SysData.getHistory() returns a List<GameHistory>
        List<GameHistory> historyList = SysData.getInstance().getHistory();
        gameHistoryTable.setItems(FXCollections.observableArrayList(historyList));
    }

    @FXML
    void BackButton(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneUtils.changeScene(currentStage, "/com/example/snakesandladdersviper/hello-view.fxml", true);
    }

}
