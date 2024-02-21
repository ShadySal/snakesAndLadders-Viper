package com.example.snakesandladdersviper.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class gameModeController {
    @FXML
    private Button PlayVsBotBtn;

    @FXML
    private Button PlayWithFriendsBtn;
    @FXML
    void onPlayVsBotClicked(ActionEvent event) {
        try {
            // Load the FXML file for playing against a bot
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/snakesandladdersviper/InitializeGameBot.fxml"));
            Parent root = loader.load();

            // Get the current stage from the event source
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene on the current stage
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception as appropriate
        }
    }

    @FXML
    void onPlayWithFriendsClicked(ActionEvent event) {
        try {
            // Load the FXML file for playing with friends
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/snakesandladdersviper/InitializeGame.fxml"));
            Parent root = loader.load();

            // Get the current stage from the event source
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Initialize Game");
            // Set the new scene on the current stage
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception as appropriate
        }
    }

}
