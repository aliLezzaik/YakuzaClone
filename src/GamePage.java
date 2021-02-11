import com.sun.tools.javac.Main;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * <code>GamePage</code> is the representation of the gameplay.
 *
 * @version 1.00
 * @since 1.00
 */
public class GamePage extends Group {
    /**
     * The scene for the matter of displaying contents.
     */
    private final Scene scene = new Scene(this, 1024, 768);
    /**
     * The obstacles for making the characters not be able to cross it
     */
    private Rectangle[] obstacles;
    /**
     * The rectangle representing the enemy side.
     */
    private Rectangle enemySide;
    /**
     * The soldiers linkedlist
     */
    private final LinkedList<Soldier> soldiers = new LinkedList<>();
    /**
     * The enemies linkedlist
     */
    private final LinkedList<Enemy> enemies = new LinkedList<>();
    /**
     * The materials linkedlist
     */
    private final LinkedList<Material> materials = new LinkedList<>();
    /**
     * Element generator for adding contents to the game.
     */
    private ElementGenerator elementGenerator;
    /**
     * The x and y position of the mouse
     */
    private static double mouseX, mouseY;
    /**
     * The level generator of the object
     * @see LevelGen
     */
    private LevelGen levelGen;
    /**
     * The label demonstrating the current wave of the game.
     */
    private Label label;


    /**
     * Setter of the enemy side.
     */
    private void setEnemySide() {
        enemySide = new Rectangle();
        enemySide.setWidth(90);
        enemySide.setHeight(748);
        enemySide.relocate(10, 10);
        enemySide.setFill(Color.GRAY);
        getChildren().add(enemySide);
    }

    /**
     * The setter of the obstacles
     */
    private void setObstacles() {
        obstacles = new Rectangle[4];
        for (int i = 0; i < 4; i++) {
            obstacles[i] = new Rectangle();
            obstacles[i].setFill(Color.RED);
            obstacles[i].setWidth(15);
            obstacles[i].setHeight(125);
            obstacles[i].relocate(650, i * 192);
        }
        getChildren().addAll(obstacles);
        elementGenerator = new ElementGenerator(enemies, soldiers, materials, getChildren(), obstacles);
    }

    /**
     * this method provides a random element according to the bound
     *
     * @param bound is the bound of the random elements
     * @return a random number inside the bounds.
     */
    private int nextInt(int bound) {
        return Execute.getRandom().nextInt(bound);
    }


    /**
     * Setting the x and y position of the mouse.
     */
    private void setMousePos() {
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mouseX = event.getX();
                mouseY = event.getY();
            }
        });
    }

    /**
     * Initializing the level generator
     * @param level is the current level of the game.
     */
    private void setLevelGen(int level) {
        levelGen = new LevelGen(level);
    }

    /**
     * The updater object responsible for updating the gamepage materials.
     */
    private AnimationTimer updater;

    /**
     * This method is responsible for updating the game contents.
     */
    private void update() {
        if (levelGen.getCurWave()>6) {
            updater.stop();
            try{
                FileWriter writer = new FileWriter(new File("save/level2.bin"));
                writer.write("success");
                writer.close();
            }catch (IOException error) {

            }
            Execute.changeScene(new MainMenu().getScene());
        } else {
            if (enemies.size() == 0)
            levelGen.makeWave(elementGenerator);
        }
        if (materials.size()!=2) {
            updater.stop();
            Execute.changeScene(new MainMenu().getScene());
        }
        updateLabel();
    }

    /**
     * Initializing the updater to run the update method constantly.
     */
    private void checkForWave() {
         updater = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        updater.start();
    }

    /**
     * This method is responsible for loading the contents from the player's inventory.
     */
    private void loadPlayer() {
        try{
            Scanner materials = new Scanner(new File("save/selectedMaterials.bin"));
            Scanner soldiers = new Scanner(new File("save/selectedSoldiers.bin"));
            int i =0;
            // Adding the contents.
            while (materials.hasNextInt()) {
                elementGenerator.addMaterial(ElementGenerator.MaterialType.values()[materials.nextInt()],
                        924, 90 + (i * 500));
                i++;
            }
            // Implementing the level to the game
            File[] soldierFiles = new File("save/soldiers").listFiles();
            for (Soldier soldier:this.soldiers) {
                Scanner reader = new Scanner(soldierFiles[soldier.getType().ordinal()]);
                reader.nextInt();
                soldier.setHp(soldier.getHp()+((5/100)*soldier.getHp())*reader.nextInt());
            }
            i =0;
            //Adding contents
            while (soldiers.hasNextInt()) {
                elementGenerator.addSoldier(ElementGenerator.SoldierType.values()[soldiers.nextInt()], 900,10+(i*75));
                i++;
            }
            // Implementing the level to the game.
            File[] materialFiles = new File("save/materials").listFiles();
            for (Material material:this.materials) {
                Scanner reader = new Scanner(soldierFiles[material.getType().ordinal()]);
                reader.nextInt();
                material.setDurability(material.getDurability()+((5/100)*material.getDurability())*reader.nextInt());
            }
        }catch (IOException error) {
            //This error would only happen in case of the player getting rid of the save files or not selecting two materials.
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Error loading the preferred setup. Please make sure you chose two materials.");
            alert.showAndWait();
            Execute.changeScene(new MainMenu().getScene());
        }
    }

    /**
     * This method initializes the label responsible for demonstrating the wave count.
     */
    private void setLabel() {
        label = new Label("Wave "+levelGen.getCurWave());
        label.relocate(500,10);
        label.setTextFill(Color.BLUE);
        getChildren().add(label);
    }

    /**
     * This method is responsible for updating the label.
     */
    private void updateLabel() {
        label.setText("Wave "+levelGen.getCurWave());
    }
    /**
     * The constructor.
     */
    public GamePage(int level) {
        ImageView background = new ImageView(new Image("img/gamepage/PlayField 800X800.png"));
        background.setFitWidth(1024);
        background.setFitHeight(768);
        getChildren().add(background);
        setObstacles();
        setEnemySide();
        checkForWave();
        setLevelGen(level);
        loadPlayer();
        setMousePos();
        setLabel();
    }

    public static double getMouseX() {
        return mouseX;
    }

    public static double getMouseY() {
        return mouseY;
    }
}
