import processing.core.PImage;

import java.util.List;

public class Obstacle extends Dynamic {

    public Obstacle(
    String id,
    Point position,
    List<PImage> images,
    int animationPeriod)
    {
        super(id, position, images, animationPeriod);
    }

    public void scheduleAction(EventScheduler eventScheduler, WorldModel world, ImageStore imageStore) {
        eventScheduler.scheduleEvent(this,
                this.createAnimationAction(0),
                this.getAnimationPeriod());
    }

}
