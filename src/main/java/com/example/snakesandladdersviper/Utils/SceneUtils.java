package com.example.snakesandladdersviper.Utils;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneUtils {

    public static void changeScene(Stage stage, String fxmlPath, boolean fullScreen) {
        Platform.runLater(() -> {
            try {
                Parent root = FXMLLoader.load(SceneUtils.class.getResource(fxmlPath));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setFullScreen(fullScreen);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error loading scene", "Failed to load the requested scene.", stage);
            }
        });
    }

    public static void showAlert(String title, String content, Stage stage) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.initOwner(stage);
            alert.showAndWait();
            if (!stage.isFullScreen()) {
                stage.setFullScreen(true);
            }
        });
    }
}
