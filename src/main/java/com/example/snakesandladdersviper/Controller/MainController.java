package com.example.snakesandladdersviper.Controller;

import com.example.snakesandladdersviper.Utils.SceneUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import java.io.IOException;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.animation.ScaleTransition;
import javafx.scene.text.Font;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;


public class MainController implements Initializable{
    @FXML
    private BorderPane MainPane;

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
        animateButtonSequentially(StartGameBtn, 0.5, 0.5);

    }
    private void animateButtonSequentially(Button button, double initialDelay, double delayIncrement) {
        double delay = initialDelay;

        for (Button btn : new Button[]{StartGameBtn, GameHistoryBtn, QuestionManagementBtn, InstructionsBtn}) {
            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1.5), btn);
            fadeTransition.setFromValue(0);
            fadeTransition.setToValue(1);
            fadeTransition.setDelay(Duration.seconds(delay));

            TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(1.5), btn);
            translateTransition.setFromX(100);
            translateTransition.setToX(0);
            translateTransition.setDelay(Duration.seconds(delay));

            fadeTransition.play();
            translateTransition.play();

            delay += delayIncrement; // Increment the delay for the next button
        }
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



    public void StartGame(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneUtils.changeScene(stage, "/com/example/snakesandladdersviper/gameMode.fxml", true);
    }

    public void showGameHistory(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneUtils.changeScene(stage, "/com/example/snakesandladdersviper/GameHistory.fxml", true);
    }

    public void InstructionsOfGame(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneUtils.changeScene(stage, "/com/example/snakesandladdersviper/Instructions.fxml", true);
    }


    public void showError(String errorMessage, ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneUtils.showAlert("Error", errorMessage, stage,true);
    }



    @FXML
    void BackButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneUtils.changeScene(stage, "/com/example/snakesandladdersviper/hello-view.fxml", true);
    }



    // Assuming this is inside MainController or similar
    @FXML
    private void onQuestionManagementBtnClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/snakesandladdersviper/AdminLogin.fxml"));
        Parent root = loader.load();
        AdminLoginController loginController = loader.getController();

        Stage loginStage = new Stage();
        loginStage.initModality(Modality.APPLICATION_MODAL); // Ensure dialog appears on top without affecting fullscreen mode
        loginStage.initOwner(((Node)event.getSource()).getScene().getWindow()); // Set owner to current stage
        loginStage.setScene(new Scene(root));

        // Optionally set properties to ensure consistent appearance with fullscreen mode

        loginStage.showAndWait(); // Wait until the login window is closed

        if (loginController.isLoggedIn()) {
            // Ensure we're running changes on the JavaFX thread and maintaining fullscreen mode
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            SceneUtils.changeScene(currentStage, "/com/example/snakesandladdersviper/QuestionsPage.fxml", true);
        }
    }




    public void showAlert(String title, String errorMessage, ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneUtils.showAlert(title, errorMessage, stage,true);
    }



}
