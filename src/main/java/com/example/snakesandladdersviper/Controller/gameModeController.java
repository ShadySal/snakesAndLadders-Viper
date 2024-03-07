package com.example.snakesandladdersviper.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import com.example.snakesandladdersviper.Utils.SceneUtils; // Make sure to import SceneUtils

public class gameModeController {
    @FXML
    private Button PlayVsBotBtn;

    @FXML
    private Button PlayWithFriendsBtn;

    @FXML
    private StackPane MainPane;

    @FXML
    public Button BackButton;

    @FXML
    void handleBackButtonAction(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneUtils.changeScene(currentStage, "/com/example/snakesandladdersviper/hello-view.fxml", true);
    }

    @FXML
    void onPlayVsBotClicked(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneUtils.changeScene(currentStage, "/com/example/snakesandladdersviper/InitializeGameBot.fxml", true);
    }

    @FXML
    void onPlayWithFriendsClicked(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneUtils.changeScene(currentStage, "/com/example/snakesandladdersviper/InitializeGame.fxml", true);
    }
}
