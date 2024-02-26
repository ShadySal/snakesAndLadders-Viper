package com.example.snakesandladdersviper.Model;

public interface GameSubject {
    void registerObserver(GameObserver observer);
    void removeObserver(GameObserver observer);
    void notifyObservers(GameEvent event);
}
