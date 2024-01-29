package com.example.snakesandladdersviper;
import com.example.snakesandladders.Enums.TileType;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.control.Label;



public class Tile extends StackPane {
    private int number;
    private TileType type;
    private int x,y;
    private boolean occupied;
    private String name;
    private boolean visited;
    private Question question;

    public Tile(int x, int y) {
            this.x = x;
            this.y = y;
            this.occupied = false;
            this.name ="Tile"+x+y;
            this.type = TileType.Regular;
            question = null;

    }

    // Getters and setters...

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
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

