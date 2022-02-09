import processing.core.PImage;

import java.util.List;

public class Sapling implements Active, Transformable {
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

    public Sapling(
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
            Entity stump = this.getPosition().createStump(this.getId(), imageStore.getImageList(Functions.STUMP_KEY));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(stump);
            stump.scheduleActions(scheduler, world, imageStore);

            return true;
        }
        else if (this.getHealth() >= this.getHealthLimit())
        {
            Entity tree = this.getPosition().createTree("tree_" + this.getId(),
                    Entity.getNumFromRange(Functions.TREE_ACTION_MAX, Functions.TREE_ACTION_MIN),
                    Entity.getNumFromRange(Functions.TREE_ANIMATION_MAX, Functions.TREE_ANIMATION_MIN),
                    Entity.getNumFromRange(Functions.TREE_HEALTH_MAX, Functions.TREE_HEALTH_MIN),
                    imageStore.getImageList(Functions.TREE_KEY));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents( this);

            world.addEntity(tree);
            tree.scheduleAction(scheduler, world, imageStore);

            return true;
        }

        return false;
    }
}