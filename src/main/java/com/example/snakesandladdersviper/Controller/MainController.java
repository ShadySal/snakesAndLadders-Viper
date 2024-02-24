package com.example.snakesandladdersviper.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.ScaleTransition;
import javafx.scene.text.Font;
import javafx.animation.ScaleTransition;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.animation.ScaleTransition;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;


public class MainController implements Initializable{

    @FXML
    private VBox vboxContainer;

    @FXML
    private Text titleLabel;

    @FXML
    private Button GameHistoryBtn;

    @FXML
    private Button QuestionManagementBtn;

    @FXML
    private Button StartGameBtn;

    @FXML
    private Button InstructionsBtn;

    @FXML
    private Button BackButton;

    public void initialize(URL location, ResourceBundle resources) {
        customizeTitle();
        animateTitle();
        animateButton(StartGameBtn, 0.1);
        animateButton(GameHistoryBtn, 0.2);
        animateButton(QuestionManagementBtn, 0.3);
        animateButton(InstructionsBtn, 0.4);
    }
    private void customizeTitle() {
        // Set a larger font size for the title
        titleLabel.setFont(new Font("Arial Bold", 60));

        // Apply a drop shadow effect to make the title stand out
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.color(0.4, 0.5, 0.5));

        titleLabel.setEffect(dropShadow);

        // Change the fill to a new gradient or color if desired
        titleLabel.setFill(new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.DARKBLUE),
                new Stop(1, Color.DARKGREEN)));
    }
    private void animateTitle() {
        // Scale in title for a bounce effect
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(1), titleLabel); // Slower scale transition
        scaleTransition.setFromX(0.5);
        scaleTransition.setFromY(0.5);
        scaleTransition.setToX(1.0);
        scaleTransition.setToY(1.0);
        scaleTransition.setCycleCount(2);
        scaleTransition.setAutoReverse(true);

        // Slide in title from the top
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(1.5), titleLabel); // Slower slide transition
        translateTransition.setFromY(-50);
        translateTransition.setToY(0);

        // Play transitions sequentially or at the same time depending on desired effect
        scaleTransition.play();
        translateTransition.play();
    }

    private void animateButton(Button button, double delay) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1.5), button); // Slower fade transition
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.setDelay(Duration.seconds(delay));

        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(1.5), button); // Slower translate transition
        translateTransition.setFromX(100);
        translateTransition.setToX(0);
        translateTransition.setDelay(Duration.seconds(delay));

        // Play transitions sequentially or at the same time depending on desired effect
        fadeTransition.play();
        translateTransition.play();
    }




    public void StartGame() throws IOException {
        Stage currentStage = (Stage) StartGameBtn.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/snakesandladdersviper/gameMode.fxml"));
        currentStage.setTitle("Select Game Mode");
        currentStage.setScene(new Scene(root, 800, 800));

        // Set full-screen mode
        currentStage.setFullScreen(true);

        // Show the stage
        currentStage.show();
    }


    public void InstructionsOfGame(ActionEvent event) throws IOException {
        Parent InstructionPage = FXMLLoader.load(getClass().getResource("/com/example/snakesandladdersviper/Instructions.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(InstructionPage));

        // Set full-screen mode for the instruction page
        stage.setFullScreen(true);

        // Show the instruction page
        stage.show();
    }


    @FXML
    void BackButton(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/snakesandladdersviper/hello-view.fxml"));
            Parent root = loader.load();
            Scene nextScene = new Scene(root);
// Get the current stage and set the new scene
            Stage currentStage = (Stage) BackButton.getScene().getWindow();
            currentStage.setScene(nextScene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void onQuestionManagementBtnClicked(ActionEvent event) throws IOException {
        // Load AdminLogin.fxml
        FXMLLoader adminLoader = new FXMLLoader(getClass().getResource("/com/example/snakesandladdersviper/AdminLogin.fxml"));
        Parent adminLoginParent = adminLoader.load();

        // Get the controller to set up any necessary communication
        AdminLoginController adminLoginController = adminLoader.getController();

        // Create a new stage for the admin login dialog
        Stage adminLoginStage = new Stage();
        adminLoginStage.setScene(new Scene(adminLoginParent));
        adminLoginStage.setTitle("Admin Login");
        adminLoginStage.showAndWait(); // Show the login dialog and wait for it to close

        // After login dialog is closed, check if the admin login was successful
        if (adminLoginController.isLoggedIn()) {
            // Admin successfully logged in, proceed to question management
            Parent questionsPage = FXMLLoader.load(getClass().getResource("/com/example/snakesandladdersviper/QuestionsPage.fxml"));
            Stage questionStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            questionStage.setScene(new Scene(questionsPage));

            // Set full-screen mode for the question management page
            questionStage.setFullScreen(true);

            // Show the question management page
            questionStage.show();
        } else {
            // Admin login unsuccessful, show an error message to the user
            showAlert("Access Denied", "You must log in as an admin to access this feature.");
        }
    }


    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }



}
