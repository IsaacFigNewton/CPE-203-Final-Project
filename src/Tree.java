import processing.core.PImage;

import java.util.List;

public class Tree extends Plant {

    public Tree(
    String id,
    Point position,
    List<PImage> images,
    int animationPeriod,
    int actionPeriod,
    int health)
    {
        super(id, position, images, animationPeriod, actionPeriod, health);
    }

    public void decrementHealth() { health--; }

}
