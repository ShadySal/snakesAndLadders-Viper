package com.example.snakesandladdersviper.Controller;
import com.example.snakesandladdersviper.Model.*;
import com.example.snakesandladdersviper.Enums.Difficulty;
import com.example.snakesandladdersviper.Utils.SceneUtils;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Consumer;

public class GameBoardController {

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
//    private void setupGameDataPane() {
//        // Create a VBox for the game data pane
//        VBox gameDataPane = new VBox(10); // 10 is the spacing between elements
//        gameDataPane.setPadding(new Insets(10)); // Padding around the pane
//
//        // Create the UI elements (labels, button, etc.) as per your data
//
//        Label levelLabel = new Label("Level: " + difficulty);
//        Button mainMenuButton = new Button("Exit Game");
//        Pane diceImageContainer = new Pane();
//        Button diceRollButton = new Button("Roll Dice");
//        Label currentPlayerLabel = new Label("Current Player");
//
//        // Set preferred sizes for the pane and elements, if needed
//        gameDataPane.setPrefWidth(200); // Example width, adjust as needed
//        diceImageContainer.setPrefSize(150, 150); // Example size for dice image container
//
//        // Add elements to the game data pane
//        gameDataPane.getChildren().addAll(timeLabel, levelLabel, currentPlayerLabel, diceRollButton, diceImageContainer, mainMenuButton);
//
//        // Position the game data pane to the right of the board
//        HBox rootContainer = new HBox();
//        rootContainer.getChildren().addAll(BoardGrid, gameDataPane); // Assuming BoardGrid is your game board
//
//        // Set the main game scene or container to include this new layout
//        gamepane.getChildren().add(rootContainer); // Assuming gamepane is the parent container
//    }
private void loadPlayerImages() {
    double scale = difficulty == Difficulty.HARD ? 0.5 : 1.0;
    for (Player player : players) {
        String color = player.getPlayerColor();
        if (color != null) {
            // Adjust size based on the scale
            double baseRadius = 25 * scale;
            double topHeight = 60 * scale;
            double topWidth = 50 * scale;

            // Create a circle for the base of the pawn
            Circle base = new Circle(baseRadius);
            base.setFill(getColorFromString(color));

            // Create a path for the pointed top of the pawn
            Path top = new Path();
            top.getElements().add(new MoveTo(0, -baseRadius));
            top.getElements().add(new LineTo(topWidth / 2, -topHeight));
            top.getElements().add(new LineTo(topWidth, -baseRadius));
            top.setFill(getColorFromString(color));

            // Combine base and top in a Group
            Group pawn = new Group(base, top);

            // Positioning top relative to the base
            top.setLayoutX(base.getLayoutX() - topWidth / 2);
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
            case "pink":
                return Color.PINK;
            case "yellow":
                return Color.YELLOW;
            case "orange":
                return Color.ORANGE;
            case "green":
                return Color.GREEN;
            default:
                return Color.BLACK; // Default color or throw an exception
        }
    }
//    private void loadPlayerImages() {
//        for (Player player : players) {
//            String color = player.getPlayerColor();
//            if (color != null) {
//                String imageFileName = "/com/example/snakesandladdersviper/Images/" + color + ".png";
//                InputStream is = getClass().getResourceAsStream(imageFileName);
//                if (is != null) {
//                    Image image = new Image(is);
//                    ImageView imageView = new ImageView(image);
//                    imageView.setFitHeight(50); // Set size as needed
//                    imageView.setFitWidth(50);
//                    playerImages.put(player, imageView);
//                } else {
//                    System.out.println("Image file not found: " + imageFileName);
//                }
//            } else {
//                System.out.println("Color is null for player: " + player.getName());
//            }
//        }
//    }

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
        if (difficulty == Difficulty.EASY) {
            // Logic for EASY difficulty remains as is.
            int maxRoll = 7; // 0-4 for numbers, 5 for easy question, 6 for medium question, 7 for hard question
            int rollResult = random.nextInt(maxRoll + 1);
            switch (rollResult) {
                case 5: return "EASY_QUESTION";
                case 6: return "MEDIUM_QUESTION";
                case 7: return "HARD_QUESTION";
                default: return String.valueOf(rollResult);
            }
        } else if (difficulty == Difficulty.MEDIUM) {
            // MEDIUM difficulty: 50% chance for a question.
            if (random.nextBoolean()) {
                int questionType = random.nextInt(3);
                if (questionType == 0) return "EASY_QUESTION";
                else if (questionType == 1) return "MEDIUM_QUESTION";
                else return "HARD_QUESTION";
            } else {
                return String.valueOf(random.nextInt(6) + 1); // Regular dice roll
            }
        } else if (difficulty == Difficulty.HARD) {
            // HARD difficulty: 25% chance for hard question, 25% for easy/medium question, 50% for regular number.
            double chance = random.nextDouble();
            if (chance < 0.25) {
                return "HARD_QUESTION"; // 25% chance for a hard question
            } else if (chance < 0.5) {
                return random.nextBoolean() ? "EASY_QUESTION" : "MEDIUM_QUESTION"; // 25% chance for easy or medium question
            } else {
                return String.valueOf(random.nextInt(6) + 1); // Regular dice roll
            }
        }
        return "Invalid difficulty"; // Default case
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

        private void createTile(int number, int col, int row, int size, String specialTileColor, double tileSize) {
            Tile tile = new Tile(col, size - row - 1);
            tile.setNumber(number);
            tile.setWidth(tileSize);
            tile.setHeight(tileSize);
            tile.setX(col * tileSize*0.99); // Position the tile based on column
            tile.setY(row * tileSize*0.99); // Position the tile based on row

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

    // Generate snakes and ladders for the easy difficulty

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
    // Generate snakes and ladders for the medium difficulty
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

    // Helper method to generate a single snake
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




    private void addSnakeHead(double x, double y, Color color) {
        // Assuming headSize is relative to your grid/tile size
        double headSize = 10;

        // Create the head shape with SVGPath for more complexity and control
        SVGPath headShape = new SVGPath();
        // This path data should represent a snake head; adjust as needed
        headShape.setContent("M " + (x - headSize) + " " + y +
                " Q " + (x - headSize / 2) + " " + (y - headSize) +
                " " + x + " " + y +
                " Q " + (x + headSize / 2) + " " + (y - headSize) +
                " " + (x + headSize) + " " + y +
                " Q " + (x + headSize / 2) + " " + (y + headSize / 2) +
                " " + x + " " + y +
                " Q " + (x - headSize / 2) + " " + (y + headSize / 2) +
                " " + (x - headSize) + " " + y + " Z");
        headShape.setFill(color);

        // Add eyes to the snake head
        Circle leftEye = new Circle(x - headSize / 3, y - headSize / 2, 1, Color.WHITE);
        Circle rightEye = new Circle(x + headSize / 3, y - headSize / 2, 1, Color.WHITE);

        // Add a tongue if desired
        SVGPath tongue = new SVGPath();
        tongue.setContent("M " + x + " " + y + " L " + (x - 2) + " " + (y + 4) + " L " + x + " " + (y + 2) + " L " + (x + 2) + " " + (y + 4) + " Z");
        tongue.setFill(Color.RED);

        contentPane.getChildren().addAll(headShape, leftEye, rightEye, tongue);
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
    // Helper method to generate a single ladder
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



    private int getRowForPosition(int position) {
        int size = determineBoardSize(difficulty); // Assuming this returns the size of one side of the board
        return (position - 1) / size; // Row index, starts from 0
    }

    private int getColumnForPosition(int position) {
        int size = determineBoardSize(difficulty); // Assuming this returns the size of one side of the board
        int row = getRowForPosition(position);
        int colIndex = (position - 1) % size;

        // Adjust for reverse numbering in alternate rows
        if (row % 2 == 0) {
            // For even rows, numbering goes from left to right
            return colIndex;
        } else {
            // For odd rows, numbering goes from right to left
            return size - 1 - colIndex;
        }
    }
    private void placeSnakeOnBoard(Snake snake) {
//        Pane startTile = getTileByNumber(snake.getStart());
//        if (startTile != null) {
//            ImageView startSnakeImage = loadSnakeImage(snake.getType());
//            startTile.getChildren().add(startSnakeImage);
//        }
//
//        // If the snake is not red, place the end image as well
//        if (!snake.getType().equals("red")) {
//            Pane endTile = getTileByNumber(snake.getEndPosition());
//            if (endTile != null) {
//                ImageView endSnakeImage = loadSnakeImage(snake.getType());
//                endTile.getChildren().add(endSnakeImage);
//            }
//        }
    }

    private ImageView loadSnakeImage(String snakeType) {
        String snakeImageName = chooseSnakeImage(snakeType);
        try {
            InputStream is = getClass().getResourceAsStream("/com/example/snakesandladdersviper/Images/" + snakeImageName);
            if (is != null) {
                Image snakeImage = new Image(is);
                ImageView snakeImageView = new ImageView(snakeImage);

                // Adjust the size of the snake image to fit the tile
                double tileWidth = BoardGrid.getColumnConstraints().get(0).getPrefWidth();
                double tileHeight = BoardGrid.getRowConstraints().get(0).getPrefHeight();
                snakeImageView.setFitWidth(tileWidth);
                snakeImageView.setFitHeight(tileHeight);
                snakeImageView.setPreserveRatio(true);

                return snakeImageView;
            }
        } catch (Exception e) {
            System.out.println("Error loading snake image: " + e.getMessage());
        }
        return null; // Return null if the image is not found or an error occurs
    }

    private String chooseSnakeImage(String snakeType) {
        switch (snakeType) {
            case "yellow":
                return "YellowSnake.png";
            case "green":
                return "GreenSnake.png";
            case "blue":
                return "BlueSnake.png";
            case "red":
                return "RedSnake.png";
            default:
                return "DefaultSnake.png"; // Default case if no type matches
        }
    }
    private Pane getTilePane(int row, int col) {
        for (Node node : BoardGrid.getChildren()) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
                return (Pane) node;
            }
        }
        return null; // Return null if no matching pane is found
    }
    private void drawLineBetweenCircles(Circle startCircle, Circle endCircle, Color color) {
        Point2D startPos = getCenterPositionOfCircle(startCircle);
        Point2D endPos = getCenterPositionOfCircle(endCircle);

        Line line = new Line(startPos.getX(), startPos.getY(), endPos.getX(), endPos.getY());
        line.setStroke(color);

        // Add the line to the board
        BoardGrid.getChildren().add(line);
    }
    private Point2D getCenterPositionOfCircle(Circle circle) {
        // Assuming each cell of the GridPane has equal width and height
        double tileWidth = BoardGrid.getColumnConstraints().get(0).getPrefWidth();
        double tileHeight = BoardGrid.getRowConstraints().get(0).getPrefHeight();

        double centerX = (globalSize * tileWidth) + circle.getRadius();
        double centerY = (globalSize * tileHeight) + circle.getRadius();

        return new Point2D(centerX, centerY);
    }
    private Point2D getCenterPosition(int row, int column) {
        double x = column * 125 + 125 / 2;
        double y = row * 125 + 125 / 2;
        return new Point2D(x, y);
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


    private void placeLadderOnBoard(Ladder ladder) {
//        // Load ladder images based on the ladder's length
//        String ladderImageName = "ladder" + ladder.getLength() + ".png";
//        InputStream is = getClass().getResourceAsStream("/com/example/snakesandladdersviper/Images/" + ladderImageName);
//        if (is != null) {
//            Image ladderImage = new Image(is);
//            ImageView startLadderImage = new ImageView(ladderImage);
//            ImageView endLadderImage = new ImageView(ladderImage);
//
//            // Adjust the size of the ladder image to fit the tile
//            double tileWidth = BoardGrid.getColumnConstraints().get(0).getPrefWidth();
//            double tileHeight = BoardGrid.getRowConstraints().get(0).getPrefHeight();
//            startLadderImage.setFitWidth(tileWidth);
//            startLadderImage.setFitHeight(tileHeight);
//            startLadderImage.setPreserveRatio(true);
//            endLadderImage.setFitWidth(tileWidth);
//            endLadderImage.setFitHeight(tileHeight);
//            endLadderImage.setPreserveRatio(true);
//
//            // Place ladder image at the start position
//            Pane startTile = getTileByNumber(ladder.getStart());
//            if (startTile != null) {
//                startTile.getChildren().add(startLadderImage);
//            }
//
//            // Place ladder image at the end position
//            Pane endTile = getTileByNumber(ladder.getEnd());
//            if (endTile != null) {
//                endTile.getChildren().add(endLadderImage);
//            }
//        } else {
//            System.out.println("Image file not found: " + ladderImageName);
//        }
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

    private Map<Integer, String> generateSpecialTiles(Difficulty difficulty, int size) {
        Map<Integer, String> specialTiles = new HashMap<>();
        Random random = new Random();
        Set<Integer> usedTiles = new HashSet<>();

        // Define colors for question tiles
        String[] questionColors = {"red", "green", "yellow"};
        for (int i = 0; i < questionColors.length; i++) {
            int tile = random.nextInt(size * size - 1) + 2; // Start from 2 to avoid the first tile
            while (usedTiles.contains(tile) || tile == size * size) { // Avoid the last tile
                tile = random.nextInt(size * size - 1) + 2;
            }
            specialTiles.put(tile, questionColors[i]);
            usedTiles.add(tile);
            SpecialTiles.put(tile, questionColors[i]);
        }

        if (difficulty != Difficulty.EASY) {
            int startTile = (size * size) - 10;
            int endTile = size * size - 1; // Avoid the last tile
            int surpriseTilesCount = (difficulty == Difficulty.MEDIUM) ? 1 : 2;

            for (int i = 0; i < surpriseTilesCount; i++) {
                int tile = random.nextInt(endTile - startTile + 1) + startTile;
                while (usedTiles.contains(tile)) {
                    tile = random.nextInt(endTile - startTile + 1) + startTile;
                }
                specialTiles.put(tile, "blue");
                usedTiles.add(tile);
               // occupiedPositions.add(tile);
            }
        }
        return specialTiles;
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

    //display players on Gameboard

    public void displayPlayers(List<Player> players) {
        for (Player player : players) {
            Group playerPawn = playerImages.get(player); // Using Group instead of ImageView
            Tile startingTile = getTileByNumber(1); // Assuming player starts at tile 1

            if (startingTile != null && playerPawn != null) {
                // Set position of player pawn
                if(this.difficulty ==Difficulty.HARD ){
                    playerPawn.setLayoutX(startingTile.getX() + startingTile.getWidth() / 2 - 25); // 25 is half the width of the pawn base
                    playerPawn.setLayoutY(startingTile.getY() + startingTile.getHeight() / 2 - 25);
                }
                else {
                    playerPawn.setLayoutX(startingTile.getX() + startingTile.getWidth() / 2 - 25); // 25 is half the width of the pawn base
                    playerPawn.setLayoutY(startingTile.getY() + startingTile.getHeight() / 2 - 25); // 25 is half the height of the pawn base
                }
                contentPane.getChildren().add(playerPawn); // Add player pawn to the contentPane
            } else {
                System.out.println("Starting Tile or Player Pawn is null for player: " + player.getName());
            }
        }
    }
//    public void displayPlayers(List<Player> players) {
//        for (Player player : players) {
//            // Define colors and gradients
//            Color color = Color.web(player.getPlayerColor());
//            LinearGradient gradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
//                    new Stop(0, color.darker()), new Stop(1, color.brighter()));
//
//            // Head of the pawn, using an ellipse to create a 3D effect
//            Circle head = new Circle(10); // Adjust size as needed
//            head.setFill(gradient);
//
//            // Body of the pawn, using an ellipse for a 3D bulbous effect
//            Ellipse body = new Ellipse(15, 20); // Adjust width and height as needed
//            body.setFill(gradient);
//
//            // Base of the pawn, to give a solid standing effect
//            Ellipse base = new Ellipse(20, 5); // Adjust width and height for a flatter ellipse
//            base.setFill(gradient.darker());
//
//            // Positioning and adjustments
//            head.setCenterX(0);
//            head.setCenterY(-30); // Position the head above the body
//            body.setCenterX(0);
//            body.setCenterY(0); // Center body
//            base.setCenterX(0);
//            base.setCenterY(20); // Position base below the body
//
//            // Adding shadow for a more lifelike 3D effect
//            DropShadow shadow = new DropShadow();
//            shadow.setRadius(5.0);
//            shadow.setColor(Color.color(0.4, 0.4, 0.4));
//
//            Group pawn = new Group(head, body, base);
//            pawn.setEffect(shadow); // Apply shadow to the whole pawn
//
//            // Adjust group position based on player's current tile or starting position
//            Tile startingTile = getTileByNumber(1); // Example, replace with actual logic
//            if (startingTile != null) {
//                pawn.setLayoutX(startingTile.getX() + startingTile.getWidth() / 2);
//                pawn.setLayoutY(startingTile.getY() + startingTile.getHeight() / 2 - 20); // Adjust Y offset as needed
//                contentPane.getChildren().add(pawn); // Add the pawn to the content pane
//            } else {
//                System.out.println("Starting Tile is null for player: " + player.getName());
//            }
//        }
//    }
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
        if (outcome.equals("EASY_QUESTION")) {
            askQuestion("easy", isCorrect -> movePlayerAndUpdateTurn(isCorrect ? 0 : -1));
        } else if (outcome.equals("MEDIUM_QUESTION")) {
            askQuestion("medium", isCorrect -> movePlayerAndUpdateTurn(isCorrect ? 0 : -2));
        } else if (outcome.equals("HARD_QUESTION")) {
            askQuestion("hard", isCorrect -> movePlayerAndUpdateTurn(isCorrect ? 1 : -3));
        } else {
            int steps = Integer.parseInt(outcome);
            movePlayer(steps);
        }
    }
    private void movePlayerAndUpdateTurn(int steps) {

        if (steps == 0) {
            updatePlayerTurn();
        }
        else{
            movePlayer(steps);
        }
    }
    private void handleQuestionOutcome(boolean isCorrect, String difficulty) {
        Player currentPlayer = players.get(currentPlayerIndex);

        // Determine the number of steps to move based on the difficulty and correctness of the answer
        int steps;
        if (isCorrect) {
            if (difficulty.equals("HARD_QUESTION")) {
                steps = 1; // Move forward one step for a correct hard question
            } else {
                steps = 0; // No movement for correct answers to easy or medium questions
            }
        } else {
            if (difficulty.equals("HARD_QUESTION")) {
                steps = -3; // Move back three steps for an incorrect hard question
            } else if (difficulty.equals("MEDIUM_QUESTION")) {
                steps = -2; // Move back two steps for an incorrect medium question
            } else {
                steps = -1; // Move back one step for an incorrect easy question
            }
        }

        // Calculate the new position
        int currentPosition = gameBoard.getPlayerPosition(currentPlayer);
        int newPosition = currentPosition + steps;

        // Ensure the new position does not go below 1
        newPosition = Math.max(1, newPosition);

        // Update the player's position on the game board
        gameBoard.setPlayerPosition(currentPlayer, newPosition);

        // Update the UI to reflect the new position
        updatePlayerPositionOnBoard(currentPlayer);
    }
    private void askQuestion(String difficultyLevel, Consumer<Boolean> callback) {
        int difficulty = convertDifficultyLevelToNumber(difficultyLevel);
        Question question = SysData.getInstance().getRandomQuestion(difficulty);

        if (question == null) {
            showAlert("No Questions", "No questions available for this difficulty.");
            callback.accept(false); // No question available, treat as incorrect
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
        Stage primaryStage = (Stage) contentPane.getScene().getWindow(); // Replace 'BoardGrid' with an actual component from your primary stage
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
        Stage primaryStage = (Stage) contentPane.getScene().getWindow(); // Replace 'BoardGrid' with an actual component from your primary stage
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
                // Handle the string outcome (like "EASY_QUESTION")
                processDiceOutcome(finalOutcome);
            }

            // Update the player turn for the next move

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

    private void moveB(int steps){
        Player currentPlayer = players.get(currentPlayerIndex);
        System.out.println(currentPlayer + " in movePlayer");
        int newPosition = gameBoard.getPlayerPosition(currentPlayer) + steps;
        int totalTiles = getTotalTilesForDifficulty(difficulty);
        int temp = newPosition;
        if(steps == 0){
            updatePlayerTurn();
        }
        else {
            if (newPosition < 1) {
                newPosition = 1;
            } else if (newPosition > totalTiles) {
                newPosition = totalTiles;
                handlePlayerWin(currentPlayer);
            }
            String tileColor = SpecialTiles.get(newPosition);
            if (tileColor != null) {
                // Special tile logic (questions, etc.)
                handleSpecialTile(newPosition, tileColor);
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
        if (temp != newPosition){
            newPosition = temp;
        }
        gameBoard.setPlayerPosition(currentPlayer, newPosition);
        updatePlayerPositionOnBoard(currentPlayer);
        if (newPosition >= totalTiles) {
            handlePlayerWin(currentPlayer);
        } else {
            updatePlayerTurn();
        }
    }
    public int checkNewPosition(int newPosition){
        boolean temp = true;
        while(temp) {
            temp = false;
            String tileColor = SpecialTiles.get(newPosition);
            if (tileColor != null) {
                // Special tile logic (questions, etc.)
                handleSpecialTile(newPosition, tileColor);
                break;
            }
           else if (snakePositions.containsKey(newPosition)) {
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
    private void movePlayer(int steps) {

        Player currentPlayer = players.get(currentPlayerIndex);
        System.out.println(currentPlayerIndex + " in movePlayer");
        int newPosition = gameBoard.getPlayerPosition(currentPlayer) + steps;
        int totalTiles = getTotalTilesForDifficulty(difficulty);
        int temp = newPosition;
        if(steps == 0){
            updatePlayerTurn();
        }
        else {
            if (newPosition < 1) {
                newPosition = 1;
            } else if (newPosition > totalTiles) {
                newPosition = totalTiles;
                handlePlayerWin(currentPlayer);
            }
            String tileColor = SpecialTiles.get(newPosition);
            if (tileColor != null) {
                // Special tile logic (questions, etc.)
                handleSpecialTile(newPosition, tileColor);
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
        if (temp != newPosition){
            newPosition = temp;
        }
        gameBoard.setPlayerPosition(currentPlayer, newPosition);
        updatePlayerPositionOnBoard(currentPlayer);
        if (newPosition >= totalTiles) {
            handlePlayerWin(currentPlayer);
        } else {
            updatePlayerTurn();
        }

//        Player currentPlayer = players.get(currentPlayerIndex);
//        System.out.println(currentPlayerIndex + " in movePlayer");
//        int newPosition = gameBoard.getPlayerPosition(currentPlayer) + steps;
//        int totalTiles = getTotalTilesForDifficulty(difficulty);
//        boolean moved = true;
//        if(steps == 0){
//            updatePlayerTurn();
//        }
//        else {
//            while (moved) {
//                moved = false;
//                // Check for negative movement or going beyond the board
//                if (newPosition < 1) {
//                    newPosition = 1;
//                } else if (newPosition > totalTiles) {
//                    newPosition = totalTiles;
//                }
//
//                // Check for special tiles, snakes, and ladders
//                String tileColor = SpecialTiles.get(newPosition);
//                if (tileColor != null) {
//                    // Special tile logic (questions, etc.)
//                    handleSpecialTile(newPosition, tileColor);
//                } else if (snakePositions.containsKey(newPosition)) {
//                    newPosition = snakePositions.get(newPosition);
//                    System.out.println("Player landed on a snake! Moved to position " + newPosition);
//                    moved = true;
//                } else if (ladderPositions.containsKey(newPosition)) {
//                    newPosition = ladderPositions.get(newPosition);
//                    System.out.println("Player landed on a ladder! Moved to position " + newPosition);
//                    moved = true;
//                }
//
//                // Update the position in the game board and UI
//                gameBoard.setPlayerPosition(currentPlayer, newPosition);
//                updatePlayerPositionOnBoard(currentPlayer);
//
//                if (newPosition >= totalTiles) {
//                    handlePlayerWin(currentPlayer);
//                } else {
//                    updatePlayerTurn();
//                }
//            }
//        }
    }
    private void handleSpecialTile(int position, String tileColor) {
        Random random = new Random();
        switch (tileColor) {
            case "green":
                askQuestion("easy", isCorrect -> movePlayerAndUpdateTurn(isCorrect ? 0 : -1));
                break;
            case "yellow":
                askQuestion("medium", isCorrect -> movePlayerAndUpdateTurn(isCorrect ? 0 : -2));
                break;
            case "red":
                askQuestion("hard", isCorrect -> movePlayerAndUpdateTurn(isCorrect ? 1 : -3));
                break;
            case "blue":
                int movesteps = random.nextBoolean() ? 10: -10;
                movePlayerAndUpdateTurn(movesteps);
                break;
            // Add other cases if there are more special tiles
        }
    }
    private void handlePlayerWin(Player currentPlayer) {
        // Get the game duration as a string
        String gameDurationStr = timeLabel.getText();
        long gameDurationMillis = convertDurationToMillis(gameDurationStr);
        // Difficulty of the match
        String gameDifficulty = difficulty.toString();
        // Add the game data to history.json
        SysData.getInstance().addGameHistory(currentPlayer.getName(), gameDurationStr, difficulty.toString());

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


    private long convertDurationToMillis(String gameDuration) {
        String[] parts = gameDuration.split(":");
        long hours = Long.parseLong(parts[0]);
        long minutes = Long.parseLong(parts[1]);
        long seconds = Long.parseLong(parts[2]);
        return (hours * 3600 + minutes * 60 + seconds) * 1000;
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

