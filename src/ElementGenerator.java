import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;

import java.util.LinkedList;

/**
 * <code>ElementGenerator</code> is responsible for generating the contents inside the actual game
 * itself.
 *
 * @version 1.00
 * @since 1.00
 */
public class ElementGenerator {
    /**
     * The n value for field of view range.
     */
    private static final int n = 200;
    /**
     * The m value representing the multiplier of the attack range.
     */
    private static final int m = 5;
    /**
     * The indicator of the base multiplier for velocity.
     */
    private static final double v = 0.25;

    /**
     * All types of the soldiers in the game.
     */
    protected enum SoldierType {
        GORO_MAJIMA, DAGIO_DOJIMA, KAORU_SAYAMA, TAIGA_SAEMAJI,
        SOHEI_DOJIMA, KOJI_SHINDO, SOTARO_KOMAKI, SHINTARO_KAZAMA,
        RYUJI_GODA, OSAMRU_KASHIWAGI, MAKOTO_DATE, FUTOSHI_SHIMANO,
        RYO_TAKASHIMA, YUKIO_TERADA, JIRO_KAWARA, TETSU_TACHIBANA
    }

    /**
     * This method is responsible for providing the proper soldier according to the specs given.
     *
     * @param type is the type of the soldier.
     * @param x    is the x position for placing the soldier.
     * @param y    is the y position for placing the soldier.
     * @return a soldier according to the given specs.
     */
    private Soldier getSoldier(SoldierType type, double x, double y) {
        String name = convertName(type);
        int hp = 0;
        int attack = 0;
        int attackRange = 0;
        int fieldOfView = 0;

        if (type == SoldierType.GORO_MAJIMA) {
            hp = 4000;
            attack = 4500;
            fieldOfView = 3 * n;
            attackRange = 4 * m;
        } else if (type == SoldierType.DAGIO_DOJIMA) {
            hp = 4000;
            attack = 4000;
            fieldOfView = n;
            attackRange = 2 * m;
        } else if (type == SoldierType.KAORU_SAYAMA) {
            hp = 4500;
            attack = 4500;
            fieldOfView = 2 * n;
            attackRange = 4 * m;
        } else if (type == SoldierType.TAIGA_SAEMAJI) {
            hp = 7000;
            attack = 5000;
            fieldOfView = n;
            attackRange = m;
        } else if (type == SoldierType.SOHEI_DOJIMA) {
            hp = 3000;
            attack = 3000;
            fieldOfView = n;
            attackRange = m;
        } else if (type == SoldierType.KOJI_SHINDO) {
            hp = 3800;
            attack = 3600;
            fieldOfView = n;
            attackRange = m;
        } else if (type == SoldierType.SOTARO_KOMAKI) {
            hp = 2800;
            attack = 5000;
            fieldOfView = 3 * n;
            attackRange = m;
        } else if (type == SoldierType.SHINTARO_KAZAMA) {
            hp = 4500;
            attack = 4500;
            fieldOfView = 2 * n;
            attackRange = m;
        } else if (type == SoldierType.RYUJI_GODA) {
            hp = 5000;
            attack = 5000;
            fieldOfView = n;
            attackRange = 2 * m;
        } else if (type == SoldierType.OSAMRU_KASHIWAGI) {
            hp = 4000;
            attack = 3000;
            fieldOfView = 2 * n;
            attackRange = 50;

        } else if (type == SoldierType.MAKOTO_DATE) {
            hp = 4500;
            attack = 1800;
            fieldOfView = 2 * n;
            attackRange = 3 * m;
        } else if (type == SoldierType.FUTOSHI_SHIMANO) {
            hp = 4200;
            attack = 4000;
            fieldOfView = 2 * n;
            attackRange = m;
        } else if (type == SoldierType.RYO_TAKASHIMA) {
            hp = 3600;
            attack = 3800;
            fieldOfView = n;
            attackRange = m;
        } else if (type == SoldierType.YUKIO_TERADA) {
            hp = 4000;
            attack = 3000;
            fieldOfView = n;
            attackRange = m;
        } else if (type == SoldierType.JIRO_KAWARA) {
            hp = 5500;
            attack = 3200;
            fieldOfView = 3 * n;
            attackRange = 3 * m;
        } else {
            hp = 5600;
            attack = 4000;
            fieldOfView = 2 * n;
            attackRange = m;
        }
        return new Soldier(enemies, obstacles, soldiers, children, attack, attackRange, hp, name, fieldOfView, x, y, type);
    }

    /**
     * This method is responsible for adding a soldier to the game.
     *
     * @param type is the type of the soldier.
     * @param x    is the x position of the soldier.
     * @param y    is the y position of the soldier.
     */
    public void addSoldier(SoldierType type, double x, double y) {
        Soldier soldier = getSoldier(type, x, y);
        soldier.setTranslateX(x);
        soldier.setTranslateY(y);
        soldiers.add(soldier);
        children.add(soldier);
    }

    /**
     * All of the variations of the materials in the game.
     */
    protected enum MaterialType {
        CONTAINER, TRUCK, POWER_SHOVEL, VAN, STEEL_FRAMEWORK
    }

    /**
     * The enemies linkedlist.
     */
    private LinkedList<Enemy> enemies;
    /**
     * The soldiers linkedlist.
     */
    private LinkedList<Soldier> soldiers;
    /**
     * The materials linkedlist.
     */
    private LinkedList<Material> materials;
    /**
     * The children of the default display.
     */
    private ObservableList<Node> children;
    /**
     * The obstacles that is not passable by anyone in the game.
     */
    private Rectangle[] obstacles;

    /**
     * The constructor
     *
     * @param enemies
     * @param soldiers
     * @param materials
     * @param children
     * @param obstacles
     */
    public ElementGenerator(LinkedList<Enemy> enemies, LinkedList<Soldier> soldiers, LinkedList<Material> materials, ObservableList<Node> children, Rectangle[] obstacles) {
        this.enemies = enemies;
        this.soldiers = soldiers;
        this.materials = materials;
        this.children = children;
        this.obstacles = obstacles;
    }

    /**
     * All possible enemy types in the game.
     */
    protected enum EnemyType {
        RED_SOLDIER, YELLOW_SOLDIER, BOSS_LEVEL_1, BOSS_LEVEL_2,
        GRAY_SOLDIER, GREEN_SOLDIER, GOLD_SOLDIER
    }

    /**
     * This method is responsible for converting mainly the name of the enums to a proper format for
     * representation.
     *
     * @param obj is intended for enums
     * @return a properly formatted string for representing the name of the objects.
     */
    public static String convertName(Object obj) {
        String wrd = "";
        boolean cap = true;
        for (int i = 0; i < obj.toString().length(); i++) {
            if (cap) {
                wrd += obj.toString().toUpperCase().charAt(i);
                cap = false;
            } else if (obj.toString().charAt(i) == '_') {
                wrd += " ";
                cap = true;
            } else {
                wrd += obj.toString().toLowerCase().charAt(i);
            }
        }
        return wrd;
    }

    /**
     * This method would provide a material according to the specs provided in the table.
     *
     * @param type is the type of the material
     * @param x    is the x position
     * @param y    is the y position
     * @return a material with proper values.
     */
    private Material getMaterial(MaterialType type, double x, double y) {
        String name = convertName(type);
        int durability = 0;
        if (type == MaterialType.CONTAINER) {
            durability = 3000;
        } else if (type == MaterialType.TRUCK) {
            durability = 5000;
        } else if (type == MaterialType.POWER_SHOVEL) {
            durability = 11000;
        } else if (type == MaterialType.VAN) {
            durability = 6000;
        } else durability = 8500;
        return new Material(name, durability, x, y, type);
    }

    /**
     * This method is responsible for adding a material in the proper position
     *
     * @param type is the type of the material
     * @param x    is the x position
     * @param y    is the y position
     */
    public void addMaterial(MaterialType type, double x, double y) {
        Material m = getMaterial(type, x, y);
        materials.add(m);
        children.add(m);
    }

    /**
     * This method is responsible for creating an enemy according to the given specs.
     *
     * @param type is the type of the enemy.
     * @param x    is the x position of the enemy.
     * @param y    is the y position of the enemy.
     * @return an enemy with the given specs.
     */
    private Enemy getEnemy(EnemyType type, double x, double y) {
        String name = convertName(type);
        int health = 0;
        int attack = 0;
        int attackRange = 0;
        double velocity;
        if (type == EnemyType.RED_SOLDIER) {
            health = 1000;
            attack = 500;
            attackRange = m;
            velocity = 3*v;
        } else if (type == EnemyType.YELLOW_SOLDIER) {
            health = 800;
            attack = 1800;
            attackRange = m;
            velocity = 2*v;
        } else if (type == EnemyType.BOSS_LEVEL_1) {
            health = 8000;
            attack = 5000;
            attackRange = m;
            velocity = 2*v;
        } else if (type == EnemyType.BOSS_LEVEL_2) {
            health = 20000;
            attack = 12000;
            attackRange = m;
            velocity = 2*v;
        } else if (type == EnemyType.GRAY_SOLDIER) {
            health = 1000;
            attack = 800;
            attackRange = m;
            velocity = 2*v;
        } else if (type == EnemyType.GREEN_SOLDIER) {
            health = 1500;
            attack = 700;
            attackRange = m;
            velocity = 2*v;
        } else {
            health = 800;
            attack = 2000;
            attackRange = 5 * m;
            velocity = v;
        }
        return new Enemy(obstacles, materials, soldiers, enemies, children, health, attack, attackRange, name, type, velocity);
    }

    /**
     * This method adds an enemy according to the given type and position.
     *
     * @param type is the type of the enemy
     * @param x    is the x position of the enemy.
     * @param y    is the y position of the enemy.
     */
    public void addEnemy(EnemyType type, double x, double y) {
        Enemy enemy = getEnemy(type, x, y);
        enemy.setTranslateX(x);
        enemy.setTranslateY(y);
        enemies.add(enemy);
        children.add(enemy);
    }
}
