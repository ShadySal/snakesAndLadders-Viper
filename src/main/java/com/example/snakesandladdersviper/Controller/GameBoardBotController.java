package com.example.snakesandladdersviper.Controller;

import com.example.snakesandladdersviper.Enums.Difficulty;
import com.example.snakesandladdersviper.Model.*;
import com.example.snakesandladdersviper.Utils.SceneUtils;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

public class GameBoardBotController{
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
    @FXML
    private Pane diceImageContainer;
    private Timeline timeline;
    private long startTime;
    // Example values for probabilities
    final double EASY_GAME_QUESTION_PROBABILITY = 0.1; // 10% chance for a question
    final double MEDIUM_GAME_QUESTION_PROBABILITY = EASY_GAME_QUESTION_PROBABILITY * 2; // 20% chance
    final double HARD_GAME_QUESTION_PROBABILITY = 0.25;
    final double REGULAR_NUMBER_PROBABILITY = 0.25; // 25% for a regular number
    final double EASY_MEDIUM_QUESTION_PROBABILITY = 0.50; // 50% for easy/medium questions

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
    private Map<Player, Group> playerImages;
    private Set<Integer> occupiedPositions;
    private int bsize;
    private int globalSize;
    private Map<Integer, Integer> snakePositions = new HashMap<>();
    private Map<Integer, Integer> ladderPositions = new HashMap<>();
    private Map<Integer, String> SpecialTiles = new HashMap<>();
    private boolean isBotTurn = false;

    public void initialize() {
        startTime = System.currentTimeMillis();
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateClock()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        playerCircles = new HashMap<>();
        playerImages = new HashMap<>();
        occupiedPositions = new HashSet<>();
        gamepane = new Pane();
        setupDiceRollAnimation();
        if (players != null) {
            loadPlayerImages();
        } else {
            System.out.println("Players list is null.");
        }
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
    private void drawLadderOnBoard(List<Ladder> ladders) {
        for (Ladder ladder : ladders) {
            Tile startTile = getTileByNumber(ladder.getStart());
            Tile endTile = getTileByNumber(ladder.getEnd());

            if (startTile != null && endTile != null) {
                Bounds startBounds = startTile.localToScene(startTile.getBoundsInLocal());
                Bounds endBounds = endTile.localToScene(endTile.getBoundsInLocal());

                double startX = startBounds.getMinX() + startBounds.getWidth() / 2;
                double startY = startBounds.getMinY() + startBounds.getHeight() / 2;
                double endX = endBounds.getMinX() + endBounds.getWidth() / 2;
                double endY = endBounds.getMinY() + endBounds.getHeight() / 2;

                double ladderWidth = 8; // Adjust for desired visual appearance

                // Creating a gradient effect for the ladder sides
                LinearGradient ladderGradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.BROWN.darker()), new Stop(1, Color.BROWN.brighter()));

                // Ladder sides with gradient effect
                Line leftSide = new Line(startX - ladderWidth, startY, endX - ladderWidth, endY);
                Line rightSide = new Line(startX + ladderWidth, startY, endX + ladderWidth, endY);
                leftSide.setStroke(ladderGradient);
                rightSide.setStroke(ladderGradient);
                leftSide.setStrokeWidth(4);
                rightSide.setStrokeWidth(4);

                // Shadow effect for 3D appearance
                DropShadow shadow = new DropShadow();
                shadow.setOffsetY(3.0);
                shadow.setColor(Color.color(0, 0, 0, 0.5));
                leftSide.setEffect(shadow);
                rightSide.setEffect(shadow);

                contentPane.getChildren().addAll(leftSide, rightSide);

                // Determine the number of rungs based on ladder height
                double distance = Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));
                int rungsCount = (int) (distance / 20); // Example calculation for rung count

                for (int i = 1; i <= rungsCount; i++) {
                    double fraction = (double) i / (rungsCount + 1);
                    double rungX = startX - ladderWidth + (endX - startX) * fraction;
                    double rungY = startY + (endY - startY) * fraction;

                    Line rung = new Line(rungX, rungY, rungX + 2 * ladderWidth, rungY);
                    rung.setStrokeWidth(2);
                    rung.setStroke(ladderGradient); // Apply gradient effect to rungs as well
                    rung.setEffect(shadow); // Apply shadow effect for 3D appearance
                    contentPane.getChildren().add(rung);
                }
            }
        }
    }
    private void drawLinesForSnakes(List<Snake> snakes) {
        for (Snake snake : snakes) {
            Tile startTile = getTileByNumber(snake.getStart());
            Tile endTile = getTileByNumber(snake.getEndPosition());

            if (startTile != null && endTile != null) {
                Bounds startBounds = startTile.localToParent(startTile.getBoundsInLocal());
                Bounds endBounds = endTile.localToParent(endTile.getBoundsInLocal());

                double startX = startBounds.getMinX() + startBounds.getWidth() / 2;
                double startY = startBounds.getMinY() + startBounds.getHeight() / 2;
                double endX = endBounds.getMinX() + endBounds.getWidth() / 2;
                double endY = endBounds.getMinY() + endBounds.getHeight() / 2;

                Path snakePath = new Path();
                snakePath.setStrokeWidth(4); // Adjusted for visual clarity
                snakePath.getElements().add(new MoveTo(startX, startY));

                // Intermediate points for the curves
                double quarterX = (startX * 3 + endX) / 4;
                double quarterY = (startY * 3 + endY) / 4;
                double threeQuarterX = (startX + endX * 3) / 4;
                double threeQuarterY = (startY + endY * 3) / 4;

                // Adjust these values to change the magnitude and direction of the curves
                double curveMagnitude = 220; // Adjust for curve depth

                // First curve to the left
                double ctrlX1 = quarterX;
                double ctrlY1 = quarterY - curveMagnitude;
                // Second curve to the right
                double ctrlX2 = threeQuarterX;
                double ctrlY2 = threeQuarterY + curveMagnitude;

                snakePath.getElements().add(new CubicCurveTo(ctrlX1, ctrlY1, ctrlX2, ctrlY2, endX, endY));

                Color snakeColor;
                switch (snake.getType()) {
                    case "yellow":
                        snakeColor = Color.YELLOW;
                        break;
                    case "green":
                        snakeColor = Color.GREEN;
                        break;
                    case "blue":
                        snakeColor = Color.BLUE;
                        break;
                    case "red":
                        snakeColor = Color.RED;
                        break;
                    default:
                        snakeColor = Color.BLACK; // Default color
                        break;
                }

                snakePath.setStroke(snakeColor);
                snakePath.setFill(null); // Do not fill the path

                contentPane.getChildren().add(snakePath);

                // Add a more detailed snake head at the start position
                addDetailedSnakeHead(startX, startY, snakeColor);
            }
        }
    }
    private void addDetailedSnakeHead(double x, double y, Color bodyColor) {
        double headRadius = 7; // Adjust size as needed
        Circle head = new Circle(x, y, headRadius);
        head.setFill(bodyColor);

        // Eyes
        Circle eye1 = new Circle(x - headRadius / 3, y - headRadius / 2, 1.5, Color.WHITE);
        Circle eye2 = new Circle(x + headRadius / 3, y - headRadius / 2, 1.5, Color.WHITE);

        // Tongue
        Path tongue = new Path();
        tongue.getElements().add(new MoveTo(x, y + headRadius / 2));
        tongue.getElements().add(new LineTo(x - headRadius / 4, y + headRadius / 2 + 2));
        tongue.getElements().add(new MoveTo(x, y + headRadius / 2));
        tongue.getElements().add(new LineTo(x + headRadius / 4, y + headRadius / 2 + 2));
        tongue.setStroke(Color.RED);
        tongue.setStrokeWidth(1);

        contentPane.getChildren().addAll(head, eye1, eye2, tongue);
    }




    private void createTile(int number, int col, int row, int size, String specialTileColor, double tileSize) {
        Tile tile = new Tile(col, size - row - 1);
        tile.setNumber(number);
        tile.setWidth(tileSize);
        tile.setHeight(tileSize);
        tile.setX(col * tileSize); // Position the tile based on column
        tile.setY(row * tileSize); // Position the tile based on row

        String backgroundColor;
        if (!specialTileColor.isEmpty()) {
            tile.setColor(specialTileColor);
            backgroundColor = specialTileColor;
        } else {
            // Adjusting tile colors here. You can choose a specific light green color that matches your UI theme.
            backgroundColor = ((row + col) % 2 == 0) ? "#90ee90" : "#f0fff0"; // LightGreen and Honeydew colors for a subtle variation
        }
        tile.setFill(Color.web(backgroundColor)); // Set the color of the rectangle
        tile.setStroke(Color.BLACK); // Set border color

        // Create a text object for the tile number or special text
        Text text = new Text();
        if (specialTileColor.equals("blue")) {
            text.setText("Surprise");
        } else if (!specialTileColor.isEmpty()) {
            text.setText("?");
        } else {
            text.setText(Integer.toString(number)); // Set the tile number
        }
        // Positioning the text at the upper left corner of the tile with a small margin
        double margin = 5; // Margin from the tile edges
        text.setStyle("-fx-font-size: " + tileSize / 5 + ";"); // Font size relative to tile size
        text.setLayoutX(tile.getX() + tileSize / 2 - text.getLayoutBounds().getWidth() / 2); // Center text horizontally
        text.setLayoutY(tile.getY() + tileSize / 2 + text.getLayoutBounds().getHeight() / 4); // Center text vertically// Align with the top edge plus margin
        // Add the tile and text to the game pane
        contentPane.getChildren().addAll(tile, text);
    }
    public void initializeBoard(Difficulty difficulty, List<Player> players) {
        int size = determineBoardSize(difficulty);
        this.globalSize = size;
        this.bsize = size*size;
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        double screenWidth = screenBounds.getWidth();
        double screenHeight = screenBounds.getHeight() * 0.95;
        gameBoard = new GameBoard(size, size);
        List<Snake> snakes = new ArrayList<>(); // List to store snakes
        List<Ladder> ladders = new ArrayList<>();
        Map<Integer, String> specialTiles = generateSpecialTiles(difficulty, size);
        Set<Integer> occupiedPositions = determineOccupiedPositions(specialTiles, size); // Determine occupied positions

        // Initialize special tiles (questions and surprise)
        //contentPane.setLayoutY(contentPane.getLayoutY() - 20); // Move up by 20 units

        contentPane.setPrefWidth(screenWidth * 0.75);
        contentPane.setPrefHeight(screenHeight);
        double gameDataVBoxWidth = screenWidth * 0.25 * 0.75; // Adjust the proportion as needed
        double gamePaneWidth = contentPane.getPrefWidth() - gameDataVBoxWidth;
        gamepane.setPrefWidth(gamePaneWidth);
        gamepane.setPrefHeight(contentPane.getPrefHeight());

        VBox gameDataVBox = new VBox(13); // Include spacing between elements
        gameDataVBox.setLayoutX(gamePaneWidth);
        gameDataVBox.setPrefWidth(gameDataVBoxWidth);
        gameDataVBox.setPrefHeight(contentPane.getPrefHeight());
        gameDataVBox.setPadding(new Insets(10, 70, 10, 20));

        // Initialize your components here (only showing a subset for brevity)
        Label levelLabel = new Label("Level: " + difficulty);
        diceImageContainer.setPrefHeight(150);
        diceImageContainer.setPrefWidth(150);

        // Spacer to push the "Exit Game" button to the bottom
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        // Create a container for the dice roll button and image
        VBox diceContainer = new VBox(12); // Spacing between the dice roll button and the image
        //diceContainer.setAlignment(Pos.CENTER); // Center these components within the container
        // Adjust the dice image container size if necessary
        diceImageContainer.setPrefSize(150, 150); // Example size, adjust as needed
        // Spacer to push elements to their positions
        Region spacerTop = new Region();
        VBox.setVgrow(spacerTop, Priority.ALWAYS);
        Region spacerBottom = new Region();
        VBox.setVgrow(spacerBottom, Priority.ALWAYS);

        Button exitGameButton = new Button("Exit Game");
        exitGameButton.setOnAction(event -> MainMenuFun(event));

// Style the dice roll button if needed
        diceRollButton.setStyle("-fx-font-weight: bold; -fx-font-size: 8px;");
        diceRollButton.setWrapText(true); // Allows text to wrap if it's too long
        diceRollButton.setMaxWidth(400); // Set a max width to ensure text wrapping; adjust as needed

        diceImageContainer.setPrefHeight(150);
        diceImageContainer.setPrefWidth(150);
        // Style the Level label
        levelLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2e8b57; -fx-font-size: 16px;"); // Increase font size

        // Style the Time label and make it bigger
        timeLabel.setStyle("-fx-font-weight: normal; -fx-text-fill: #00008b; -fx-font-size: 14px;"); // Increase font size

        // Style the Player Turn label and make it bigger
        currentPlayerLabel.setStyle("-fx-font-weight: normal; -fx-text-fill: #8b0000; -fx-font-size: 14px;"); // Increase font size
// Ensure currentPlayerLabel is initialized correctly somewhere in your class, e.g., in the initialize method
        currentPlayerLabel.setWrapText(true); // Allows text to wrap if it's too long
        currentPlayerLabel.setMaxWidth(200); // Set a max width to ensure text wrapping; adjust as needed
        // currentPlayerLabel.setTextAlignment(TextAlignment.CENTER); // Center align the text

        // Add the VBox to the root pane
        contentPane.getChildren().add(gameDataVBox);
        int tilesInRow = difficulty == Difficulty.EASY ? 7 : (difficulty == Difficulty.MEDIUM ? 10 : 13);
        double tileSize = gamePaneWidth / tilesInRow;
        this.difficulty = difficulty;
        if (difficulty == Difficulty.MEDIUM) {
            gameBoard.setDice(new Dice(6, MEDIUM_GAME_QUESTION_PROBABILITY));
        } else if (difficulty == Difficulty.HARD) {
            gameBoard.setDice(new Dice(6, HARD_GAME_QUESTION_PROBABILITY));
        } else {
            // Handle other difficulties or default case
            gameBoard.setDice(new Dice(6, EASY_GAME_QUESTION_PROBABILITY));
        }
        gameBoard.initializePlayerPositions(players);
        LevelLabel.setText("Level: " + difficulty);

        gamepane.setPadding(new Insets(0, 0, 0, 0));

        for (int row= 0; row< tilesInRow; row++) {
            for (int col = 0; col < tilesInRow; col++) {
                int number = calculateTileNumber(row, col, size);
                createTile(number, col, row, size, specialTiles.getOrDefault(number, ""), tileSize);
            }
        }
        gameBoard.initializePlayerPositions(players);
        switch (difficulty) {
            case EASY:
                generateSnakesAndLaddersForEasy(snakes, ladders, occupiedPositions, 48);
                break;
            case MEDIUM:
                generateSnakesAndLaddersForMedium(snakes, ladders, occupiedPositions, bsize);
                break;
            case HARD:
                generateSnakesAndLaddersForHard(snakes, ladders, occupiedPositions, bsize);
                break;
            default:
                throw new IllegalArgumentException("Unrecognized difficulty level");
        }
        setPlayers(players);
        updatePlayerTurn();
        // Assuming you've created and configured diceContainer as shown in your snippet
        diceContainer.getChildren().addAll(diceRollButton, diceImageContainer);

// Now add components to gameDataVBox in a way that centers the dice components vertically
        gameDataVBox.getChildren().clear(); // Clear to ensure it's empty before adding components
        gameDataVBox.getChildren().addAll(timeLabel, levelLabel,currentPlayerLabel,spacerTop, diceContainer, spacerBottom, exitGameButton);



        Platform.runLater(() -> drawLinesForSnakes(snakes));
        Platform.runLater(()->drawLadderOnBoard(ladders));
    }
    @FXML
    void MainMenuFun(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Create a confirmation alert
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.initOwner(stage);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText("Are you sure you want to end the game and back to the main menu?");
        confirmationAlert.setContentText("Any unsaved changes may be lost.");

        // Add OK and Cancel buttons to the alert
        confirmationAlert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        // Show the alert and wait for user input
        confirmationAlert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                // User clicked OK, proceed with transferring to the main menu using SceneUtils for a smooth transition
                SceneUtils.changeScene(stage, "/com/example/snakesandladdersviper/hello-view.fxml", true);
            } else {
                // User clicked Cancel, do nothing or handle accordingly
            }
        });
    }

    private void generateSnakesAndLaddersForEasy(List<Snake> snakes, List<Ladder> ladders, Set<Integer> occupiedPosition, int maxPosition) {
        int minPositionForYellow = 8; // Second row onwards in a 7x7 board
        int minPositionForGreen = 15; // Third row onwards
        int minPositionForBlue = 22; // Fourth row onwards

        generateSnake(snakes, occupiedPositions, 48, "yellow", minPositionForYellow);
        generateSnake(snakes, occupiedPositions, 48, "green", minPositionForGreen);
        generateSnake(snakes, occupiedPositions, 48, "blue", minPositionForBlue);
        generateSnake(snakes, occupiedPositions, 48, "red", 2); // Anywhere except first and last tiles

        for (int i = 1; i <= 4; i++) {
            generateLadder(ladders, occupiedPositions, maxPosition, i);
        }
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
    private HashMap<Integer, List<Integer>> createRowToTilesMap(int boardSize) {
        HashMap<Integer, List<Integer>> rowToTilesMap = new HashMap<>();
        int tileNumber = 1;

        for (int row = 1; row <= globalSize; row++) {
            List<Integer> tilesInRow = new ArrayList<>();
            for (int col = 1; col <= globalSize; col++) {
                tilesInRow.add(tileNumber++);

            }
            rowToTilesMap.put(row, tilesInRow);
        }

        return rowToTilesMap;
    }
    private void generateLadder(List<Ladder> ladders, Set<Integer> occupiedPosition, int maxPosition, int length) {
        Random random = new Random();
        int start = -1, end;
        int startRow=-1, endRow;
        int boardSize = globalSize;
        HashMap<Integer, List<Integer>> rowToTilesMap = createRowToTilesMap(boardSize);
        boolean validStartFound = false;

        while (!validStartFound) {
            // Randomly select a start row
            startRow = random.nextInt(boardSize - length) + 1;
            List<Integer> possibleStartPositions = rowToTilesMap.get(startRow);
            Collections.shuffle(possibleStartPositions);

            for (int possibleStart : possibleStartPositions) {
                if (!occupiedPositions.contains(possibleStart) && possibleStart != 1 && !SpecialTiles.containsKey(possibleStart)) {
                    start = possibleStart;
                    validStartFound = true;
                    break;
                }
            }
        }
        // Determine the end row
        endRow = startRow + length;
        List<Integer> possibleEndPositions = rowToTilesMap.get(endRow);
        // Select a random end position from the possible end positions
        end = possibleEndPositions.get(random.nextInt(possibleEndPositions.size()));
        // Add the ladder with the determined start, end positions, and length
        Ladder newLadder = new Ladder(start, end, length);
        ladders.add(newLadder);
        ladderPositions.put(start,end);
        System.out.println("Added ladder: Start=" + start + ", End=" + end + ", Length=" + length);
        occupiedPositions.add(start);
        if (start != end) {
            occupiedPositions.add(end);
        }
    }

    private Map<Integer, String> generateSpecialTiles(Difficulty difficulty, int size) {
        Map<Integer, String> specialTiles = new HashMap<>();

        return specialTiles;
    }
    private void generateSnakesAndLaddersForMedium(List<Snake> snakes, List<Ladder> ladders, Set<Integer> occupiedPositions, int maxPosition) {
        // Min start positions for snake types
        int minPositionForYellow = 11;
        int minPositionForGreen = 21;
        int minPositionForBlue = 31;
        // No restriction for red snakes

        // Generate 6 snakes
        for (int i = 0; i < 2; i++) {
            generateSnake(snakes, occupiedPositions, 99, "red", 2);
            generateSnake(snakes, occupiedPositions, 99, "green", minPositionForGreen);
        }
        generateSnake(snakes, occupiedPositions, 99, "blue", minPositionForBlue);
        generateSnake(snakes, occupiedPositions, 99, "yellow", minPositionForYellow);

        // Generate 6 ladders of lengths 1 to 6
        for (int i = 1; i <= 6; i++) {
            generateLadder(ladders, occupiedPositions, maxPosition, i);
        }
    }
    // Generate snakes and ladders for the hard difficulty
    private void generateSnakesAndLaddersForHard(List<Snake> snakes, List<Ladder> ladders, Set<Integer> occupiedPositions, int maxPosition) {
        int minPositionForYellow = 14;
        int minPositionForGreen = 27;
        int minPositionForBlue = 40;
        // No restriction for red snakes

        // Generate 8 snakes (2 of each type)
        for (int i = 0; i < 2; i++) {
            generateSnake(snakes, occupiedPositions, 168, "red", 2);
            generateSnake(snakes, occupiedPositions, 168, "green", minPositionForGreen);
            generateSnake(snakes, occupiedPositions, 168, "blue", minPositionForBlue);
            generateSnake(snakes, occupiedPositions, 168, "yellow", minPositionForYellow);
        }

        // Generate 8 ladders of lengths 1 to 8
        for (int i = 1; i <= 8; i++) {
            generateLadder(ladders, occupiedPositions, maxPosition, i);
        }
    }
    private void generateSnake(List<Snake> snakes, Set<Integer> occupiedP, int maxPosition, String type, int minPosition) {
        Random random = new Random();
        int start = -1, end;
        int rowsBack = type.equals("yellow") ? 1 : (type.equals("green") ? 2 : 3);
        int startRow = -1; // Declare startRow outside of the loop
        int boardSize = globalSize;
        HashMap<Integer, List<Integer>> rowToTilesMap = createRowToTilesMap(boardSize);
        boolean validStartFound = false;
        while (!validStartFound) {
            // Randomly select a start row based on the snake type
            startRow = random.nextInt(boardSize - rowsBack) + rowsBack + 1;  // +1 to adjust row index to start from 1
            List<Integer> possibleStartPositions = rowToTilesMap.get(startRow);

            // Shuffle the list for randomness
            Collections.shuffle(possibleStartPositions);

            for (int possibleStart : possibleStartPositions) {
                if (!occupiedPositions.contains(possibleStart) && possibleStart <= maxPosition && !SpecialTiles.containsKey(possibleStart)) {
                    start = possibleStart;
                    validStartFound = true;
                    break;
                }
            }
        }
        // Determine the end row
        int endRow = startRow - rowsBack;
        List<Integer> possibleEndPositions = rowToTilesMap.get(endRow);
        // Select a random end position from the possible end positions
        end = possibleEndPositions.get(random.nextInt(possibleEndPositions.size()));
        // Add the snake with the determined start and end positions
        Snake newSnake = new Snake(start, end, type);
        snakes.add(newSnake);
        snakePositions.put(start, end);

        occupiedPositions.add(start);
        if("red".equals(type)){
            newSnake.setEndPosition(1);
        }
        if (start != end) {
            occupiedPositions.add(end);
        }
        System.out.println("Added " + type + " snake: Start=" + start + ", End=" + end);
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
    public void displayPlayers(List<Player> players) {
        for (Player player : players) {
            Group playerPawn = playerImages.get(player); // Using Group instead of ImageView
            Tile startingTile = getTileByNumber(1); // Assuming player starts at tile 1

            if (startingTile != null && playerPawn != null) {
                // Set position of player pawn
                playerPawn.setLayoutX(startingTile.getX() + startingTile.getWidth() / 2 - 25); // 25 is half the width of the pawn base
                playerPawn.setLayoutY(startingTile.getY() + startingTile.getHeight() / 2 - 25); // 25 is half the height of the pawn base

                contentPane.getChildren().add(playerPawn); // Add player pawn to the contentPane
            } else {
                System.out.println("Starting Tile or Player Pawn is null for player: " + player.getName());
            }
        }
    }

    private void loadPlayerImages() {
        for (Player player : players) {
            String color = player.getPlayerColor();
            if (color != null) {
                // Create a circle for the base of the pawn
                Circle base = new Circle(25); // Radius of 25 for the base
                base.setFill(getColorFromString(color));

                // Create a path for the pointed top of the pawn
                Path top = new Path();
                top.getElements().add(new MoveTo(0, -25));
                top.getElements().add(new LineTo(25, -60)); // Adjust the height of the point
                top.getElements().add(new LineTo(50, -25));
                top.setFill(getColorFromString(color));

                // Combine base and top in a Group
                Group pawn = new Group(base, top);

                // Positioning top relative to the base
                top.setLayoutX(base.getLayoutX() - 25); // Adjust for the width of the base
                top.setLayoutY(base.getLayoutY());

                // Store the pawn in the playerImages map
                playerImages.put(player, pawn);
            } else {
                System.out.println("Color is null for player: " + player.getName());
            }
        }
    }

    private Color getColorFromString(String colorName) {
        switch (colorName.toLowerCase()) {
            case "red":
                return Color.RED;
            case "blue":
                return Color.BLUE;
            case "green":
                return Color.GREEN;
            case "yellow":
                return Color.YELLOW;
            case "orange":
                return Color.ORANGE;
            case "white":
                return Color.WHITE;
            default:
                return Color.BLACK; // Default color or throw an exception
        }
    }

    private String generateRandomNumber(Difficulty difficulty) {
        Random random = new Random();
        // Always return a regular dice roll between 1 and 6, regardless of the difficulty
        return String.valueOf(random.nextInt(6) + 1);
    }
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
    private int calculateTileNumber(int row, int col, int size) {
        int bottomRow = size - 1 - row; // Flip the row index so it starts from the bottom
        if (bottomRow % 2 == 0) {
            // For even rows (from the bottom), numbering goes from left to right
            return (bottomRow * size) + col + 1;
        } else {
            // For odd rows (from the bottom), numbering goes from right to left
            return (bottomRow * size) + (size - 1 - col) + 1;
        }
    }
    @FXML
    private void onDiceRoll() {
        if (!isBotTurn) {

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
                    if (finalNumber == 0) {
                        Image image = new Image("/com/example/snakesandladdersviper/Images/" + finalNumber + ".jpg");
                        ImageView imageView = new ImageView(image);
                        imageView.setFitHeight(150); // Match the size of the diceImageContainer
                        imageView.setFitWidth(150);

                        // Clear previous content and add the new ImageView
                        diceImageContainer.getChildren().clear();
                        diceImageContainer.getChildren().add(imageView);
                        updatePlayerTurn();

                    }
                    if (finalNumber == 1) {
                        Image image = new Image("/com/example/snakesandladdersviper/Images/" + finalNumber + ".jpg");
                        ImageView imageView = new ImageView(image);
                        imageView.setFitHeight(150); // Match the size of the diceImageContainer
                        imageView.setFitWidth(150);

                        // Clear previous content and add the new ImageView
                        diceImageContainer.getChildren().clear();
                        diceImageContainer.getChildren().add(imageView);
                    }
                    if (finalNumber == 2) {
                        Image image = new Image("/com/example/snakesandladdersviper/Images/" + finalNumber + ".jpeg");
                        ImageView imageView = new ImageView(image);
                        imageView.setFitHeight(150); // Match the size of the diceImageContainer
                        imageView.setFitWidth(150);

                        // Clear previous content and add the new ImageView
                        diceImageContainer.getChildren().clear();
                        diceImageContainer.getChildren().add(imageView);
                    }
                    if (finalNumber == 3) {
                        Image image = new Image("/com/example/snakesandladdersviper/Images/" + finalNumber + ".jpeg");
                        ImageView imageView = new ImageView(image);
                        imageView.setFitHeight(150); // Match the size of the diceImageContainer
                        imageView.setFitWidth(150);

                        // Clear previous content and add the new ImageView
                        diceImageContainer.getChildren().clear();
                        diceImageContainer.getChildren().add(imageView);
                    }
                    if (finalNumber == 4) {
                        Image image = new Image("/com/example/snakesandladdersviper/Images/" + finalNumber + ".jpeg");
                        ImageView imageView = new ImageView(image);
                        imageView.setFitHeight(150); // Match the size of the diceImageContainer
                        imageView.setFitWidth(150);

                        // Clear previous content and add the new ImageView
                        diceImageContainer.getChildren().clear();
                        diceImageContainer.getChildren().add(imageView);
                    }
                    if (finalNumber == 5) {
                        Image image = new Image("/com/example/snakesandladdersviper/Images/" + finalNumber + ".jpeg");
                        ImageView imageView = new ImageView(image);
                        imageView.setFitHeight(150); // Match the size of the diceImageContainer
                        imageView.setFitWidth(150);

                        // Clear previous content and add the new ImageView
                        diceImageContainer.getChildren().clear();
                        diceImageContainer.getChildren().add(imageView);
                    }
                    if (finalNumber == 6) {
                        Image image = new Image("/com/example/snakesandladdersviper/Images/" + finalNumber + ".jpg");
                        ImageView imageView = new ImageView(image);
                        imageView.setFitHeight(150); // Match the size of the diceImageContainer
                        imageView.setFitWidth(150);

                        // Clear previous content and add the new ImageView
                        diceImageContainer.getChildren().clear();
                        diceImageContainer.getChildren().add(imageView);
                    }
                    // Handle the number outcome (e.g., move the player)
                    movePlayer(finalNumber);
                    // updatePlayerTurn();
                } else {
                    // If it's not a number, it must be a question or another string outcome
                    Image image = new Image("/com/example/snakesandladdersviper/Images/QuestionIcon.jpg");
                    ImageView imageView = new ImageView(image);
                    imageView.setFitHeight(150); // Match the size of the diceImageContainer
                    imageView.setFitWidth(150);
                    // Clear previous content and add the new ImageView
                    diceImageContainer.getChildren().clear();
                    diceImageContainer.getChildren().add(imageView);
                }
            });
        }
    }
    private void botsTurn() {
        new Thread(() -> {
            // Skip the human player at index 0, start with bots
            for (int i = 2; i < players.size()+1; i++) {
                final int currentPlayerIndex = i; // To be used in lambda
                try {
                    // Delay for visual effect
                    Thread.sleep(3000);
                    // Run on JavaFX thread
                    Platform.runLater(() -> {
                        Player currentPlayer = players.get(currentPlayerIndex);

                        // Simulate dice roll for bot
                        String diceOutcome = generateRandomNumber(difficulty);
                        int diceNumber = Integer.parseInt(diceOutcome); // Assuming it's always a number for bots
                        updateDiceImage(diceNumber); // Update dice image on the screen
                        processDiceRollOutcome(diceOutcome, currentPlayer);
                    });
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Thread interrupted: " + e.getMessage());
                }
            }
            Platform.runLater(() -> {isBotTurn = false;
                updatePlayerTurn(1);

            });

        }).start();
    }
    private void updateDiceImage(int diceNumber) {

        if (diceNumber == 0) {
            Image image = new Image("/com/example/snakesandladdersviper/Images/" + diceNumber + ".jpg");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(150); // Match the size of the diceImageContainer
            imageView.setFitWidth(150);

            // Clear previous content and add the new ImageView
            diceImageContainer.getChildren().clear();
            diceImageContainer.getChildren().add(imageView);
            updatePlayerTurn();

        }
        if (diceNumber == 1) {
            Image image = new Image("/com/example/snakesandladdersviper/Images/" + diceNumber + ".jpg");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(150); // Match the size of the diceImageContainer
            imageView.setFitWidth(150);

            // Clear previous content and add the new ImageView
            diceImageContainer.getChildren().clear();
            diceImageContainer.getChildren().add(imageView);
        }
        if (diceNumber == 2) {
            Image image = new Image("/com/example/snakesandladdersviper/Images/" + diceNumber + ".jpeg");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(150); // Match the size of the diceImageContainer
            imageView.setFitWidth(150);

            // Clear previous content and add the new ImageView
            diceImageContainer.getChildren().clear();
            diceImageContainer.getChildren().add(imageView);
        }
        if (diceNumber == 3) {
            Image image = new Image("/com/example/snakesandladdersviper/Images/" + diceNumber + ".jpeg");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(150); // Match the size of the diceImageContainer
            imageView.setFitWidth(150);

            // Clear previous content and add the new ImageView
            diceImageContainer.getChildren().clear();
            diceImageContainer.getChildren().add(imageView);
        }
        if (diceNumber == 4) {
            Image image = new Image("/com/example/snakesandladdersviper/Images/" + diceNumber + ".jpeg");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(150); // Match the size of the diceImageContainer
            imageView.setFitWidth(150);

            // Clear previous content and add the new ImageView
            diceImageContainer.getChildren().clear();
            diceImageContainer.getChildren().add(imageView);
        }
        if (diceNumber == 5) {
            Image image = new Image("/com/example/snakesandladdersviper/Images/" + diceNumber + ".jpeg");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(150); // Match the size of the diceImageContainer
            imageView.setFitWidth(150);

            // Clear previous content and add the new ImageView
            diceImageContainer.getChildren().clear();
            diceImageContainer.getChildren().add(imageView);
        }
        if (diceNumber == 6) {
            Image image = new Image("/com/example/snakesandladdersviper/Images/" + diceNumber + ".jpg");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(150); // Match the size of the diceImageContainer
            imageView.setFitWidth(150);

            // Clear previous content and add the new ImageView
            diceImageContainer.getChildren().clear();
            diceImageContainer.getChildren().add(imageView);
        }
        String imagePath = "/com/example/snakesandladdersviper/Images/" + diceNumber + (diceNumber == 6 ? ".jpg" : ".jpeg");
        Image image = new Image(imagePath);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(150); // Match the size of the diceImageContainer
        imageView.setFitWidth(150);

        // Clear previous content and add the new ImageView
        diceImageContainer.getChildren().clear();
        diceImageContainer.getChildren().add(imageView);
    }
    private void processDiceRollOutcome(String outcome, Player player) {
        if (isNumeric(outcome)) {
            int number = Integer.parseInt(outcome);
            movePlayer(player, number);
        }
    }
    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private void updatePlayerTurn(int i) {
        if (players == null || players.isEmpty()) {
            System.out.println("Player list is empty or not initialized.");
            return;
        }
        // Increment the currentPlayerIndex and wrap around if it exceeds the size of the players list
        currentPlayerIndex = 1;
        System.out.println(currentPlayerIndex + " In updatePlayerTurn");

        // Get the current player based on the updated index
        Player currentPlayer = players.get(currentPlayerIndex);

        // Update the currentPlayerLabel with the current player's name
        if (currentPlayer != null) {
            currentPlayerLabel.setText("Player " + currentPlayer.getPlayerColor() + "'s Turn ");
        } else {
            System.out.println("Current player is null.");
        }
    }
    private void updatePlayerTurn() {
        if (players == null || players.isEmpty()) {
            System.out.println("Player list is empty or not initialized.");
            return;
        }
        // Increment the currentPlayerIndex and wrap around if it exceeds the size of the players list
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        System.out.println(currentPlayerIndex + " In updatePlayerTurn");

        // Get the current player based on the updated index
        Player currentPlayer = players.get(currentPlayerIndex);

        // Update the currentPlayerLabel with the current player's name
        if (currentPlayer != null) {
            currentPlayerLabel.setText("Player " + currentPlayer.getPlayerColor() + "'s Turn ");
        } else {
            System.out.println("Current player is null.");
        }
    }
    private void movePlayer(int steps) {
        Player currentPlayer = players.get(currentPlayerIndex);
        System.out.println(currentPlayerIndex + " in movePlayer");
        int newPosition = gameBoard.getPlayerPosition(currentPlayer) + steps;
        int totalTiles = getTotalTilesForDifficulty(difficulty);
        int temp = newPosition;
        if (steps == 0) {
            updatePlayerTurn();
        } else {
            if (newPosition < 1) {
                newPosition = 1;
            } else if (newPosition > totalTiles) {
                newPosition = totalTiles;
                handlePlayerWin(currentPlayer);
            }
            String tileColor = SpecialTiles.get(newPosition);
            if (tileColor != null) {
                // Special tile logic (questions, etc.)
                //handleSpecialTile(newPosition, tileColor);
            } else if (snakePositions.containsKey(newPosition)) {
                newPosition = snakePositions.get(newPosition);
                System.out.println("Player landed on a snake! Moved to position " + newPosition);
                temp = checkNewPosition(newPosition);
            } else if (ladderPositions.containsKey(newPosition)) {
                newPosition = ladderPositions.get(newPosition);
                System.out.println("Player landed on a ladder! Moved to position " + newPosition);
                temp = checkNewPosition(newPosition);
            }
        }
        if (temp != newPosition) {
            newPosition = temp;
        }
        gameBoard.setPlayerPosition(currentPlayer, newPosition);
        updatePlayerPositionOnBoard(currentPlayer);
        if (newPosition >= totalTiles) {
            handlePlayerWin(currentPlayer);
        } else {
            this.isBotTurn = true;
            botsTurn();

        }
    }
    public int checkNewPosition(int newPosition){
        boolean temp = true;
        while(temp) {
            temp = false;
            String tileColor = SpecialTiles.get(newPosition);

            if (snakePositions.containsKey(newPosition)) {
                System.out.println("Player landed on a snake! Moved to position " + newPosition);
                newPosition = snakePositions.get(newPosition);
                temp = true;
            }
            else if (ladderPositions.containsKey(newPosition)) {
                newPosition = ladderPositions.get(newPosition);
                System.out.println("Player landed on a ladder! Moved to position " + newPosition);
                temp = true;
            }
        }
        return newPosition;
    }
    private void movePlayer(Player player, int steps) {
        System.out.println(player.getName() + " in movePlayer");
        int newPosition = gameBoard.getPlayerPosition(player) + steps;
        int totalTiles = getTotalTilesForDifficulty(difficulty);
        int temp = newPosition;

        if (steps == 0) {
            // If no movement, just update the turn
            updatePlayerTurn();
        } else {
            // Handle movement and collisions with special tiles
            if (newPosition < 1) {
                newPosition = 1;
            } else if (newPosition > totalTiles) {
                newPosition = totalTiles;
                handlePlayerWin(player);
            }

            String tileColor = SpecialTiles.get(newPosition);
            if (tileColor != null) {
                // Special tile logic
                // handleSpecialTile(newPosition, tileColor);
            } else if (snakePositions.containsKey(newPosition)) {
                newPosition = snakePositions.get(newPosition);
                System.out.println(player.getName() + " landed on a snake! Moved to position " + newPosition);
                temp = checkNewPosition(newPosition);
            } else if (ladderPositions.containsKey(newPosition)) {
                newPosition = ladderPositions.get(newPosition);
                System.out.println(player.getName() + " landed on a ladder! Moved to position " + newPosition);
                temp = checkNewPosition(newPosition);
            }

            if (temp != newPosition) {
                newPosition = temp;
            }

            gameBoard.setPlayerPosition(player, newPosition);
            updatePlayerPositionOnBoard(player);

            if (newPosition >= totalTiles) {
                handlePlayerWin(player);
            } else {
                updatePlayerTurn();
            }
        }
    }

    private void handlePlayerWin(Player currentPlayer) {
        // Get the game duration as a string
        String gameDurationStr = timeLabel.getText();
       // long gameDurationMillis = convertDurationToMillis(gameDurationStr);
        // Difficulty of the match
        String gameDifficulty = difficulty.toString();
        // Add the game data to history.json
       // SysData.getInstance().addGameHistory(currentPlayer.getName(), gameDurationStr, difficulty.toString());

        Platform.runLater(() -> {
            // Capture the current full screen state
            Stage stage = (Stage) contentPane.getScene().getWindow();
            boolean wasFullScreen = stage.isFullScreen();

            Alert winAlert = new Alert(Alert.AlertType.INFORMATION);
            winAlert.setTitle("Congratulations!");
            winAlert.setHeaderText(null);
            winAlert.setContentText(currentPlayer.getName() + " has won the game!");

            // Add a "Home Screen" button
            ButtonType homeScreenButton = new ButtonType("End Game", ButtonBar.ButtonData.OK_DONE);
            winAlert.getButtonTypes().setAll(homeScreenButton);

            // Temporarily exit full screen to show the alert, then restore the state
            stage.setFullScreen(false);
            winAlert.showAndWait().ifPresent(response -> {
                if (response == homeScreenButton) {
                    // Navigate to the home screen
                    SceneUtils.changeScene(stage, "/com/example/snakesandladdersviper/hello-view.fxml", wasFullScreen);
                }
            });

            // Restore the full-screen mode after the alert is dismissed
            stage.setFullScreen(wasFullScreen);
        });
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
    private void updatePlayerPositionOnBoard(Player player) {
        int newPosition = gameBoard.getPlayerPosition(player);

        // Convert this position to row and column on the grid
        int size = determineBoardSize(difficulty);
        int row = size - 1 - (newPosition / size);
        int column = (row % 2 == size % 2) ? size - 1 - (newPosition % size) : newPosition % size;

        // Find the corresponding tile
        Tile newTile = getTileByNumber(newPosition);

        if (newTile != null) {
            Group playerPawn = playerImages.get(player);
            double newX = newTile.getX() + newTile.getWidth() / 2 - 25; // 25 is half the width of the pawn base
            double newY = newTile.getY() + newTile.getHeight() / 2 - 25; // 25 is half the height of the pawn base

            // Create and play the animation
            TranslateTransition transition = new TranslateTransition(Duration.seconds(1), playerPawn);
            transition.setToX(newX - playerPawn.getLayoutX());
            transition.setToY(newY - playerPawn.getLayoutY());
            transition.play();
        }
    }
    private Tile getTileByNumber(int tileNumber) {
        for (Node node : contentPane.getChildren()) { // If tiles are directly added to the gamepane
            if (node instanceof Tile) {
                Tile tile = (Tile) node;
                if (tile.getNumber() == tileNumber) {
                    return tile;
                }
            }
        }
        return null; // Return null if no matching tile is found
    }
    private int determineBoardSize(Difficulty difficulty) {
        switch (difficulty) {
            case EASY:
                return 7;
            case MEDIUM:
                return 10;
            case HARD:
                return 13;
            default:
                throw new IllegalArgumentException("Unrecognized difficulty level");
        }
    }


}
