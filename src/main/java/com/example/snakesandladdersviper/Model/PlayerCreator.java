package com.example.snakesandladdersviper.Model;

public abstract class PlayerCreator {
    public Player createPlayer(String name, int playerNumber, String color) {
        Player player = instantiatePlayer(name, playerNumber, color);
        customizePlayer(player);
        return player;
    }

    // This method is implemented by subclasses to provide different types of players.
    protected abstract Player instantiatePlayer(String name, int playerNumber, String color);

    // This method can be overridden by subclasses to add additional customization.
    protected void customizePlayer(Player player) {
        // Default implementation
    }
    public static class DefaultPlayerCreator extends PlayerCreator {
        @Override
        protected Player instantiatePlayer(String name, int playerNumber, String color) {
            return new Player(name, playerNumber, color);
        }
    }
}

