import processing.core.PImage;

import java.util.List;

public class Sapling  extends Plant implements Transformable {
    private String id;
    private Point position;
    private final List<PImage> images;
    private int imageIndex;
    private final int actionPeriod;
    private final int animationPeriod;
    private final int healthLimit;

    public Sapling(
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

    public int getHealthLimit() { return healthLimit; }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point newPosition) {
        this.position = newPosition;
    }

    public void setImageIndex(int index) { this.imageIndex = index; }

    public Action createAnimationAction(int repeatCount) {
        return new Animation(this, repeatCount);
    }

    public Action createActivityAction(WorldModel world, ImageStore imageStore) {
        return new Activity(this, world, imageStore);
    }

    public int getAnimationPeriod() {
        return this.animationPeriod;
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        this.health++;
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
            Entity stump = new Stump(this.getId(), this.getPosition(), imageStore.getImageList(Functions.STUMP_KEY));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(stump);

            return true;
        }
        else if (this.getHealth() >= this.getHealthLimit())
        {
            Entity tree = new Tree("tree_" + this.getId(), this.getPosition(),
                    imageStore.getImageList(Functions.TREE_KEY),
                    Plant.getNumFromRange(Functions.TREE_ACTION_MAX, Functions.TREE_ACTION_MIN),
                    Plant.getNumFromRange(Functions.TREE_ANIMATION_MAX, Functions.TREE_ANIMATION_MIN),
                    Plant.getNumFromRange(Functions.TREE_HEALTH_MAX, Functions.TREE_HEALTH_MIN));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents( this);

            world.addEntity(tree);
            if (tree instanceof Active)
                ((Active)tree).scheduleAction(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    public void scheduleAction (EventScheduler eventScheduler, WorldModel world, ImageStore imageStore) {
        eventScheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                this.getActionPeriod());

        eventScheduler.scheduleEvent(this,
                this.createAnimationAction(0),
                this.getAnimationPeriod());
    }
}
