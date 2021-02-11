import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * <code>MainMenu</code> refers to the main menu scene of the game.
 * @version 1.00
 * @since 1.00
 */
public class MainMenu extends Group {
    /**
     * The background of the main menu.
     */
    private ImageView background;
    /**
     * The buttons in the main menu.
     */
    private ImageView[] buttons;
    /**
     * The scene responsible for the illustration.
     */
    private final Scene scene = new Scene(this,1024,768);

    /**
     * This method initializes the background of the game.
     */
    private void setBackground() {
        background = new ImageView(new Image("img/mainmenu/bg.png"));
        background.setFitWidth(1024);
        background.setFitHeight(768);
        background.relocate(0,0);
        getChildren().add(background);
    }

    /**
     * This method sets the event of the missions button
     */
    private void setMissions() {
        buttons[0].setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Execute.changeScene(new Level().getScene());
            }
        });
    }

    /**
     * This method sets the event of the organization button
     */
    private void setOrganization() {
        buttons[1].setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Execute.changeScene(new Organization().getScene());
            }
        });
    }

    /**
     * This method sets the event of the exit button.
     */
    private void setExit() {
        buttons[2].setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.exit(0);
            }
        });
    }

    /**
     * This method is responsible for initializing the buttons.
     */
    private void setButtons() {
        buttons = new ImageView[3];
        for (int i =0;i<3;i++) {
            buttons[i] = new ImageView(new Image("img/mainmenu/B"+(i+1)+".png"));
            buttons[i].relocate(310,400+(100*i));
            getChildren().add(buttons[i]);
        }
        setMissions();
        setOrganization();
        setExit();
    }

    /**
     * The constructor.
     */
    public MainMenu() {
        setBackground();
        setButtons();
    }
}
