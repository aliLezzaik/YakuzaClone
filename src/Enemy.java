import javafx.animation.AnimationTimer;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.LinkedList;

/**
 * <code>Enemy</code> is the representation of the enemies in this game.
 *
 * @version 1.00
 * @since 1.00
 */
public class Enemy extends ImageView {
    /**
     * Obstacles of the map.
     */
    private Rectangle[] obstacles;
    /**
     * The materials of the map.
     */
    private LinkedList<Material> materials;
    /**
     * The hit point of the enemy.
     */
    private double hp;
    /**
     * The attack strength of the enemy.
     */
    private int attack;
    /**
     * The range of the attack of the enemy.
     */
    private int attackRange;
    /**
     * The children of the display.
     */
    private ObservableList<Node> children;

    /**
     * The stages of the finite state machine responsible for controlling this unit.
     */
    private enum Stages {
        GOTOMATERIAL, ATTACK, ATTACKMATERIALS
    }

    public double getHp() {
        return hp;
    }

    public void setHp(double hp) {
        this.hp = hp;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    /**
     * The current state of the finite state machine.
     */
    private Stages currentStage;
    /**
     * The soldiers linkedlist.
     */
    private LinkedList<Soldier> soldiers;
    /**
     * The enemies linkedlist.
     */
    private LinkedList<Enemy> enemies;
    /**
     * The updater of the enemy.
     */
    private AnimationTimer timer;
    /**
     * The name of the enemy.
     */
    private String name;
    /**
     * The indicator of the velocity of the enemy.
     */
    private double velocity;

    /**
     * The constructor
     *
     * @param obstacles
     * @param materials
     * @param soldiers
     * @param enemies
     * @param children
     * @param hp
     * @param attack
     * @param attackRange
     * @param name
     */

    public Enemy(Rectangle[] obstacles, LinkedList<Material> materials, LinkedList<Soldier> soldiers, LinkedList<Enemy> enemies, ObservableList<Node> children, int hp, int attack, int attackRange, String name, ElementGenerator.EnemyType type, double velocity) {
        setFitWidth(20);
        setFitHeight(20);
        setImage(new Image("img/enemies/"+(type.ordinal()+1)+".png"));
        setTranslateX(10);
        setTranslateY(10);
        this.obstacles = obstacles;
        this.materials = materials;
        currentStage = Stages.GOTOMATERIAL;
        this.enemies = enemies;
        this.hp = hp;
        this.attack = attack;
        this.attackRange = attackRange;
        this.soldiers = soldiers;
        this.children = children;
        this.velocity = velocity;
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        this.name = name;
        timer.start();

    }

    /**
     * This method provides the closest soldier to the enemy.
     *
     * @return the closest soldier.
     */
    private Soldier closestSoldier() {
        int index = 0;
        for (int i = 1; i < soldiers.size(); i++) {
            if (distanceTo(soldiers.get(index)) > distanceTo(soldiers.get(i))) {
                index = i;
            }
        }
        return soldiers.get(index);
    }

    /**
     * This method provides the angle between the enemy and given rectangle.
     *
     * @param rectangle is the destination of the enemy.
     * @return the angle between the enemy and the rectangle.
     */
    private double angleBetween(Node rectangle) {
        return Math.toDegrees(Math.atan2(rectangle.getTranslateY() - getTranslateY(), rectangle.getTranslateX() - getTranslateX()));
    }

    /**
     * This method provides the distance to an object.
     *
     * @param node is the destination object.
     * @return the distance between two objects.
     */
    private double distanceTo(Node node) {
        return Math.sqrt(Math.pow(node.getTranslateY() - getTranslateY(), 2) + Math.pow(node.getTranslateX() - getTranslateX(), 2));
    }

    /**
     * This method is responsible for giving the closest material to the enemy.
     *
     * @return the closest material to the enemy.
     */
    private Material closestMaterial() {
        if (materials.size() == 1) return materials.get(0);
        if (materials.size() == 0) return new Material("", 0, 0, 0, ElementGenerator.MaterialType.CONTAINER);
        if (distanceTo(materials.get(0)) < distanceTo(materials.get(1))) {
            return materials.get(0);
        }
        return materials.get(1);
    }

    /**
     * This method moves the enemy towards the closest material.
     */
    private void approachClosestMaterial() {
        boolean collided = false;
        if (obstacles != null)
            for (Rectangle rectangle : obstacles) {
                if (rectangle.getBoundsInParent().intersects(getBoundsInParent())) {
                    collided = true;
                    break;
                }
            }
        if (!collided) {
            this.setTranslateX(velocity*(Math.cos(Math.toRadians(angleBetween(closestMaterial())))) + this.getTranslateX());
        } else {
            setTranslateY(getTranslateY() + 1);
        }
        for (Enemy enemy: enemies) {
            if (enemy == this) continue;
            if (getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                this.setTranslateX(velocity*(-Math.cos(Math.toRadians(angleBetween(enemy)))) + this.getTranslateX());
                this.setTranslateY(this.getTranslateY() - Math.sin(Math.toRadians(angleBetween(enemy)))*velocity);
            }
        }
        setRotate(90+angleBetween(closestMaterial()));
        this.setTranslateY(this.getTranslateY() + Math.sin(Math.toRadians(angleBetween(closestMaterial())))*velocity);
    }

    /**
     * The gotomaterial state of the finite state machine
     */
    private void gotoMaterial() {
        approachClosestMaterial();
        Circle circle = new Circle();
        circle.setRadius(2*attackRange);
        circle.setTranslateX(getTranslateX());
        circle.setTranslateY(getTranslateY());
        for (Soldier soldier : soldiers) {
            if (circle.getBoundsInParent().intersects(soldier.getBoundsInParent())) {
                currentStage = Stages.ATTACK;
                return;
            }
        }

        if (circle.getBoundsInParent().intersects(closestMaterial().getBoundsInParent()))
            currentStage = Stages.ATTACKMATERIALS;
    }

    /**
     * The attack state of the finite state machine
     */
    private void attack() {
        Circle circle = new Circle();
        circle.setRadius(2*attackRange);
        circle.setTranslateX(getTranslateX());
        circle.setTranslateY(getTranslateY());
        if (soldiers.size() == 0 || !circle.getBoundsInParent().intersects(closestSoldier().getBoundsInParent())) {
            currentStage = Stages.GOTOMATERIAL;
            return;
        }
        setRotate(90+angleBetween(closestSoldier()));
        closestSoldier().setHp(closestSoldier().getHp() - attack);
    }

    /**
     * The attackMaterials state of the finite state machine.
     */
    private void attackMaterials() {
        Circle circle = new Circle();
        circle.setRadius(2*attackRange);
        circle.setTranslateX(getTranslateX());
        circle.setTranslateY(getTranslateY());
        if (circle.getBoundsInParent().intersects(closestMaterial().getBoundsInParent())) {
            closestMaterial().setDurability(closestMaterial().getDurability() - (attack/100));
            if (closestMaterial().getDurability() <= 0) {
                children.remove(closestMaterial());
                materials.remove(closestMaterial());
            }
        } else currentStage = Stages.GOTOMATERIAL;
    }

    /**
     * This method is responsible for updating the enemy and take actions according to the current state of the
     * finite state machine.
     */
    private void update() {
        if (currentStage == Stages.GOTOMATERIAL) {
            gotoMaterial();
        } else if (currentStage == Stages.ATTACK) {
            attack();
        } else attackMaterials();
        if (hp <= 0) {
            Execute.getPlayer().modifyMoney(Execute.getPlayer().getMoneyAmount()+50);
            children.remove(this);
            enemies.remove(this);
            timer.stop();
        }
    }
}
