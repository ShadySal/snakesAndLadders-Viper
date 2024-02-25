package com.example.snakesandladdersviper;

import com.example.snakesandladdersviper.Controller.SceneController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private Stage primaryStage; // Add a class member to hold the primary stage

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Create an instance of your SceneController, passing the primaryStage to its constructor
        SceneController sceneController = new SceneController(primaryStage);

        // Now, you can use sceneController to switch scenes as needed
        // For the initial scene, you might want to do something like this:
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setTitle("Snakes and Ladders");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    public void switchScene(String fxmlFile) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
        try {
            Scene newScene = new Scene(fxmlLoader.load());
            primaryStage.setScene(newScene);
            // Fullscreen is maintained, as per the listener in the start method
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
