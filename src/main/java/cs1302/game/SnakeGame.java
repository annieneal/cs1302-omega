package cs1302.game;

import java.util.ArrayList;
import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;
import javafx.scene.image.Image;

/**
 * This class represents a classic snake game. The user moves the snake with
 * the arrow keys, attempting to eat as many apples as possible. The game ends
 * if the snake runs into a wall or itself.
 */
public class SnakeGame extends VBox {

    protected static ArrayList<Snake> snake;
    protected static int appleX = 0;
    protected static int appleY = 0;
    protected static final int BOX_SIZE = 20;
    protected static final int GRID_SIZE = 400;
    protected static final int ROWS = 20;
    protected static final int COLUMNS = 20;
    protected static Random rand = new Random();
    private final Timeline loop = new Timeline();
    private final Duration fpsTarget;
    Canvas canvas;
    GraphicsContext gc;
    boolean gameOver;
    VBox scoreBox;
    Text scoreText;
    int score = 0;
    int tailX = 0;
    int tailY = 0;
    private boolean initialized = false;
    String direction = "right";

    /**
     * Constructs a {@code SnakeGame} object.
     */
    public SnakeGame() {
        super();
        setMinWidth(GRID_SIZE);
        setMinHeight(GRID_SIZE + 16);
        this.fpsTarget = Duration.millis(300);
        addEventFilter(KeyEvent.KEY_PRESSED, event -> getDirection(event));
        this.snake = new ArrayList<>();
        scoreText = new Text("Score: " + score);
        canvas = new Canvas(GRID_SIZE, GRID_SIZE);
        gc = canvas.getGraphicsContext2D();
        gameOver = false;
        initGameLoop();
    } // SnakeGame

    /**
     * Initializes the snake game. The {@link #play} method calls this method once.
     */
    protected void init() {
        snake.add(new Snake(8, 10));
        snake.add(new Snake(7, 10));
        snake.add(new Snake(6, 10));
        getChildren().addAll(scoreText, canvas);
        // filling in background of game screen
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, GRID_SIZE, GRID_SIZE);
        // filling in snake links
        for (Snake link : snake) {
            gc.setFill(Color.GREEN);
            gc.fillRect(link.x * BOX_SIZE, link.y * BOX_SIZE, BOX_SIZE, BOX_SIZE);
        }
        moveApple();
        gc.setFill(Color.RED);
        gc.fillOval(appleX * BOX_SIZE, appleY * BOX_SIZE, BOX_SIZE, BOX_SIZE);
    }

    /**
     * Sets up and starts the main game loop. This method was derived from the the
     * UGA CSCI1302 {@code cs1302.game.Game} class.
     */
    public final void play() {
        if (!initialized) {
            init();
            initialized = true;
        }
        loop.play();
    }

    /**
     * Pauses the main game loop.
     */
    public final void pause() {
        loop.pause();
    }

    /**
     * Initializes the main game loop. This method was derived from UGA CSCI1302
     * {@code cs1302.game.Game} class.
     */
    private void initGameLoop() {
        KeyFrame updateFrame = new KeyFrame(fpsTarget, event -> {
            requestFocus();
            update();
        });
        loop.setCycleCount(Timeline.INDEFINITE);
        loop.getKeyFrames().add(updateFrame);
    } // initGameLoop

    /**
     * Sets the direction of the snake given user input of key pressed.
     *
     * @param event the KeyEvent passed by the user.
     */
    protected void getDirection(KeyEvent event) {
        if (event.getCode() == KeyCode.RIGHT) {
            direction = "right";
        }
        if (event.getCode() == KeyCode.LEFT) {
            direction = "left";
        }
        if (event.getCode() == KeyCode.UP) {
            direction = "up";
        }
        if (event.getCode() == KeyCode.DOWN) {
            direction = "down";
        }
    }

    /**
     * Performs one iteration of the main game loop.
     */
    protected void update() {
        if (gameOver) {
            gc.setFill(Color.RED);
            gc.fillText("Game Over", 170, 200);
            return;
        }
        // variables to hold value of snake tail of the last frame
        tailX = snake.get(snake.size() - 1).x;
        tailY = snake.get(snake.size() - 1).y;
        // moves snake to position for this frame
        for (int i  = snake.size() - 1; i >= 1; i--) {
            snake.get(i).x = snake.get(i - 1).x;
            snake.get(i).y = snake.get(i - 1).y;
        }
        // if the snake eats the apple, the coordinates of the tail from the last frame
        // are appended to the array list
        if (eatApple() == true) {
            snake.add(snake.size(), new Snake(tailX, tailY));
        }
        moveSnake();
        if (checkWallCollision() || checkSelfCollision()) {
            gameOver = true;
        }
        // redraws game screen
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, GRID_SIZE, GRID_SIZE);
        gc.setFill(Color.RED);
        gc.fillOval(appleX * BOX_SIZE, appleY * BOX_SIZE, BOX_SIZE, BOX_SIZE);
        for (Snake link : snake) {
            gc.setFill(Color.GREEN);
            gc.fillRect(link.x * BOX_SIZE, link.y * BOX_SIZE, BOX_SIZE, BOX_SIZE);
        }
    } // update

    /**
     * Moves the food to a new random spot on the game screen.
     */
    protected void moveApple() {
        appleX = rand.nextInt(ROWS);
        appleY = rand.nextInt(COLUMNS);
    } // moveApple

    /**
     * Helper method to return X position of the head of the snake.
     * @return the X position of the snake head
     */
    protected int getHeadX() {
        return snake.get(0).x;
    }

    /**
     * Helper method to return the Y position of the head of the snake.
     * @return the Y position of the snake head
     */
    protected int getHeadY() {
        return snake.get(0).y;
    }

    /**
     * Checks to see if the snake runs into the wall.
     * @return true if the snake hit the wall (i.e. the snake head goes out of bounds)
     * and false otherwise
     */
    protected boolean checkWallCollision() {
        if (getHeadX() < 0 || getHeadX() > COLUMNS || getHeadY() < 0 || getHeadY() > ROWS) {
            return true;
        } else {
            return false;
        }
    } // checkWallCollision

    /**
     * Checks to see if the snake collides with itself.
     * @return true if the snake runs into itself, false otherwise
     */
    protected boolean checkSelfCollision() {
        boolean selfCollision = false;
        for (int i = 1; i < snake.size(); i++) {
            if (getHeadX() == snake.get(i).x && getHeadY() == snake.get(i).y) {
                selfCollision = true;
            }
        }
        return selfCollision;
    } // checkSelfCollision

    /**
     * Moves the head of the snake based on the direction acquired from the KeyEvent handler.
     */
    protected void moveSnake() {
        if (direction.equals("left")) {
            snake.get(0).x--;
            if (checkWallCollision() || checkSelfCollision()) {
                gameOver = true;
            }
        } else if (direction.equals("right")) {
            snake.get(0).x++;
            if (checkWallCollision() || checkSelfCollision()) {
                gameOver = true;
            }
        } else if (direction.equals("up")) {
            snake.get(0).y--;
            if (checkWallCollision() || checkSelfCollision()) {
                gameOver = true;
            }
        } else if (direction.equals("down")) {
            snake.get(0).y++;
            if (checkWallCollision() || checkSelfCollision()) {
                gameOver = true;
            }
        }

    } // moveSnake

    /**
     * Moves the food and updates the score when the snake eats it.
     * @return true if the snake eats the apple, false otherwise
     */
    protected boolean eatApple() {
        if (appleX == getHeadX() && appleY == getHeadY()) {
            score++;
            scoreText.setText("Score: " + score);
            moveApple();
            return true;
        } else {
            return false;
        }
    }

} // SnakeGame
