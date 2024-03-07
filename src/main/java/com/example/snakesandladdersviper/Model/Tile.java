package com.example.snakesandladdersviper.Model;
import com.example.snakesandladdersviper.Enums.TileType;
import javafx.geometry.Bounds;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class Tile extends Rectangle {
    private int number;
    private TileType type;
    private int x,y;
    private boolean occupied;
    private String name;
    private boolean visited;
    private Question question;
    private String color;
    private Bounds boundsInScene;
    private Bounds boundsInScreen;


    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        this.occupied = false;
        this.name ="Tile"+x+y;
        this.type = TileType.Regular;
        question = null;

    }

    public Bounds getBoundsInScene() {
        return boundsInScene;
    }

    public void setBoundsInScene(Bounds boundsInScene) {
        this.boundsInScene = boundsInScene;
    }

    public Bounds getBoundsInScreen() {
        return boundsInScreen;
    }

    public void setBoundsInScreen(Bounds boundsInScreen) {
        this.boundsInScreen = boundsInScreen;
    }

    public void setNumber(int number){
        this.number = number;
}
public int getNumber(){
        return this.number;
}
    // Getters and setters...

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }


    public void setX(int x) {
        this.x = x;
    }



    public void setY(int y) {
        this.y = y;
    }

    public boolean isOccupied() {
        return occupied;
    }
    public boolean isVisited() {
        return visited;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }
    public void setVisited(boolean visited) {
        this.visited = visited;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public TileType getType() {
        return type;
    }

    public void setType(TileType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tile tile = (Tile) o;

        return name.equals(tile.name);
    }
    @Override
    public String toString() {
        return "Tile{" +
                "number=" + number +
                ", type=" + type +
                ", x=" + x +
                ", y=" + y +
                ", occupied=" + occupied +
                ", name='" + name + '\'' +
                ", visited=" + visited +
                ", question=" + question +
                '}';
    }

//    @Override
//    public int hashCode() {
//        return name.hashCode();
//    }

//    @Override
//    public String toString() {
//        String status;
//        if(this.occupied) status = "Occupied";
//        else status = "Not occupied";
////        return "Tile" + this.x + this.y + " - " + status;
//        return "Tile";
//    }
//    public QuestionLevel getLevel() {
//        return level;
//    }
}