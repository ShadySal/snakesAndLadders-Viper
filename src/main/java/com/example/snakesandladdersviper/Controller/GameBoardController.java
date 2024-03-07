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
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.transform.Rotate;
import javafx.util.Pair;
import javafx.scene.paint.Color;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Consumer;

public class GameBoardController {

    private GridPane BoardGrid;
    @FXML
    private AnchorPane contentPane;
    @FXML
    private StackPane gamepane;
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
    private Map<Player, ImageView> playerImages;
    private Set<Integer> occupiedPositions;
    private int bsize;
    private int globalSize;
    private Map<Integer, Integer> snakePositions = new HashMap<>();
    private Map<Integer, Integer> ladderPositions = new HashMap<>();
    private Map<Integer, String> SpecialTiles = new HashMap<>();

//    private void handlePlayerMove(GameEvent event) {
//        Player player = event.getPlayer();
//        int newPosition = event.getNewPosition();
//
//        // Find the corresponding tile Pane for the new position
//        Pane newTile = getTileForPosition(newPosition);
//
//        if (newTile != null) {
//            ImageView playerImage = playerImages.get(player);
//
//            // Remove player image from the old position
//            Pane oldTile = getTileForPlayer(player);
//            oldTile.getChildren().remove(playerImage);
//
//            // Add player image to the new position
//            newTile.getChildren().add(playerImage);
//
//            // Adjust the position of the image in the tile
//            // You might want to animate this movement for a better UX
//            int imageIndex = newTile.getChildren().indexOf(playerImage);
//            double xOffset = (imageIndex - 0.8) * 40;
//            double yOffset = (imageIndex - 0.3) * 40;
//            playerImage.setTranslateX(xOffset);
//            playerImage.setTranslateY(yOffset);
//        }
//    }
//    private void handleSpecialTile(GameEvent event) {
//        Player player = event.getPlayer();
//        int tilePosition = event.getTilePosition();
//        String tileType = event.getTileType();
//
//        // Based on tileType, you can show a dialog, animate movement, etc.
//        switch (tileType) {
//            case "snake":
//                // Handle snake logic
//                break;
//            case "ladder":
//                // Handle ladder logic
//                break;
//            case "question":
//                // Show a question dialog and wait for the answer
//                break;
//            // Handle other special tile types...
//        }
//    }

    public void initialize() {
        startTime = System.currentTimeMillis();
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateClock()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        playerCircles = new HashMap<>();
        playerImages = new HashMap<>();
        occupiedPositions = new HashSet<>();
        BoardGrid = new GridPane();
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
                    imageView.setFitHeight(50); // Set size as needed
                    imageView.setFitWidth(50);
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
        BoardGrid = new GridPane();
        int size = determineBoardSize(difficulty);
        this.globalSize = size;
        this.bsize = size*size;
        System.out.println(bsize);
        gameBoard = new GameBoard(size, size);
        List<Snake> snakes = new ArrayList<>(); // List to store snakes
        List<Ladder> ladders = new ArrayList<>();
        Map<Integer, String> specialTiles = generateSpecialTiles(difficulty, size);
        Set<Integer> occupiedPositions = determineOccupiedPositions(specialTiles, size); // Determine occupied positions
        // Initialize special tiles (questions and surprise)

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
        gamepane.setAlignment(Pos.CENTER);

        setupGridConstraints(size);
        LevelLabel.setText("Level: " + difficulty);
        BoardGrid.getChildren().clear();
        gameBoard.initializePlayerPositions(players);
        gamepane.getChildren().add(BoardGrid);
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

        // Place the snakes and ladders on the board
        placeSnakesAndLadders(snakes, ladders);

        setPlayers(players);
        updatePlayerTurn();

    }
    // Generate snakes and ladders for the easy difficulty
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
    private void generateSnake(List<Snake> snakes, Set<Integer> occupiedPositions, int maxPosition, String type, int minPosition) {
        Random random = new Random();
        int start = -1, end;
        int rowsBack = type.equals("yellow") ? 1 : (type.equals("green") ? 2 : 3);
        int startRow = -1; // Declare startRow outside of the loop
        int boardSize = globalSize;
        System.out.println(boardSize + "boardSize");
        HashMap<Integer, List<Integer>> rowToTilesMap = createRowToTilesMap(boardSize);
        boolean validStartFound = false;

        while (!validStartFound) {
            // Randomly select a start row based on the snake type
            startRow = random.nextInt(boardSize - rowsBack) + rowsBack + 1;  // +1 to adjust row index to start from 1



            List<Integer> possibleStartPositions = rowToTilesMap.get(startRow);

            // Shuffle the list for randomness
            Collections.shuffle(possibleStartPositions);

            for (int possibleStart : possibleStartPositions) {
                if (!occupiedPositions.contains(possibleStart) && possibleStart <= maxPosition) {
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

        System.out.println("Added " + type + " snake: Start=" + start + ", End=" + end);
        occupiedPositions.add(start);
        if (start != end) {
            occupiedPositions.add(end);
        }
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
        System.out.println(rowToTilesMap);
        return rowToTilesMap;
    }
    // Helper method to generate a single ladder
    private void generateLadder(List<Ladder> ladders, Set<Integer> occupiedPositions, int maxPosition, int length) {
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
                if (!occupiedPositions.contains(possibleStart)) {
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


    private void placeSnakesAndLadders(List<Snake> snakes, List<Ladder> ladders) {
        for (Snake snake : snakes) {
            placeSnakeOnBoard(snake);
        }
        for (Ladder ladder : ladders) {
            placeLadderOnBoard(ladder);
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
        Pane startTile = getTileByNumber(snake.getStart());
        if (startTile != null) {
            ImageView startSnakeImage = loadSnakeImage(snake.getType());
            startTile.getChildren().add(startSnakeImage);
        }

        // If the snake is not red, place the end image as well
        if (!snake.getType().equals("red")) {
            Pane endTile = getTileByNumber(snake.getEndPosition());
            if (endTile != null) {
                ImageView endSnakeImage = loadSnakeImage(snake.getType());
                endTile.getChildren().add(endSnakeImage);
            }
        }
    }

    private Pane getTileByNumber(int tileNumber) {
        for (Node node : BoardGrid.getChildren()) {
            if (node instanceof Tile && ((Tile)node).getNumber() == tileNumber) {
                return (Pane) node;
            }
        }
        return null; // If no matching tile is found
    }
//        int startRow = getRowForPosition(snake.getStartPosition());
//        int startColumn = getColumnForPosition(snake.getStartPosition());
//        int endRow = getRowForPosition(snake.getEndPosition());
//        int endColumn = getColumnForPosition(snake.getEndPosition());
//
//        // Load snake image
//        String snakeImageName = chooseSnakeImage(snake.getType());
//        InputStream is = getClass().getResourceAsStream("/com/example/snakesandladdersviper/Images/" + snakeImageName);
//        if (is != null) {
//            Image snakeImage = new Image(is);
//            ImageView snakeImageView = new ImageView(snakeImage);
//
//            // Calculate GridPane spans
//            int columnSpan = Math.abs(endColumn - startColumn) + 1;
//            int rowSpan = Math.abs(endRow - startRow) + 1;
//
//            // Adjust ImageView size and position
//            snakeImageView.fitWidthProperty().bind(BoardGrid.widthProperty().divide(globalSize).multiply(columnSpan));
//            snakeImageView.fitHeightProperty().bind(BoardGrid.heightProperty().divide(globalSize).multiply(rowSpan));
//            snakeImageView.setPreserveRatio(true);
//
//            // Add snake image to the GridPane at the calculated position and span
//            GridPane.setRowIndex(snakeImageView, Math.min(startRow, endRow));
//            GridPane.setColumnIndex(snakeImageView, Math.min(startColumn, endColumn));
//            GridPane.setRowSpan(snakeImageView, rowSpan);
//            GridPane.setColumnSpan(snakeImageView, columnSpan);
//            BoardGrid.getChildren().add(snakeImageView);
//        } else {
//            System.out.println("Image file not found: " + snakeImageName);
//        }

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




    private void placeLadderOnBoard(Ladder ladder) {
        // Load ladder images based on the ladder's length
        String ladderImageName = "ladder" + ladder.getLength() + ".png";
        InputStream is = getClass().getResourceAsStream("/com/example/snakesandladdersviper/Images/" + ladderImageName);
        if (is != null) {
            Image ladderImage = new Image(is);
            ImageView startLadderImage = new ImageView(ladderImage);
            ImageView endLadderImage = new ImageView(ladderImage);

            // Adjust the size of the ladder image to fit the tile
            double tileWidth = BoardGrid.getColumnConstraints().get(0).getPrefWidth();
            double tileHeight = BoardGrid.getRowConstraints().get(0).getPrefHeight();
            startLadderImage.setFitWidth(tileWidth);
            startLadderImage.setFitHeight(tileHeight);
            startLadderImage.setPreserveRatio(true);
            endLadderImage.setFitWidth(tileWidth);
            endLadderImage.setFitHeight(tileHeight);
            endLadderImage.setPreserveRatio(true);

            // Place ladder image at the start position
            Pane startTile = getTileByNumber(ladder.getStart());
            if (startTile != null) {
                startTile.getChildren().add(startLadderImage);
            }

            // Place ladder image at the end position
            Pane endTile = getTileByNumber(ladder.getEnd());
            if (endTile != null) {
                endTile.getChildren().add(endLadderImage);
            }
        } else {
            System.out.println("Image file not found: " + ladderImageName);
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
            SpecialTiles.put(tile,questionColors[i]);
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
        System.out.println("in CreateTile");
        Tile tile = new Tile(col, size - row - 1);
        String backgroundColor = "-fx-background-color: ";
        String textColor = "-fx-text-fill: black;";
        String tileLabel = Integer.toString(number);
        tile.setNumber(number);
        if (!specialTileColor.isEmpty()) {
            tile.setColor(specialTileColor);
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
       // StackPane.setAlignment(label, Pos.TOP_LEFT);
       // System.out.println(tile);
        int s = size - row -1 ;
        //System.out.println(col + s);
        BoardGrid.add(tile, col, size - row - 1);

        Platform.runLater(() -> printTileCoordinates(tile));
    }
    private void printTileCoordinates(Tile tile) {
        // Ensure the tile is not null and is part of a scene
        System.out.println("in printTileCoo");
        if (tile != null && tile.getScene() != null) {
            System.out.println("in IF statements");
            Bounds boundsInScene = tile.localToScene(tile.getBoundsInLocal());
            Bounds boundsInScreen = tile.localToScreen(tile.getBoundsInLocal());
            System.out.println("Tile Number: " + tile.getNumber() +
                    ", Scene Coordinates: X=" + boundsInScene.getMinX() +
                    ", Y=" + boundsInScene.getMinY() +
                    ", Screen Coordinates: X=" + boundsInScreen.getMinX() +
                    ", Y=" + boundsInScreen.getMinY());
        }
    }
    private void setupGridConstraints(int size) {
        BoardGrid.getColumnConstraints().clear();
        BoardGrid.getRowConstraints().clear();
        // Define the static size for each tile based on difficulty
        double tileWidth;
        double tileHeight;

        switch (difficulty) {
            case EASY:
                tileWidth = 125; // Width in pixels for easy difficulty
                tileHeight = 125; // Height in pixels for easy difficulty
                break;
            case MEDIUM:
                tileWidth = 100; // Width in pixels for medium difficulty
                tileHeight = 100; // Height in pixels for medium difficulty
                break;
            case HARD:
                tileWidth = 75; // Width in pixels for hard difficulty
                tileHeight = 75; // Height in pixels for hard difficulty
                break;
            default:
                throw new IllegalArgumentException("Unrecognized difficulty level");
        }
        // Apply constraints to each column and row
        for (int i = 0; i < size; i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setMaxWidth(tileWidth);
            columnConstraints.setPrefWidth(tileWidth);
            columnConstraints.setMaxWidth(tileWidth);
            BoardGrid.getColumnConstraints().add(columnConstraints);

            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setMinHeight(tileHeight);
            rowConstraints.setPrefHeight(tileHeight);
            rowConstraints.setMaxHeight(tileWidth);
            BoardGrid.getRowConstraints().add(rowConstraints);
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
                playerImage.setFitHeight(60); // Increase height (e.g., 40)
                playerImage.setFitWidth(60);
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
        // Create a confirmation aler
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
                    currentStage.setFullScreen(true);
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
                if (finalNumber == 0) {
                    Image image = new Image("/com/example/snakesandladdersviper/Images/" + finalNumber + ".jpg");
                    ImageView imageView = new ImageView(image);
                    imageView.setFitHeight(150); // Match the size of the diceImageContainer
                    imageView.setFitWidth(150);

                    // Clear previous content and add the new ImageView
                    diceImageContainer.getChildren().clear();
                    diceImageContainer.getChildren().add(imageView);
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
        int currentPosition = gameBoard.getPlayerPosition(currentPlayer);
        int newPosition = currentPosition + steps;
        int totalTiles = getTotalTilesForDifficulty(difficulty);

        // Check if the new position goes beyond the last tile
        if (newPosition >= totalTiles) {
            handlePlayerWin(currentPlayer);
            return; // Return to prevent further execution
        }

        // Check if the new position is a special tile and trigger appropriate action
        String tileColor = SpecialTiles.get(newPosition);
        if (tileColor != null) {
            switch (tileColor) {
                case "green":
                    askQuestion("easy", isCorrect -> handleQuestionOutcome(isCorrect, "easy"));
                    break;
                case "yellow":
                    askQuestion("medium", isCorrect -> handleQuestionOutcome(isCorrect, "medium"));
                    break;
                case "red":
                    askQuestion("hard", isCorrect -> handleQuestionOutcome(isCorrect, "hard"));
                    break;
            }
        } else {
            // Check if the new position is the start of a snake
            if (snakePositions.containsKey(newPosition)) {
                newPosition = snakePositions.get(newPosition);
                System.out.println("Player landed on a snake! Moved to position " + newPosition);
            }

            // Check if the new position is the start of a ladder
            if (ladderPositions.containsKey(newPosition)) {
                newPosition = ladderPositions.get(newPosition);
                System.out.println("Player landed on a ladder! Moved to position " + newPosition);
            }

            // Update the position in the game board
            gameBoard.setPlayerPosition(currentPlayer, newPosition);

            // Update the UI to reflect the new position
            updatePlayerPositionOnBoard(currentPlayer);
        }
    }
    private void handlePlayerWin(Player currentPlayer) {
        // Get the game duration as a string
        String gameDurationStr = SysData.getInstance().calculateGameDuration(startTime);
        long gameDurationMillis = convertDurationToMillis(gameDurationStr);
        // Difficulty of the match
        String gameDifficulty = difficulty.toString(); // Assuming 'difficulty' is an enum or string variable in your class
        // Add the game data to history.json
        SysData.getInstance().addGameHistory(currentPlayer.getName(), gameDurationMillis, gameDifficulty);
        Platform.runLater(() -> {
            Alert winAlert = new Alert(Alert.AlertType.INFORMATION);
            winAlert.setTitle("Congratulations!");
            winAlert.setHeaderText(null);
            winAlert.setContentText(currentPlayer.getName() + " has won the game!");

            // Add a "Home Screen" button
            ButtonType homeScreenButton = new ButtonType("End Game", ButtonBar.ButtonData.OK_DONE);
            winAlert.getButtonTypes().setAll(homeScreenButton);

            // Wait for user response
            Optional<ButtonType> result = winAlert.showAndWait();

            if (result.isPresent() && result.get() == homeScreenButton) {
                // Load the home screen
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/snakesandladdersviper/hello-view.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);

                    // Get the current stage and set the new scene
                    Stage currentStage = (Stage) contentPane.getScene().getWindow();
                    currentStage.setScene(scene);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private long convertDurationToMillis(String gameDuration) {
        String[] parts = gameDuration.split(":");
        long hours = Long.parseLong(parts[0]);
        long minutes = Long.parseLong(parts[1]);
        long seconds = Long.parseLong(parts[2]);
        return (hours * 3600 + minutes * 60 + seconds) * 1000;
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

