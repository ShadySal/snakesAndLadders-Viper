package com.example.snakesandladdersviper.test;

import com.example.snakesandladdersviper.Controller.InitializeGame;
import com.example.snakesandladdersviper.Enums.Difficulty;
import com.example.snakesandladdersviper.Model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InitializeGameTest {

    private InitializeGame controller;

    @BeforeEach
    void setUp() {
        controller = new InitializeGame();
        controller.initialize();
    }

    @Test
    void testInitialization() {
        assertNotNull(controller.getPlayersNum(), "PlayersNum ComboBox should not be null");
        assertEquals(2, controller.getPlayersNum().getValue(), "Default number of players should be 2");
        assertNotNull(controller.getSelectDifficulty(), "SelectDifficulty ComboBox should not be null");
        assertEquals(Difficulty.EASY, controller.getSelectDifficulty().getValue(), "Default difficulty should be EASY");
    }

    @Test
    void testPlayerCreation() {
        controller.SubmitChoices(null); // Simulate button click
        assertFalse(controller.getPlayers().isEmpty(), "Players list should not be empty after SubmitChoices");
        assertEquals(1, controller.getPlayers().size(), "There should be one player in the list");
        Player player = controller.getPlayers().get(0);
        assertNotNull(player, "Player should not be null");
        assertEquals("Player 1", player.getName(), "Player name should be set correctly");
    }
}
