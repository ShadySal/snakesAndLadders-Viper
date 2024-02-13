package com.example.snakesandladdersviper.Controller;

import com.example.snakesandladdersviper.Model.Dice;
import com.example.snakesandladdersviper.Model.GameBoard;
import com.example.snakesandladdersviper.Model.Player;
import com.example.snakesandladdersviper.Model.Tile;
import com.example.snakesandladdersviper.Enums.Difficulty;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;


public class GameBoardController {

    @FXML
    private GridPane BoardGrid;

    @FXML
    private Pane contentPane;

    @FXML
    private Pane gamepane;

    @FXML
    private Pane gameDataPane;

    @FXML
    private Label timeLabel;

    @FXML
    private Label LevelLabel;
    @FXML
    private Button MainMenuButton;

    private Timeline timeline;
    private long startTime;
    // Example values for probabilities
    final double EASY_GAME_QUESTION_PROBABILITY = 0.1; // 10% chance for a question
    final double MEDIUM_GAME_QUESTION_PROBABILITY = EASY_GAME_QUESTION_PROBABILITY * 2; // 20% chance
    @FXML
    private Box dice;
    private GameBoard gameBoard;
    private Difficulty difficulty;

    @FXML
    private Button diceRollButton;

    private int diceRollValue = 0;
    private Timeline diceRollAnimation;
    @FXML
    private Label currentPlayerLabel;

    private int currentPlayerIndex = 0;
    private List<Player> players;

    public void initialize() {
        startTime = System.currentTimeMillis();
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateClock()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        setupDiceRollAnimation();


    }


    //dice animation
    private void setupDiceRollAnimation() {
        diceRollAnimation = new Timeline(new KeyFrame(Duration.millis(100), e -> {
            String diceOutcome = generateRandomNumber(difficulty);

            // Check if the outcome is a number or a question
            try {
                // If it's a number, parse it and update the button text
                diceRollValue = Integer.parseInt(diceOutcome);
                diceRollButton.setText(String.valueOf(diceRollValue));
            } catch (NumberFormatException ex) {
                // If it's not a number, it must be a question or another string outcome
                // Update the button text with the string outcome (like "EASY_QUESTION")
                diceRollButton.setText(diceOutcome);
            }
        }));
        diceRollAnimation.setCycleCount(10); // Adjust the cycle count to control the speed of the animation
    }
    //number generation
    private String generateRandomNumber(Difficulty difficulty) {
        Random random = new Random();
        int rollResult;

        if (difficulty == Difficulty.EASY) {
            int maxRoll = 7; // 0-4 for numbers, 5 for easy question, 6 for medium question, 7 for hard question
            rollResult = random.nextInt(maxRoll + 1);

            switch (rollResult) {
                case 5: return "EASY_QUESTION";
                case 6: return "MEDIUM_QUESTION";
                case 7: return "HARD_QUESTION";
                default: return String.valueOf(rollResult);
            }
        } else if (difficulty == Difficulty.MEDIUM) {
            // Logic for medium difficulty dice roll
            // Adjust the logic as per your game rules
        } else if (difficulty == Difficulty.HARD) {
            // Logic for hard difficulty dice roll
            // Adjust the logic as per your game rules
        }

        // Default return statement if none of the above conditions are met
        return "Invalid difficulty";
        // Other difficulty cases...
    }

    private void updateClock() {
        long now = System.currentTimeMillis();
        long elapsedMillis = now - startTime;
        int elapsedSeconds = (int) (elapsedMillis / 1000);
        int seconds = elapsedSeconds % 60;
        int minutes = (elapsedSeconds / 60) % 60;
        int hours = elapsedSeconds / 3600;

        timeLabel.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
    }

    public void initializeBoard(Difficulty difficulty, List<Player> players) {
        int size = determineBoardSize(difficulty);

        gameBoard = new GameBoard(size, size);
        if (size == 7) {
            Dice easyGameDice = new Dice(6, EASY_GAME_QUESTION_PROBABILITY);
            this.difficulty = difficulty;

            gameBoard.setDice(easyGameDice);

        } else if (size == 10) {
            Dice mediumGameDice = new Dice(9, MEDIUM_GAME_QUESTION_PROBABILITY); // 0-6 for movement, 7-9 for questions
            gameBoard.setDice(mediumGameDice);
            this.difficulty = difficulty;
        } else if (size == 13) {
            // TO DO
            this.difficulty = difficulty;

        }

        gameBoard.initializePlayerPositions(players);

        System.out.println(players);

        setupGridConstraints(size); // Set up grid constraints
        LevelLabel.setText("Level: " + difficulty);
        BoardGrid.getChildren().clear(); // Clear existing content

        BoardGrid.prefWidthProperty().bind(gamepane.widthProperty());
        BoardGrid.prefHeightProperty().bind(gamepane.heightProperty());
        gamepane.setPadding(new Insets(0, 0, 0, 0)); // Remove padding if not needed
        GridPane.setMargin(BoardGrid, new Insets(0)); // Remove margin if not needed
        for (int row = size - 1; row >= 0; row--) {
            for (int col = 0; col < size; col++) {
                // Calculate tile number starting from bottom left
                int number;  // Adjusted calculation

                if(row % 2 == 0){
                    number = (size * row) + col + 1;
                }
                else{
                    number = (size * (row + 1)) - col;
                }


                // Alternate between two colors (e.g., green and white)
                String backgroundColor;
                if ((row + col) % 2 == 0) {
                    backgroundColor = "-fx-background-color: green";
                } else {
                    backgroundColor = "-fx-background-color: white";
                }

                createTile(number, col, row, size);
            }
        }
//
//        dice = create3DDice();
//        dice.setOnMouseClicked(event -> onDiceRoll());
//
//        Group diceGroup = new Group(dice);
//        SubScene diceSubScene = create3DSubScene(diceGroup, 300, 300); // Adjust size as needed
//
//        // To Add the SubScene to the gameDataPane and align it to the left
//        gameDataPane.getChildren().add(diceSubScene);
//        AnchorPane.setLeftAnchor(diceSubScene, 10.0); // Adjust the left anchor as needed

        displayPlayers(players);
    }

    private void createTile(int number, int col, int row, int size) {
        Tile tile = new Tile(col, size - row - 1);
        String backgroundColor = ((row + col) % 2 == 0) ? "-fx-background-color: green" : "-fx-background-color: white";
        tile.setStyle(backgroundColor + "; -fx-border-color: black;");

        Label label = new Label(Integer.toString(number));
        label.setStyle("-fx-text-fill: black; -fx-font-size: 12;");
        tile.getChildren().add(label);
        GridPane.setValignment(label, VPos.TOP);
        GridPane.setHalignment(label, HPos.LEFT);

        BoardGrid.add(tile, col, size - row - 1);
    }
//    private SubScene create3DSubScene(Group content, double width, double height) {
//        PerspectiveCamera camera = new PerspectiveCamera(true);
//        camera.setTranslateZ(-500); // Adjust the camera position
//
//        SubScene subScene = new SubScene( content, width, height, true, SceneAntialiasing.BALANCED);
//        subScene.setCamera(camera);
//
//        return subScene;
//    }

    private void setupGridConstraints(int size) {
        BoardGrid.getColumnConstraints().clear();
        BoardGrid.getRowConstraints().clear();

        // Define percentage-based constraints to make the grid cells fill the pane
        for (int i = 0; i < size; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / size); // Each column takes an equal share
            BoardGrid.getColumnConstraints().add(colConst);

            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / size); // Each row takes an equal share
            BoardGrid.getRowConstraints().add(rowConst);
        }
    }

    private int determineBoardSize(Difficulty difficulty) {
        switch (difficulty) {
            case EASY:
                // Create a die instance for an easy game
                Dice easyGameDice = new Dice(6, EASY_GAME_QUESTION_PROBABILITY);
                return 7;
            case MEDIUM:
                Dice mediumGameDice = new Dice(9, MEDIUM_GAME_QUESTION_PROBABILITY); // 0-6 for movement, 7-9 for questions
                return 10;
            case HARD:
                return 13;
            default:
                throw new IllegalArgumentException("Unrecognized difficulty level");
        }
    }

    public void startGame(List<Player> players) {
        // Initialize game logic here
        setPlayers(players);
        // Display players on the board
        displayPlayers(players);
        updatePlayerTurn();

        // Other game start logic...
    }


    //display players on Gameboard
    public void displayPlayers(List<Player> players) {
        for (Player player : players) {
            Circle playerCircle = new Circle(10); // Radius of 10, adjust as needed
            playerCircle.setFill(player.getPlayerColor());

            // Get the tile for the player
            Pane tile = getTileForPlayer(player);

            // Check if the tile is valid
            if (tile != null) {
                // Add the circle to the tile
                tile.getChildren().add(playerCircle);
 }}
    }

    @FXML
    void MainMenuFun(ActionEvent event) throws IOException {
        // Create a confirmation alert
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText("Are you sure you want to end the game and back to main menu?");
        confirmationAlert.setContentText("Any unsaved changes may be lost.");

        // Add OK and Cancel buttons to the alert
        confirmationAlert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        // Show the alert and wait for user input
        confirmationAlert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                // User clicked OK, proceed with transferring to the main menu

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/snakesandladdersviper/hello-view.fxml"));
                    Parent root = loader.load();
                    Scene nextScene = new Scene(root);

                    // Get the current stage and set the new scene
                    Stage currentStage = (Stage) MainMenuButton.getScene().getWindow();
                    currentStage.setScene(nextScene);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                // User clicked Cancel, do nothing or handle accordingly
            }
        });
    }

    private Pane getTileForPlayer(Player player) {
        int size = determineBoardSize(difficulty);
        int position = gameBoard.getPlayerPosition(player) - 1; // Adjust if your positions start at 1

        int row = size - 1 - (position / size);
        int column;

        if (row % 2 == size % 2) {
            // For even rows (if size is odd) or odd rows (if size is even), numbering goes from right to left
            column = size - 1 - (position % size);
        } else {
            // For odd rows (if size is odd) or even rows (if size is even), numbering goes from left to right
            column = position % size;
        }

        // Find the corresponding tile Pane
        for (Node node : BoardGrid.getChildren()) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                return (Pane) node;
            }
        }
        return null; // Tile not found or invalid position
    }


    private String getDiceRollOutcome(Difficulty difficulty) {
        Random random = new Random();
        int maxRoll;
        int rollResult;

        switch (difficulty) {
            case EASY:
                maxRoll = 7; // 0-4 for numbers, 5 for easy question, 6 for medium question, 7 for hard question
                rollResult = random.nextInt(maxRoll + 1);
                if (rollResult >= 5) {
                    if (rollResult == 5) return "EASY_QUESTION";
                    if (rollResult == 6) return "MEDIUM_QUESTION";
                    return "HARD_QUESTION";
                }
                break;
            case MEDIUM:
                maxRoll = 6; // Maximum roll for medium difficulty
                rollResult = random.nextInt(maxRoll + 1);
                break;
            case HARD:
                maxRoll = 10; // Maximum roll for hard difficulty
                rollResult = random.nextInt(maxRoll + 1);
                break;
            default:
                throw new IllegalArgumentException("Unrecognized difficulty level");
        }

        return String.valueOf(rollResult);
    }


    private void processDiceOutcome(String outcome) {
        if (outcome.equals("EASY_QUESTION")) {
            // Handle easy question
        } else if (outcome.equals("MEDIUM_QUESTION")) {
            // Handle medium question
        } else if (outcome.equals("HARD_QUESTION")) {
            // Handle hard question
        } else {
            int steps = Integer.parseInt(outcome);
            // Move the player 'steps' number of tiles
            movePlayer(steps);
        }
    }

    @FXML
    private void onDiceRoll() {
        setupDiceRollAnimation();

        // Start the dice roll animation
        diceRollAnimation.play();

        // Handle the completion of the dice roll
        diceRollAnimation.setOnFinished(e -> {
            String finalOutcome = diceRollButton.getText();

            // Check if the final outcome is a number or a question
            if (isNumeric(finalOutcome)) {
                // If it's a number, parse it and handle it as a dice number
                int finalNumber = Integer.parseInt(finalOutcome);
                // Handle the number outcome (e.g., move the player)
                movePlayer(finalNumber);
            } else {
                // If it's not a number, it must be a question or another string outcome
                // Handle the string outcome (like "EASY_QUESTION")
                processDiceOutcome(finalOutcome);
            }

            // Update the player turn for the next move
            updatePlayerTurn();
        });
    }

    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }

    private void movePlayer(int steps) {
        // Logic to move the player 'steps' number of tiles
        // Example: movePlayerOnBoard(currentPlayer, steps);
    }

    private void updatePlayerTurn() {
        if (players == null || players.isEmpty()) {
            System.out.println("Player list is empty or not initialized.");
            return;
        }

        // Increment the currentPlayerIndex and wrap around if it exceeds the size of the players list
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();

        // Get the current player based on the updated index
        Player currentPlayer = players.get(currentPlayerIndex);

        // Update the currentPlayerLabel with the current player's name
        if (currentPlayer != null) {
            currentPlayerLabel.setText("Player " + currentPlayer.getName() + "'s Turn");
        } else {
            System.out.println("Current player is null.");
        }
    }

    public void setPlayers(List<Player> players){
        this.players = players;
    }


    //create 3d dice
//    private Box create3DDice() {
//        double size = 100.0;
//        Box dice = new Box(size, size, size);
//
//        InputStream imageStream = getClass().getResourceAsStream("/dice-twenty-faces-one.png");
//        if (imageStream != null) {
//            PhongMaterial material = new PhongMaterial();
//            material.setDiffuseMap(new Image(imageStream));
//            dice.setMaterial(material);
//        } else {
//            System.out.println("Image file not found");
//        }
//
//        return dice;
//    }


//    private void rollDice(Box dice) {
//        RotateTransition rt = new RotateTransition(Duration.seconds(1), dice);
//        rt.setByAngle(360 * 3); // Rotate several times for effect
//        rt.setCycleCount(1);
//        rt.setAxis(Rotate.Y_AXIS);
//
//        rt.setOnFinished(e -> {
//            String outcome = getDiceRollOutcome(difficulty);
//            processDiceOutcome(outcome);
//        });
//
//        rt.play();
//    }
//    private Label createClickToPlayLabel() {
//        Label label = new Label("CLICK");
//        label.setStyle("-fx-font-size: 24; -fx-text-fill: white;");
//        return label;
//    }
}
