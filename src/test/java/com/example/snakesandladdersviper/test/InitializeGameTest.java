

package com.example.snakesandladdersviper.test;

import static org.junit.Assert.*;

import com.example.snakesandladdersviper.Controller.InitializeGame;
import com.example.snakesandladdersviper.Enums.Difficulty;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.Test;
import javafx.scene.control.ComboBox;


public class InitializeGameTest {
    static {
        new JFXPanel();
    }

    private InitializeGame createControllerAndInitialize() {
        // Create controller instance
        InitializeGame controller = new InitializeGame();

        // Initialize JavaFX components manually
        Platform.runLater(() -> {
            controller.PlayersNum = new ComboBox<>();
            controller.SelectDifficulty = new ComboBox<>();
            controller.NextButton = new Button();
            controller.BackButton = new Button();
            controller.PlayerName = new TextField();

            // Other initialization logic...
            controller.initialize();
        });

        // Wait for the initialization to complete
        waitForRunLater();

        return controller;
    }

    private void waitForRunLater() {
        try {
            Thread.sleep(500); // Adjust the sleep time as necessary
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Test
    void testDifficultySetCorrectlyAfterSelection() {
        InitializeGame controller = createControllerAndInitialize();
        Platform.runLater(() -> controller.SelectDifficulty.setValue(Difficulty.MEDIUM));
        waitForRunLater();
        assertEquals("The selected difficulty should be set correctly", Difficulty.MEDIUM, controller.SelectDifficulty.getValue());
    }
}
