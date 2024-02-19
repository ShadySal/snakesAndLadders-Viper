package com.example.snakesandladdersviper.Controller;

import com.example.snakesandladdersviper.Model.*;
import com.example.snakesandladdersviper.Enums.Difficulty;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;

import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import java.util.*;


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
    private List<Player> players = new ArrayList<Player>();
    private Map<Player, Circle> playerCircles;
    private Map<Player, ImageView> playerImages;
    public void initialize() {
        startTime = System.currentTimeMillis();
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateClock()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        playerCircles = new HashMap<>();
        playerImages = new HashMap<>();
        setupDiceRollAnimation();

        if (players != null) {
            loadPlayerImages();
        } else {
            System.out.println("Players list is null.");
        }
    }
    private void loadPlayerImages() {
        for (Player player : players) {
            String color = player.getPlayerColor();
            if (color != null) {
                String imageFileName = "/com/example/snakesandladdersviper/Images/" + color + ".png";
                InputStream is = getClass().getResourceAsStream(imageFileName);
                if (is != null) {
                    Image image = new Image(is);
                    ImageView imageView = new ImageView(image);
                    imageView.setFitHeight(20); // Set size as needed
                    imageView.setFitWidth(20);
                    playerImages.put(player, imageView);
                } else {
                    System.out.println("Image file not found: " + imageFileName);
                }
            } else {
                System.out.println("Color is null for player: " + player.getName());
            }
        }
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
            boolean isQuestionTurn = random.nextBoolean(); // 50% chance of getting question
            if (isQuestionTurn) {
                rollResult = random.nextInt(3);
                if(rollResult == 0){
                    return "EASY_QUESTION";
                }
                if(rollResult == 1){
                    return "MEDIUM_QUESTION";
                }
                if(rollResult == 2){
                    return "HARD_QUESTION";
                }
            } else {
                rollResult = random.nextInt(6) + 1;
                return String.valueOf(rollResult);
            }
        } else if (difficulty == Difficulty.HARD) {
            if (random.nextDouble() < 0.25) { // 25% chance for a hard question
                return "HARD_QUESTION";
            } else if (random.nextDouble() < 0.5) { // Additional 25% chance for an easy or medium question
                rollResult = random.nextInt(2);
                if(rollResult == 0) {
                    return "EASY_QUESTION";
                } else {
                    return "MEDIUM_QUESTION";
                }
            } else {
                rollResult = random.nextInt(6) + 1; // 1-6 for movement
                return String.valueOf(rollResult);
            }
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

        // Initialize special tiles (questions and surprise)
        Map<Integer, String> specialTiles = generateSpecialTiles(difficulty, size);

        this.difficulty = difficulty;
        gameBoard.setDice(new Dice(6, EASY_GAME_QUESTION_PROBABILITY));
        gameBoard.initializePlayerPositions(players);

        setupGridConstraints(size);
        LevelLabel.setText("Level: " + difficulty);
        BoardGrid.getChildren().clear();
        gameBoard.initializePlayerPositions(players);
        setupGridConstraints(size);
        LevelLabel.setText("Level: " + difficulty);
        BoardGrid.getChildren().clear();

        BoardGrid.prefWidthProperty().bind(gamepane.widthProperty());
        BoardGrid.prefHeightProperty().bind(gamepane.heightProperty());
        gamepane.setPadding(new Insets(0, 0, 0, 0));
        GridPane.setMargin(BoardGrid, new Insets(0));

        for (int row = size - 1; row >= 0; row--) {
            for (int col = 0; col < size; col++) {
                int number = calculateTileNumber(row, col, size);
                createTile(number, col, row, size, specialTiles.getOrDefault(number, ""));
            }
        }
        setPlayers(players);

        updatePlayerTurn();
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


    }

    private int calculateTileNumber(int row, int col, int size) {
        if (row % 2 == 0) {
            return (size * row) + col + 1;
        } else {
            return (size * (row + 1)) - col;
        }
    }
    private Map<Integer, String> generateSpecialTiles(Difficulty difficulty, int size) {
        Map<Integer, String> specialTiles = new HashMap<>();
        Random random = new Random();
        Set<Integer> usedTiles = new HashSet<>();

        // Define colors for question tiles
        String[] questionColors = {"red", "green", "yellow"};
        for (int i = 0; i < questionColors.length; i++) {
            int tile = random.nextInt(size * size) + 1;
            while (usedTiles.contains(tile)) {
                tile = random.nextInt(size * size) + 1;
            }
            specialTiles.put(tile, questionColors[i]);
            usedTiles.add(tile);
        }

        if (difficulty != Difficulty.EASY) {
            int startTile = (size * size) - 10;
            int endTile = size * size;
            int surpriseTilesCount = (difficulty == Difficulty.MEDIUM) ? 1 : 2;

            for (int i = 0; i < surpriseTilesCount; i++) {
                int tile = random.nextInt(endTile - startTile + 1) + startTile;
                while (usedTiles.contains(tile)) {
                    tile = random.nextInt(endTile - startTile + 1) + startTile;
                }
                specialTiles.put(tile, "blue");
                usedTiles.add(tile);
            }
        }
        return specialTiles;
    }


    private void createTile(int number, int col, int row, int size, String specialTileColor) {
        Tile tile = new Tile(col, size - row - 1);
        String backgroundColor = "-fx-background-color: ";
        String textColor = "-fx-text-fill: black;";
        String tileLabel = Integer.toString(number);

        if (!specialTileColor.isEmpty()) {
            backgroundColor += specialTileColor;
            if (specialTileColor.equals("blue")) {
                tileLabel = "Surprise";
            } else {
                tileLabel = "?";
            }
            textColor = "-fx-text-fill: white;";
        } else {
            backgroundColor += ((row + col) % 2 == 0) ? "green" : "white";
        }

        tile.setStyle(backgroundColor + "; -fx-border-color: black;");

        // Create a StackPane to hold the label
        StackPane stackPane = new StackPane();
        Label label = new Label(tileLabel);
        label.setStyle(textColor + " -fx-font-size: 12;");

        // Add label to StackPane and StackPane to Tile
        stackPane.getChildren().add(label);
        tile.getChildren().add(stackPane);

        // Set alignment for the label in the upper-left corner
        StackPane.setAlignment(label, Pos.TOP_LEFT);

        BoardGrid.add(tile, col, size - row - 1);
    }



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



    //display players on Gameboard
    public void displayPlayers(List<Player> players) {
        for (Player player : players) {
            ImageView playerImage = playerImages.get(player);
            Pane tile = getTileForPlayer(player);
            if (tile != null && playerImage != null) {
                tile.getChildren().add(playerImage);
                playerImage.setFitHeight(1000); // Increase height (e.g., 40)
                playerImage.setFitWidth(1000);
                int imageIndex = tile.getChildren().indexOf(playerImage);
                double xOffset = (imageIndex - 0.8) * 10;
                double yOffset = (imageIndex - 0.3) * 11;
                playerImage.setTranslateX(xOffset);
                playerImage.setTranslateY(yOffset);
            } else {
                System.out.println("Tile or Player Image is null for player: " + player.getName());
            }
        }
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





    private void processDiceOutcome(String outcome) {
        boolean questionAnsweredCorrectly = false;

        if (outcome.equals("EASY_QUESTION")) {
            questionAnsweredCorrectly = askQuestion("easy");
        } else if (outcome.equals("MEDIUM_QUESTION")) {
            questionAnsweredCorrectly = askQuestion("medium");
        } else if (outcome.equals("HARD_QUESTION")) {
            questionAnsweredCorrectly = askQuestion("hard");
        } else {
            int steps = Integer.parseInt(outcome);
            movePlayer(steps);
            return;
        }

        handleQuestionOutcome(questionAnsweredCorrectly, outcome);
    }
    private void handleQuestionOutcome(boolean isCorrect, String difficulty) {
        Player currentPlayer = players.get(currentPlayerIndex);
        if (isCorrect) {
            if (difficulty.equals("HARD_QUESTION")) {
                // If the question is hard and the answer is correct, move forward one step
                gameBoard.movePlayer(currentPlayer, 1);
            }
            // No action needed for correct answers to easy or medium questions
        } else {
            int stepsBack = 0;
            if (difficulty.equals("EASY_QUESTION")) {
                stepsBack = 1;
            } else if (difficulty.equals("MEDIUM_QUESTION")) {
                stepsBack = 2;
            } else if (difficulty.equals("HARD_QUESTION")) {
                stepsBack = 3;
            }

            int currentPosition = gameBoard.getPlayerPosition(currentPlayer);
            int newPosition = Math.max(1, currentPosition - stepsBack); // Ensure not to go below tile 1
            gameBoard.setPlayerPosition(currentPlayer, newPosition);
        }

        updatePlayerPositionOnBoard(currentPlayer);
    }

    private boolean askQuestion(String difficultyLevel) {
        int difficulty = convertDifficultyLevelToNumber(difficultyLevel);
        Question question = SysData.getInstance().getRandomQuestion(difficulty);

        if (question == null) {
            showAlert("No Questions", "No questions available for this difficulty.");
            return false;
        }

        final boolean[] isCorrect = {false};

        // Schedule the dialog to be shown on the JavaFX Application Thread
        Platform.runLater(() -> {
            isCorrect[0] = showQuestionDialog(question, difficultyLevel);
        });

        return isCorrect[0];
    }
    private boolean showQuestionDialog(Question question, String difficultyLevel) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Question - " + capitalizeFirstLetter(difficultyLevel));
        dialog.setHeaderText(question.getQuestionText());

        // Set up the buttons
        ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(confirmButtonType);

        // Create a VBox to hold question choices
        VBox vbox = new VBox(10);
        ToggleGroup group = new ToggleGroup();

        question.getAnswers().forEach((key, value) -> {
            RadioButton rb = new RadioButton(value);
            rb.setUserData(key); // Store answer key
            rb.setToggleGroup(group);
            vbox.getChildren().add(rb);
        });

        dialog.getDialogPane().setContent(vbox);

        // Disable the "Confirm" button initially
        final Button confirmButton = (Button) dialog.getDialogPane().lookupButton(confirmButtonType);
        confirmButton.setDisable(true);

        // Enable the "Confirm" button only when an option is selected
        group.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            confirmButton.setDisable(newVal == null);
        });

        // Show dialog and wait for response
        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == confirmButtonType) {
            RadioButton selected = (RadioButton) group.getSelectedToggle();
            boolean isCorrect = question.getCorrectAns() == (int) selected.getUserData();
            showAnswerResultAlert(isCorrect);
            return isCorrect;
        }

        // Considered wrong if no answer is selected
        showAnswerResultAlert(false);
        return false;
    }

    private void showAnswerResultAlert(boolean isCorrect) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Answer Result");
        if (isCorrect) {
            alert.setHeaderText("Correct Answer!");
            alert.setContentText("You have answered the question correctly.");
        } else {
            alert.setHeaderText("Wrong Answer");
            alert.setContentText("Sorry, your answer is incorrect.");
        }
        alert.showAndWait();
    }

    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    private int convertDifficultyLevelToNumber(String difficultyLevel) {
        switch (difficultyLevel.toLowerCase()) {
            case "easy":
                return 1;
            case "medium":
                return 2;
            case "hard":
                return 3;
            default:
                return 0;
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
        Player currentPlayer = players.get(currentPlayerIndex);

        // Update the player's position on the game board
        gameBoard.movePlayer(currentPlayer, steps);

        // Update the UI to reflect the new position
        updatePlayerPositionOnBoard(currentPlayer);
    }
    private void updatePlayerPositionOnBoard(Player player) {
        // Get the player's new position
        int newPosition = gameBoard.getPlayerPosition(player);

        // Convert this position to row and column on the grid
        int size = determineBoardSize(difficulty);
        int row = size - 1 - (newPosition / size);
        int column;
        if (row % 2 == size % 2) {
            column = size - 1 - (newPosition % size);
        } else {
            column = newPosition % size;
        }

        // Get the tile Pane for the new position
        Pane newTile = getTileForPlayer(player);

        // Move the player's visual representation to the new tile
        if (newTile != null) {
            Circle playerCircle = getPlayerCircle(player);

            // Check if the circle is already on the tile
            if (!newTile.getChildren().contains(playerCircle)) {
                // If not, add it to the tile
                newTile.getChildren().add(playerCircle);
            }
        }
    }
    private Circle getPlayerCircle(Player player) {
        return playerCircles.get(player);
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
        playerImages = new HashMap<>();
        if (players != null) {
            loadPlayerImages();
            displayPlayers(players);

        } else {
            System.out.println("Players list is null.");
        }
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

//    private String getDiceRollOutcome(Difficulty difficulty) {
//        Random random = new Random();
//        int maxRoll;
//        int rollResult;
//
//        switch (difficulty) {
//            case EASY:
//                maxRoll = 7; // 0-4 for numbers, 5 for easy question, 6 for medium question, 7 for hard question
//                rollResult = random.nextInt(maxRoll + 1);
//                if (rollResult >= 5) {
//                    if (rollResult == 5) return "EASY_QUESTION";
//                    if (rollResult == 6) return "MEDIUM_QUESTION";
//                    return "HARD_QUESTION";
//                }
//                break;
//            case MEDIUM:
//                boolean isQuestionTurn = random.nextBoolean(); // 50% chance of getting question
//                if (isQuestionTurn) {
//                    rollResult = random.nextInt(3);
//                    if(rollResult == 0){
//                        return "EASY_QUESTION";
//                    }
//                    if(rollResult == 1){
//                        return "MEDIUM_QUESTION";
//                    }
//                    if(rollResult == 2){
//                        return "HARD_QUESTION";
//                    }
//                } else {
//                    rollResult = random.nextInt(6) + 1; // 1-6 להתקדמות
//                }
//                break;
//            case HARD:
//                maxRoll = 10; // Maximum roll for hard difficulty
//                rollResult = random.nextInt(maxRoll + 1);
//                break;
//            default:
//                throw new IllegalArgumentException("Unrecognized difficulty level");
//        }
//
//        return String.valueOf(rollResult);
//    }
}
