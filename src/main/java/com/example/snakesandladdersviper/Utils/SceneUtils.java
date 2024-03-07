package com.example.snakesandladdersviper.Utils;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneUtils {

    // Updated to change the root of the current scene
    public static void changeScene(Stage stage, String fxmlPath, boolean keepFullScreen) {
        Platform.runLater(() -> {
            try {
                Parent root = FXMLLoader.load(SceneUtils.class.getResource(fxmlPath));
                if (stage.getScene() != null) {
                    stage.getScene().setRoot(root);
                } else {
                    stage.setScene(new Scene(root));
                }
                stage.setFullScreen(keepFullScreen);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error loading scene", "Failed to load the requested scene.", stage, keepFullScreen);
            }
        });
    }

    // Adjusted to include the keepFullScreen parameter for consistency
    public static void showAlert(String title, String content, Stage stage, boolean keepFullScreen) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.initOwner(stage);
            alert.showAndWait();

            // Reapply full-screen mode if necessary, based on the keepFullScreen parameter
            if (keepFullScreen) {
                stage.setFullScreen(true);
            }
        });
    }
}
