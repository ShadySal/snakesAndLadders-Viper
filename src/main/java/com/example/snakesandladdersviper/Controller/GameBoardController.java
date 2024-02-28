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
import javafx.util.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Consumer;

public class GameBoardController implements GameObserver {
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


    @Override
    public void update(GameEvent event) {
        switch (event.getEventType()) {
            case PLAYER_MOVED:
                handlePlayerMove(event);
                break;
            case SPECIAL_TILE_HIT:
                handleSpecialTile(event);
                break;
            case PLAYER_WON:
                handlePlayerWin(event);
                break;
            // Add other cases as needed...
        }
    }

    private void handlePlayerMove(GameEvent event) {
        Player player = event.getPlayer();
        int newPosition = event.getNewPosition();

        // Find the corresponding tile Pane for the new position
        Pane newTile = getTileForPosition(newPosition);

        if (newTile != null) {
            ImageView playerImage = playerImages.get(player);

            // Remove player image from the old position
            Pane oldTile = getTileForPlayer(player);
            oldTile.getChildren().remove(playerImage);

            // Add player image to the new position
            newTile.getChildren().add(playerImage);

            // Adjust the position of the image in the tile
            // You might want to animate this movement for a better UX
            int imageIndex = newTile.getChildren().indexOf(playerImage);
            double xOffset = (imageIndex - 0.8) * 40;
            double yOffset = (imageIndex - 0.3) * 40;
            playerImage.setTranslateX(xOffset);
            playerImage.setTranslateY(yOffset);
        }
    }

    private void handleSpecialTile(GameEvent event) {
        Player player = event.getPlayer();
        int tilePosition = event.getTilePosition();
        String tileType = event.getTileType();

        // Based on tileType, you can show a dialog, animate movement, etc.
        switch (tileType) {
            case "snake":
                // Handle snake logic
                break;
            case "ladder":
                // Handle ladder logic
                break;
            case "question":
                // Show a question dialog and wait for the answer
                break;
            // Handle other special tile types...
        }
    }

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
                case 5:
                    return "EASY_QUESTION";
                case 6:
                    return "MEDIUM_QUESTION";
                case 7:
                    return "HARD_QUESTION";
                default:
                    return String.valueOf(rollResult);
            }
        } else if (difficulty == Difficulty.MEDIUM) {
            boolean isQuestionTurn = random.nextBoolean(); // 50% chance of getting question
            if (isQuestionTurn) {
                rollResult = random.nextInt(3);
                if (rollResult == 0) {
                    return "EASY_QUESTION";
                }
                if (rollResult == 1) {
                    return "MEDIUM_QUESTION";
                }
                if (rollResult == 2) {
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
                if (rollResult == 0) {
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
        generateSnakesAndLadders(difficulty);

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

    private void generateSnakesAndLadders(Difficulty difficulty) {
        int boardSize = determineBoardSize(difficulty);
        Map<Integer, String> specialTiles = generateSpecialTiles(difficulty, boardSize);
        Set<Integer> occupiedPositions = determineOccupiedPositions(specialTiles, boardSize);

        Pair<List<Snake>, List<Ladder>> snakesAndLadders = createDynamicSnakesAndLadders(difficulty, boardSize, occupiedPositions);
        placeSnakesAndLadders(snakesAndLadders.getKey(), snakesAndLadders.getValue());
    }

    private Set<Integer> determineOccupiedPositions(Map<Integer, String> specialTiles, int boardSize) {
        Set<Integer> occupiedPositions = new HashSet<>();

        // Add special tile positions to the occupied set
        if (specialTiles != null) {
            occupiedPositions.addAll(specialTiles.keySet());
        }

        // Add the start and end positions (typically 1 and the last tile)
        occupiedPositions.add(1); // Starting position
        occupiedPositions.add(boardSize * boardSize); // Ending position

        return occupiedPositions;
    }


    private Pair<List<Snake>, List<Ladder>> createDynamicSnakesAndLadders(Difficulty difficulty, int boardSize, Set<Integer> occupiedPositions) {
        List<Snake> snakes = new ArrayList<>();
        List<Ladder> ladders = new ArrayList<>();
        int maxPosition = boardSize * boardSize;

        // Define the number of snakes and ladders based on difficulty
        int numberOfSnakes, numberOfLadders;
        switch (difficulty) {
            case EASY:
                numberOfSnakes = numberOfLadders = 4;
                break;
            case MEDIUM:
                numberOfSnakes = numberOfLadders = 6;
                break;
            case HARD:
                numberOfSnakes = numberOfLadders = 8;
                break;
            default:
                throw new IllegalArgumentException("Unrecognized difficulty level");
        }

        addRandomSnakes(snakes, numberOfSnakes, maxPosition, occupiedPositions);
        addRandomLadders(ladders, numberOfLadders, maxPosition, occupiedPositions);

        return new Pair<>(snakes, ladders);
    }

    private void addRandomSnakes(List<Snake> snakes, int numberOfSnakes, int maxPosition, Set<Integer> occupiedPositions) {
        Random random = new Random();
        for (int i = 0; i < numberOfSnakes; i++) {
            int start, end;
            do {
                start = random.nextInt(maxPosition - 1) + 2;
                end = random.nextInt(start - 1) + 1;
            } while (occupiedPositions.contains(start) || occupiedPositions.contains(end));
            occupiedPositions.add(start);
            occupiedPositions.add(end);
            snakes.add(GameElementFactory.createSnake(start, end));
        }
    }

    private void addRandomLadders(List<Ladder> ladders, int numberOfLadders, int maxPosition, Set<Integer> occupiedPositions) {
        Random random = new Random();
        for (int i = 0; i < numberOfLadders; i++) {
            int start, end;
            do {
                start = random.nextInt(maxPosition - 1) + 1;
                end = random.nextInt(maxPosition - start) + start + 1;
            } while (occupiedPositions.contains(start) || occupiedPositions.contains(end));
            occupiedPositions.add(start);
            occupiedPositions.add(end);
            ladders.add(GameElementFactory.createLadder(start, end));
        }
    }

    private void placeSnakesAndLadders(List<Snake> snakes, List<Ladder> ladders) {
        for (Snake snake : snakes) {
            placeSnakeOnBoard(snake);
        }
        for (Ladder ladder : ladders) {
            placeLadderOnBoard(ladder);
        }
    }

    private void placeSnakeOnBoard(Snake snake) {
        Pane startTile = getTileForPosition(snake.getStartPosition());
        Pane endTile = getTileForPosition(snake.getEndPosition());
        //ImageView snakeImage = new ImageView(new Image(/* path to snake image */));
        // Additional logic for positioning the snake image
        //startTile.getChildren().add(snakeImage);
    }

    private void placeLadderOnBoard(Ladder ladder) {
        Pane startTile = getTileForPosition(ladder.getStart());
        Pane endTile = getTileForPosition(ladder.getEnd());
//        ImageView ladderImage = new ImageView(new Image(/* path to ladder image */));
//        // Additional logic for positioning the ladder image
//        startTile.getChildren().add(ladderImage);
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
                playerImage.setFitHeight(40); // Increase height (e.g., 40)
                playerImage.setFitWidth(40);
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

    private Pane getTileForPosition(int position) {
        int size = determineBoardSize(difficulty); // Assuming this method returns the size of the grid
        position--; // Adjusting because positions start at 1

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
        if (outcome.equals("EASY_QUESTION")) {
            askQuestion("easy", isCorrect -> handleQuestionOutcome(isCorrect, "easy"));
        } else if (outcome.equals("MEDIUM_QUESTION")) {
            askQuestion("medium", isCorrect -> handleQuestionOutcome(isCorrect, "medium"));
        } else if (outcome.equals("HARD_QUESTION")) {
            askQuestion("hard", isCorrect -> handleQuestionOutcome(isCorrect, "hard"));
        } else {
            int steps = Integer.parseInt(outcome);
            movePlayer(steps);
        }
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

    private void askQuestion(String difficultyLevel, Consumer<Boolean> callback) {
        int difficulty = convertDifficultyLevelToNumber(difficultyLevel);
        Question question = SysData.getInstance().getRandomQuestion(difficulty);

        if (question == null) {
            showAlert("No Questions", "No questions available for this difficulty.");
            callback.accept(false); // Assuming false for no question available
            return;
        }

        Platform.runLater(() -> {
            boolean isCorrect = showQuestionDialog(question, difficultyLevel);
            callback.accept(isCorrect);
        });
    }


    private boolean showQuestionDialog(Question question, String difficultyLevel) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Question - " + capitalizeFirstLetter(difficultyLevel));
        dialog.setHeaderText(question.getQuestionText());

        // Setting the owner of the dialog
        Stage primaryStage = (Stage) BoardGrid.getScene().getWindow(); // Replace 'BoardGrid' with an actual component from your primary stage
        dialog.initOwner(primaryStage);

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
        group.selectedToggleProperty().addListener((obs, oldVal, newVal) -> confirmButton.setDisable(newVal == null));

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

        // Setting the owner of the alert
        Stage primaryStage = (Stage) BoardGrid.getScene().getWindow(); // Replace 'BoardGrid' with an actual component from your primary stage
        alert.initOwner(primaryStage);

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
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void movePlayer(int steps) {
        Player currentPlayer = players.get(currentPlayerIndex);

        // Update the player's position on the game board
        gameBoard.movePlayer(currentPlayer, steps);

        // Update the UI to reflect the new position
        updatePlayerPositionOnBoard(currentPlayer);

        if (hasPlayerWon(currentPlayer)) {
            // Create a GameEvent object with the winning player
            GameEvent winEvent = new GameEvent(currentPlayer); // Adjust this line based on your GameEvent class constructor or methods

            // Handle the player win with the correct GameEvent object
            handlePlayerWin(winEvent);
        }
    }

    private void handlePlayerWin(GameEvent event) {
        Player player = event.getPlayer(); // Assuming event.getPlayer() returns a Player object

        // Show a congratulatory message or dialog
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText("Congratulations " + player.getName() + "! You have won the game.");
        alert.showAndWait();

        // Assuming the addGameHistory method in SysData requires the winner's name, start time, and difficulty
        // Here, you need to provide the correct difficulty level if required
        String difficulty = "Easy"; // Example, adjust based on your game's logic
        SysData.getInstance().addGameHistory(player.getName(), startTime, difficulty);

        // You can also add animations or sounds here to celebrate the win
    }

    private void updatePlayerPositionOnBoard(Player player) {
        // Get the player's new position
        int newPosition = gameBoard.getPlayerPosition(player);

        // Convert this position to row and column on the grid
        int size = determineBoardSize(difficulty);
        int row = size - 1 - (newPosition / size);
        int column = (row % 2 == size % 2) ? size - 1 - (newPosition % size) : newPosition % size;

        // Get the tile Pane for the new position
        Pane newTile = getTileForPlayer(player);

        if (newTile != null) {
            ImageView playerImage = playerImages.get(player);

            // Check if the ImageView is already on the tile
            if (!newTile.getChildren().contains(playerImage)) {
                // If not, add it to the tile
                newTile.getChildren().add(playerImage);
            }

            // Adjust the position of the image in the tile
            int imageIndex = newTile.getChildren().indexOf(playerImage);
            double xOffset = (imageIndex - 0.8) * 40; // Adjust based on image size
            double yOffset = (imageIndex - 0.3) * 40;
            playerImage.setTranslateX(xOffset);
            playerImage.setTranslateY(yOffset);
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

    public void setPlayers(List<Player> players) {
        this.players = players;
        playerImages = new HashMap<>();
        if (players != null) {
            loadPlayerImages();
            displayPlayers(players);

        } else {
            System.out.println("Players list is null.");
        }
    }

    public boolean hasPlayerWon(Player player) {
        int totalTiles = getTotalTilesForDifficulty(difficulty);
        return gameBoard.getPlayerPosition(player) == totalTiles;
    }

    private int getTotalTilesForDifficulty(Difficulty difficulty) {
        switch (difficulty) {
            case EASY:
                return 7 * 7; // Assuming a 7x7 board for easy difficulty
            case MEDIUM:
                return 10 * 10; // Assuming a 10x10 board for medium difficulty
            case HARD:
                return 13 * 13; // Assuming a 13x13 board for hard difficulty
            default:
                throw new IllegalArgumentException("Unrecognized difficulty level");
        }
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

