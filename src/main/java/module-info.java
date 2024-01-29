module com.example.snakesandladdersviper {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;


    opens com.example.snakesandladdersviper.Controller to javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires org.json;

    opens com.example.snakesandladdersviper to javafx.fxml;
    exports com.example.snakesandladdersviper;
    exports com.example.snakesandladdersviper.Model;
    opens com.example.snakesandladdersviper.Model to javafx.fxml;
}