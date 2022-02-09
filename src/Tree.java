import processing.core.PImage;

import java.util.List;

public class Tree implements Animated, Transformable {
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int actionPeriod;
    private int animationPeriod;
    private int health;
    private int healthLimit;

    public Tree(
    String id,
    Point position,
    List<PImage> images,
    int actionPeriod,
    int animationPeriod,
    int health,
    int healthLimit)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
        this.health = health;
        this.healthLimit = healthLimit;
    }


    public String getId() {
        return id;
    }

    public List<PImage> getImages() {
        return images;
    }

    public int getImageIndex() {
        return imageIndex;
    }

    public int getActionPeriod() {
        return actionPeriod;
    }

    public int getHealth() {
        return health;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point newPosition) {
        this.position = newPosition;
    }

    public void decrementHealth() { health--; }

    public void setImageIndex(int index) { this.imageIndex = index; }

    public int getAnimationPeriod() {
        return this.animationPeriod;
    }


    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {

        if (!this.transform(world, scheduler, imageStore)) {

            scheduler.scheduleEvent(this,
                    this.createActivityAction(world, imageStore),
                    this.actionPeriod);
        }
    }

    public boolean transform(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        if (this.getHealth() <= 0) {
            Entity stump = this.getPosition().createStump(this.getId(), imageStore.getImageList(Functions.STUMP_KEY));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(stump);
            if (stump instanceof Active)
                ((Active)stump).scheduleAction(scheduler, world, imageStore);

            return true;
        }

        return false;
    }
}
