import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Random;

/**
 * <code>Execute</code> class is responsible for running the game.
 *
 * @version 1.00
 * @since 1.00
 */
public class Execute extends Application {
    /**
     * The stage of the project.
     */
    private static Stage stage;
    /**
     * The random object utilized in the whole project.
     */
    private static final Random random = new Random();
    /**
     * The player object used inside the whole game.
     */
    private final static Player player = Player.player();

    /**
     * Getter of the player
     * @return the player
     */
    public static Player getPlayer() {
        return player;
    }

    /**
     * Getter of the random object
     *
     * @return the random object.
     */
    public static Random getRandom() {
        return random;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        stage.setResizable(false);
        changeScene(new MainMenu().getScene());
        stage.show();
    }

    /**
     * This method is responsible for changing the scene of the game.
     *
     * @param scene is the new scene we would want to show.
     */
    public static void changeScene(Scene scene) {
        stage.setScene(scene);
    }

    /**
     * The main method
     *
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
