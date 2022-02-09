import processing.core.PImage;

import java.util.List;

public class House implements Entity {
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int resourceLimit;
    private int resourceCount;
    private int actionPeriod;
    private int animationPeriod;
    private int health;
    private int healthLimit;

    public House(
    String id,
    Point position,
    List<PImage> images,
    int resourceLimit,
    int resourceCount,
    int actionPeriod,
    int animationPeriod,
    int health,
    int healthLimit)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
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

    public int getResourceLimit() {
        return resourceLimit;
    }

    public int getResourceCount() {
        return resourceCount;
    }

    public int getActionPeriod() {
        return actionPeriod;
    }

    public int getHealthLimit() {
        return healthLimit;
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

    public void incrementHealth() { health++; }

    public void decrementHealth() { health--; }

    public void setImageIndex(int index) { this.imageIndex = index; }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

    }
}
