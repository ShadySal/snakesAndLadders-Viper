package com.example.snakesandladdersviper.test;




import com.example.snakesandladdersviper.Model.GameBoard;
import com.example.snakesandladdersviper.Enums.Difficulty;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class GameBoardTest {

    @Test
    void testGameBoardInitialization() {
        GameBoard gameBoard = new GameBoard(7, 7); // Assuming a constructor with width and height
        Assertions.assertNotNull(gameBoard, "GameBoard should be initialized");
    }

    @Test
    void testGameBoardDifficulty() {
        GameBoard gameBoard = new GameBoard(10, 10); // Example for medium difficulty
        gameBoard.setDifficulty(Difficulty.MEDIUM);
        Assertions.assertEquals(Difficulty.MEDIUM, gameBoard.getDifficulty(), "Difficulty should be set to MEDIUM");
    }


}