package com.example.snakesandladdersviper.Model;

import com.example.snakesandladdersviper.Enums.GameEventType;

public class GameEvent {
    private GameEventType eventType;
    private Player player;
    private int newPosition;

    private int tilePosition;
    private String tileType;
    private String additionalInfo;

    public GameEvent(GameEventType eventType, Player player, int newPosition, String additionalInfo) {
        this.eventType = eventType;
        this.player = player;
        this.newPosition = newPosition;
        this.additionalInfo = additionalInfo;
    }

    public GameEvent(Player currentPlayer) {
    }

    // Getters
    public GameEventType getEventType() {
        return eventType;
    }

    public Player getPlayer() {
        return player;
    }

    public int getNewPosition() {
        return newPosition;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }
    public void setEventType(GameEventType eventType) {
        this.eventType = eventType;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setNewPosition(int newPosition) {
        this.newPosition = newPosition;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
    public int getTilePosition() {
        return tilePosition;
    }

    public String getTileType() {
        return tileType;
    }
    
}
