package com.example.snakesandladdersviper;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Snakes and Ladders");
        stage.setScene(scene);

        // Show the stage first
        stage.show();

        // Then set full-screen mode
        stage.setFullScreen(true);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
