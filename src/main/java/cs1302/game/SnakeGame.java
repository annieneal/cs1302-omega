package cs1302.game;

import java.util.ArrayList;
import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.geometry.Bounds;
import javafx.geometry.BoundingBox;
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
    private final Bounds bounds;
    //SnakeFood apple;
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
    //Image apple;


    public SnakeGame() {
        //super(GRID_SIZE, GRID_SIZE, 60);
        super();
        setMinWidth(GRID_SIZE);
        setMinHeight(GRID_SIZE + 15);
        this.bounds = new BoundingBox(0, 0, ROWS, COLUMNS);
        this.fpsTarget = Duration.millis(400);
        addEventFilter(KeyEvent.KEY_PRESSED, event -> getDirection(event));
        this.snake = new ArrayList<>();
        scoreText = new Text("Score: " + score);
        canvas = new Canvas(GRID_SIZE, GRID_SIZE);
        gc = canvas.getGraphicsContext2D();
        gameOver = false;
        initGameLoop();
    } // SnakeGame

    protected void init() {
        snake.add(new Snake(10, 10));
        snake.add(new Snake(9, 10));
        snake.add(new Snake(8, 10));
        //scoreBox.getChildren().add(scoreText);
        getChildren().addAll(scoreText, canvas);
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, GRID_SIZE, GRID_SIZE);
        for(Snake link : snake) {
            gc.setFill(Color.GREEN);
            gc.fillRect(link.x * BOX_SIZE, link.y * BOX_SIZE, BOX_SIZE, BOX_SIZE);
        }
        moveApple();
        gc.setFill(Color.RED);
        gc.fillOval(appleX * BOX_SIZE, appleY * BOX_SIZE, BOX_SIZE, BOX_SIZE);

    }

    public final void play() {
        if(!initialized) {
            init();
            initialized = true;
        }
        loop.play();
    }

    /**
     * Method derived from UGA CSCI1302 cs1302.game.Game class.
     */
    private void initGameLoop() {
        KeyFrame updateFrame = new KeyFrame(fpsTarget, event -> {
            requestFocus();
            update();
        });
        loop.setCycleCount(Timeline.INDEFINITE);
        loop.getKeyFrames().add(updateFrame);
    } // initGameLoop

    protected void getDirection(KeyEvent event) {
        if(event.getCode() == KeyCode.RIGHT) {
            direction = "right";
        }
        if(event.getCode() == KeyCode.LEFT) {
            direction = "left";
        }
        if(event.getCode() == KeyCode.UP) {
            direction = "up";
        }
        if(event.getCode() == KeyCode.DOWN) {
            direction = "down";
        }
    }


    protected void update() {
        if(gameOver) {
            gc.setFill(Color.RED);
            gc.fillText("Game Over", 150, 200);
            return;
        }
        tailX = snake.get(snake.size() - 1).x;
        tailY = snake.get(snake.size() - 1).y;
        for(int i  = snake.size() - 1; i >= 1; i--) {
            snake.get(i).x = snake.get(i - 1).x;
            snake.get(i).y = snake.get(i - 1).y;
        }
        if(eatApple() == true) {
            snake.add(snake.size(), new Snake(tailX, tailY));
        }
        moveSnake();
        if(checkWallCollision() || checkSelfCollision()) {
            gameOver = true;
        }
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, GRID_SIZE, GRID_SIZE);
        gc.setFill(Color.RED);
        gc.fillOval(appleX * BOX_SIZE, appleY * BOX_SIZE, BOX_SIZE, BOX_SIZE);
        for(Snake link : snake) {
            gc.setFill(Color.GREEN);
            gc.fillRect(link.x * BOX_SIZE, link.y * BOX_SIZE, BOX_SIZE, BOX_SIZE);
        }



    } // update

    protected void moveApple() {
        appleX = rand.nextInt(ROWS);
        appleY = rand.nextInt(COLUMNS);
        for(Snake link : snake) {
            if(link.x == appleX && link.y == appleY) {
                // something to find new random number
            }
        }
    } // moveApple

    protected int getHeadX() {
        return snake.get(0).x;
    }

    protected int getHeadY() {
        return snake.get(0).y;
    }

    protected boolean checkWallCollision() {
        if(getHeadX() < 0 || getHeadX() > COLUMNS || getHeadY() < 0 || getHeadY() > ROWS) {
            return true;
        } else {
            return false;
        }
    } // checkWallCollision

    protected boolean checkSelfCollision() {
        boolean selfCollision = false;
        for(int i = 1; i < snake.size(); i++) {
            if(getHeadX() == snake.get(i).x && getHeadY() == snake.get(i).y) {
                selfCollision = true;
            }
        }
        return selfCollision;
    } // checkSelfCollision

    protected void moveSnake() {
        if(direction.equals("left")) {
            snake.get(0).x--;
            //if(checkWallCollision() || checkSelfCollision()) {
            //gameOver = true;
            //}
        } else if(direction.equals("right")) {
            snake.get(0).x++;
            //if(checkWallCollision() || checkSelfCollision()) {
                //  gameOver = true;
            //}
        } else if(direction.equals("up")) {
            snake.get(0).y--;
            //if(checkWallCollision() || checkSelfCollision()) {
            //  gameOver = true;
            //}
        } else if(direction.equals("down")) {
            snake.get(0).y++;
            //if(checkWallCollision() || checkSelfCollision()) {
            // gameOver = true;
            //}
        }

    } // moveSnake

    protected boolean eatApple() {
        if(appleX == getHeadX() && appleY == getHeadY()) {
            score++;
            scoreText.setText("Score: " + score);
            moveApple();
            return true;
        } else {
            return false;
        }
    }





} // SnakeGame
