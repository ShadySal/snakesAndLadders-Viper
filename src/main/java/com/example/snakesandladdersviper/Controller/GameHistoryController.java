package com.example.snakesandladdersviper.Controller;

import com.example.snakesandladdersviper.Model.GameHistory;
import com.example.snakesandladdersviper.Model.SysData;
import com.example.snakesandladdersviper.Utils.SceneUtils;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
    void BackButton(ActionEvent event) {
        Stage stage = (Stage) BackButton.getScene().getWindow();

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.initOwner(stage);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText("Are you sure you want to back to the Main Menu?");
        confirmationAlert.setContentText("");
        confirmationAlert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        // Show the alert and wait for user input
        confirmationAlert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                SceneUtils.changeScene(stage, "/com/example/snakesandladdersviper/hello-view.fxml", true);
            }
        });
    }

    public void initialize() {
        // Set up the columns to use the property getters from the GameHistory class
        winnerColumn.setCellValueFactory(new PropertyValueFactory<>("winner"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        difficultyColumn.setCellValueFactory(new PropertyValueFactory<>("difficulty"));

        // Fetch the game history data and populate the table
        List<GameHistory> historyList = SysData.getInstance().getHistory();
        historyTable.setItems(FXCollections.observableArrayList(historyList));
    }
}
