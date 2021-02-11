import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * <code>Level</code> refers to the scene that illustrates the playable levels to the player.
 * @version 1.00
 * @since 1.00
 */
public class Level extends Group {
    /**
     * The scene responsible for showing the display.
     */
    private final Scene scene = new Scene(this,1024,768);

    /**
     * Buttons for showing the missions.
     */
    private Button mission1, mission2;

    /**
     * This method initializes the buttons.
     */
    private void setButtons() {
        mission1 = new Button("Mission 1");
        mission2 = new Button("Mission 2");
        getChildren().addAll(mission1, mission2);
        mission1.setPrefWidth(300);
        mission1.setPrefHeight(100);
        try{
            Scanner reader = new Scanner(new File("save/level2.bin"));
            reader.nextLine();
        }catch (IOException error) {
            //If the first level has not been beaten yet.
            mission2.setDisable(true);
        }
        mission2.setPrefWidth(300);
        mission2.setPrefHeight(100);
        mission1.relocate(512-150, 300);
        mission2.relocate(512-150, 500);
        mission1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Execute.changeScene(new GamePage(1).getScene());
            }
        });
        mission2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Execute.changeScene(new GamePage(2).getScene());
            }
        });
        ImageView back = new ImageView("img/mainmenu/back.png");
        getChildren().add(back);
        back.relocate(20,720);
        back.setFitWidth(100);
        back.setFitHeight(40);
        back.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Execute.changeScene(new MainMenu().getScene());
            }
        });
    }

    /**
     * This method sets the background
     */
    private void setBackground() {
        ImageView bg = new ImageView("img/mainmenu/bg.png");
        bg.setFitHeight(768);
        bg.setFitWidth(1024);
        getChildren().add(bg);
    }

    /**
     * The constructor.
     */
    public Level() {
        setBackground();
        setButtons();
    }

}
