import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * <code>Material</code> is the representation of the material object in the gameplay.
 *
 * @version 1.00
 * @since 1.00
 */
public class Material extends ImageView {
    /**
     * The durability of the material.
     */
    private double durability;
    /**
     * The name of the material.
     */
    private String name;
    /**
     * The type of the material.
     */
    private ElementGenerator.MaterialType type;

    /**
     * The constructor
     *
     * @param name
     * @param durability
     * @param x
     * @param y
     */
    public Material(String name, int durability, double x, double y, ElementGenerator.MaterialType type) {
        this.name = name;
        this.durability = durability;
        setImage(new Image("img/materials/"+(type.ordinal()+1)+".png"));
        setFitWidth(90);
        setFitHeight(90);
        setTranslateX(x);
        setTranslateY(y);
        this.type = type;
    }

    public ElementGenerator.MaterialType getType() {
        return type;
    }

    public double getDurability() {
        return durability;
    }

    public void setDurability(double durability) {
        this.durability = durability;
    }
}
