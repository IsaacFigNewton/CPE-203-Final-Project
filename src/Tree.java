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

    public boolean transform(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        if (this.getHealth() <= 0) {
            Entity stump = new Stump(this.getId(), this.getPosition(), imageStore.getImageList(Functions.STUMP_KEY));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(stump);

            return true;
        }

        return false;
    }
}
