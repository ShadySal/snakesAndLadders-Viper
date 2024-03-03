package com.example.snakesandladdersviper.Controller;

import com.example.snakesandladdersviper.Model.Ladder;
import com.example.snakesandladdersviper.Model.Player;
import com.example.snakesandladdersviper.Model.Snake;

public class GameElementFactory {
    public static Player createPlayer(String name, int playerNumber, String color) {
        // Logic to create a new player
        return new Player(name, playerNumber, color);
    }

    public static Snake createSnake(int start, int end, String type) {
        // Logic to create a new snake
        return new Snake(start, end, type);
    }

    public static Ladder createLadder(int start, int end, int length) {
        // Logic to create a new ladder
        return new Ladder(start, end, length);
    }

}
