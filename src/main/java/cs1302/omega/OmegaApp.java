package cs1302.omega;

//import cs1302.game.DemoGame;
import cs1302.game.SnakeGame;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

/**
 * This app represents a snake game.
 */
public class OmegaApp extends Application {

    /**
     * Constructs an {@code OmegaApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */
    public OmegaApp() {}

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {

        Label instructions
            = new Label("Use the arrow keys to move the snake.");

        SnakeGame game = new SnakeGame();

        Button playAgain = new Button("Play Again");

        // setup scene
        VBox root = new VBox(instructions, game, playAgain);
        Scene scene = new Scene(root);
        playAgain.setOnAction(event -> {
            game.pause();
            stage.setScene(scene);
            start(stage);
        });


        // setup stage
        stage.setTitle("SnakeGame!"); // was OmegaApp!
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.sizeToScene();
        stage.show();

        // play the game
        game.play();

    } // start

} // OmegaApp
