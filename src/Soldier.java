import javafx.animation.AnimationTimer;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.LinkedList;

/**
 * <code>Soldier</code> class is the representation of the soldier in the game.
 */
public class Soldier extends ImageView {
    /**
     * A random color getter for setting rgb color
     *
     * @return an int from 0 to 255 inclusive.
     */
    private int randomColor() {
        return Execute.getRandom().nextInt(256);
    }

    /**
     * The stages of the finite state machine
     */
    private enum Stage {
        ATTACK, MOVE, GOBACK, FOLLOWMOUSE
    }

    /**
     * The enemies linkedlist
     */
    private LinkedList<Enemy> enemies;
    /**
     * An object of the states of the finite state machine.
     */
    private Stage currentStage;
    /**
     * The obstacles of the map.
     */
    private Rectangle[] obstacles;
    /**
     * The stats of the soldier
     */
    private double hp, attack, filedOfView, attackRange;
    /**
     * The name of the soldier.
     */
    private String name;
    /**
     * Soldiers linked list.
     */
    private LinkedList<Soldier> soldiers;
    /**
     * The children of the display.
     */
    private ObservableList<Node> children;
    private boolean selected;

    public double getHp() {
        return hp;
    }

    public void setHp(double hp) {
        this.hp = hp;
    }

    public double getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    /**
     * The updater of the finite state machine.
     */
    private AnimationTimer a;
    /**
     * The initial x and y position.
     */
    private double initX, initY;

    private int level;
    private ElementGenerator.SoldierType type;
    /**
     * The constructor.
     * <strong>Note:</strong>
     * It was inconvenient to create an object using the constructor every time. Therefore ElementGenerator takes the
     * responsibility for managing all of the game objects.
     * <a>Important:</a>
     * The default velocity speed is 0.25. Therefore by multiplying it by 4 there would be no need for modification of the
     * speed of the soldiers.
     * For more information see {@link ElementGenerator}
     *
     * @param enemies
     * @param obstacles
     * @param soldiers
     * @param children
     * @param attack
     * @param attackRange
     * @param hp
     * @param name
     * @param fieldOfView
     * @param initX
     * @param initY
     */
    public Soldier(LinkedList<Enemy> enemies, Rectangle[] obstacles, LinkedList<Soldier> soldiers, ObservableList<Node> children, int attack, int attackRange, int hp, String name, int fieldOfView, double initX, double initY, ElementGenerator.SoldierType type) {
        setImage(new Image("img/soldiers/"+(((type.ordinal())%4)+1)+".png"));
        setFitHeight(25);
        setFitWidth(25);
        setRotate(270);
        currentStage = Stage.MOVE;
        this.enemies = enemies;
        this.obstacles = obstacles;
        this.soldiers = soldiers;
        this.hp = hp;
        this.attack = attack;
        this.filedOfView = fieldOfView;
        this.name = name;
        this.attackRange = attackRange;
        this.children = children;
        a = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        a.start();
        this.initX = initX;
        this.initY = initY;
        setOnMouseHandle();
        this.type = type;
    }

    public ElementGenerator.SoldierType getType() {
        return type;
    }

    private void setOnMouseHandle() {
        setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                for (Soldier soldier:soldiers) {
                    soldier.setSelected(false);
                }
                setSelected(true);
            }
        });
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * This method provides the angle between two objects in the game.
     *
     * @param rectangle is the rectangle representing the destination.
     * @return the angle between the soldier and the rectangle.
     */
    private double angleBetween(Node rectangle) {
        return Math.toDegrees(Math.atan2(rectangle.getTranslateY() - getTranslateY(), rectangle.getTranslateX() - getTranslateX()));
    }

    /**
     * This method provides the distance to an object in the game
     *
     * @param rectangle is the destination object
     * @return the distance between the given two objects.
     */
    private double distanceTo(Node rectangle) {
        return Math.sqrt(Math.pow(rectangle.getTranslateY() - getTranslateY(), 2) + Math.pow(rectangle.getTranslateX() - getTranslateX(), 2));
    }

    /**
     * This method would provide the closest enemy to the soldier.
     *
     * @return the closest enemy to the soldier.
     */
    private Enemy closestEnemy() {
        int index = 0;
        for (int i = 1; i < enemies.size(); i++) {
            if (distanceTo(enemies.get(index)) > distanceTo(enemies.get(i))) {
                index = i;
            }
        }
        return enemies.get(index);
    }
    private void avoidCollision() {
        for (Soldier soldier:soldiers) {
            if (soldier == this) continue;
            if (getBoundsInParent().intersects(soldier.getBoundsInParent())) {
                setTranslateX(getTranslateX()-(Math.cos(Math.toRadians(angleBetween(soldier)))));
                setTranslateY(getTranslateY()- Math.sin(Math.toRadians(angleBetween(soldier))));
            }
        }
    }

    /**
     * The move state of the finite state machine.
     */
    private void move() {
        avoidCollision();
        if (!selected) {
            boolean collided = false;
            for (Rectangle rectangle : obstacles) {
                if (rectangle.getBoundsInParent().intersects(getBoundsInParent())) {
                    collided = true;
                    break;
                }
            }
            if (enemies.size() > 0) {
                if (distanceTo(closestEnemy()) < filedOfView) {
                    if (!collided) {
                        this.setTranslateX((Math.cos(Math.toRadians(angleBetween(closestEnemy())))) + this.getTranslateX());
                    } else {
                        setTranslateY(getTranslateY() + 1);
                    }
                    this.setTranslateY(this.getTranslateY() + Math.sin(Math.toRadians(angleBetween(closestEnemy()))));
                    setRotate(angleBetween(closestEnemy())+90);
                }
                Circle circle = new Circle();
                circle.setRadius(2 * attackRange);
                circle.setTranslateX(getTranslateX());
                circle.setTranslateY(getTranslateY());
                if (circle.getBoundsInParent().intersects(closestEnemy().getBoundsInParent()))
                    currentStage = Stage.ATTACK;
            }
            if ((enemies.size() == 0 || distanceTo(closestEnemy()) > filedOfView) && ((int) getTranslateX() != (int) initX && (int) getTranslateY() != (int) initY)) {
                currentStage = Stage.GOBACK;
            }
        }else {
            currentStage = Stage.FOLLOWMOUSE;
        }
    }

    /**
     * The go back state of the finite state machine.
     */
    private void goBack() {
        boolean collided = false;
        for (Rectangle rectangle : obstacles) {
            if (rectangle.getBoundsInParent().intersects(getBoundsInParent())) {
                collided = true;
                break;
            }
        }
        Rectangle init = new Rectangle(1, 1);
        init.setTranslateX(initX);
        init.setTranslateY(initY);
        if (!collided) {
            setTranslateX(Math.cos(Math.toRadians(angleBetween(init))) + getTranslateX());

        } else setTranslateY(getTranslateY()+1);
        setRotate(angleBetween(init)+90);
        setTranslateY(Math.sin(Math.toRadians(angleBetween(init))) + getTranslateY());
        currentStage = Stage.MOVE;
    }

    /**
     * The attack state state of the finite state machine.
     */
    private void attack() {
        Circle circle = new Circle();
        circle.setRadius(2*attackRange);
        circle.setTranslateX(getTranslateX());
        circle.setTranslateY(getTranslateY());
        if (enemies.size() == 0 || !circle.getBoundsInParent().intersects(closestEnemy().getBoundsInParent())) {
            currentStage = Stage.MOVE;
            return;
        }
        closestEnemy().setHp(closestEnemy().getHp() - attack);
    }
    private double mouseX() {
        return GamePage.getMouseX();
    }
    private double mouseY() {
        return GamePage.getMouseY();
    }
    private void followMouse() {
        avoidCollision();
        if (!selected) {
            currentStage = Stage.MOVE;
            return;
        }
        if ((int) mouseX() == (int)getTranslateX()&&(int) mouseY() == (int) getTranslateY()){
            currentStage = Stage.MOVE;
            return;
        }
        Circle circle = new Circle();
        circle.setRadius(2 * attackRange);
        circle.setTranslateX(getTranslateX());
        circle.setTranslateY(getTranslateY());
        boolean collided = false;
        for (Rectangle rectangle : obstacles) {
            if (rectangle.getBoundsInParent().intersects(getBoundsInParent())) {
                collided = true;
                break;
            }
        }
        Rectangle r = new Rectangle(1,1);
        r.setTranslateX(mouseX());
        r.setTranslateY(mouseY());
        if (!collided) {
            this.setTranslateX((Math.cos(Math.toRadians(angleBetween(r)))) + this.getTranslateX());

        } else {
            setTranslateY(getTranslateY() + 1);
        }
        setRotate(angleBetween(r)+90);
        this.setTranslateY(this.getTranslateY() + Math.sin(Math.toRadians(angleBetween(r))));
        if (enemies.size()!=0&&circle.getBoundsInParent().intersects(closestEnemy().getBoundsInParent()))
            currentStage = Stage.ATTACK;
    }

    /**
     * This method is responsible for updating the finite state machine.
     */
    private void update() {

        if (currentStage == Stage.MOVE) {
            move();
        } else if (currentStage == Stage.ATTACK) {
            attack();
        } else if (currentStage == Stage.GOBACK) {
            goBack();
        } else followMouse();
        if (hp <= 0) {
            children.remove(this);
            soldiers.remove(this);
            a.stop();
        }
    }
}
