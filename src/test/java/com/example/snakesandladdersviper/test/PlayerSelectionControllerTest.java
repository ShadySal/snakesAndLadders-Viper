package com.example.snakesandladdersviper.test;

import com.example.snakesandladdersviper.Controller.PlayerSelectionController;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerSelectionControllerTest {

    private void initializeJavaFX() {
        // Ensures JavaFX runtime is initialized
        new JFXPanel();
    }

    private PlayerSelectionController createAndInitializeController() {
        PlayerSelectionController controller = new PlayerSelectionController();
        controller.PlayerName = new TextField();
        controller.ObjectSelect = new ComboBox<>();
        controller.PlayerSelectionTurn = new Text();
        controller.initialize();
        return controller;
    }

    @Test
    public void testPlayerNameFieldIsInitialized() {
        initializeJavaFX();
        PlayerSelectionController controller = createAndInitializeController();
        assertNotNull(controller.getPlayerName(), "PlayerName TextField should not be null");
    }

    @Test
    public void testObjectSelectComboBoxIsInitialized() {
        initializeJavaFX();
        PlayerSelectionController controller = createAndInitializeController();
        assertNotNull(controller.getObjectSelect(), "ObjectSelect ComboBox should not be null");
        assertTrue(controller.getObjectSelect().getItems().contains("Red"), "ObjectSelect should contain 'Red'");
    }

    @Test
    public void testPlayerSelectionTurnIsInitialized() {
        initializeJavaFX();
        PlayerSelectionController controller = createAndInitializeController();
        assertNotNull(controller.getPlayerSelectionTurn(), "PlayerSelectionTurn Text should not be null");
        assertEquals("Player 1", controller.getPlayerSelectionTurn().getText(), "PlayerSelectionTurn should display 'Player 1'");
    }


}
