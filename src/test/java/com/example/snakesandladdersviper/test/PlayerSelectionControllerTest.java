package com.example.snakesandladdersviper.test;
import com.example.snakesandladdersviper.Model.Player;
import com.example.snakesandladdersviper.Controller.PlayerSelectionController;
import com.example.snakesandladdersviper.Enums.Difficulty;
import javafx.scene.control.ComboBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerSelectionControllerTest {

    private PlayerSelectionController controller;

    @BeforeEach
    void setUp() {
        controller = new PlayerSelectionController();
        controller.initialize();
    }

    @Test
    void testInitialization() {
        assertNotNull(controller.getObjectSelect(), "ObjectSelect ComboBox should not be null");
        assertEquals(6, controller.getObjectSelect().getItems().size(), "ObjectSelect should have 6 items");
        assertNotNull(controller.getPlayerName(), "PlayerName TextField should not be null");
        assertNotNull(controller.getPlayerSelectionTurn(), "PlayerSelectionTurn Text should not be null");
    }

    @Test
    void testPlayerCreation() {
        // Simulate user interactions to fill in player information
        controller.getPlayerName().setText("TestPlayer");
        controller.getObjectSelect().getSelectionModel().select("Red");

        controller.savePlayerSelection(); // Simulate button click

        assertFalse(controller.getPlayers().isEmpty(), "Players list should not be empty after savePlayerSelection");
        assertEquals(1, controller.getPlayers().size(), "There should be one player in the list");
        Player player = controller.getPlayers().get(0);
        assertNotNull(player, "Player should not be null");
        assertEquals("TestPlayer", player.getName(), "Player name should be set correctly");
    }
}
