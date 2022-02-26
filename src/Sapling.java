import processing.core.PImage;

import java.util.List;

public class Sapling extends Plant {
    private final int healthLimit;

    public Sapling(
    String id,
    Point position,
    List<PImage> images,
    int animationPeriod,
    int actionPeriod,
    int health,
    int healthLimit)
    {
        super(id, position, images, animationPeriod, actionPeriod, health);
        this.healthLimit = healthLimit;
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        this.health++;
        super.executeActivity(world, imageStore, scheduler);
    }

    public boolean transform(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        if (this.getHealth() >= this.healthLimit)
        {
            Entity tree = new Tree("tree_" + this.getId(), this.getPosition(),
                    imageStore.getImageList(Functions.TREE_KEY),
                    Plant.getNumFromRange(Functions.TREE_ANIMATION_MAX, Functions.TREE_ANIMATION_MIN),
                    Plant.getNumFromRange(Functions.TREE_ACTION_MAX, Functions.TREE_ACTION_MIN),
                    Plant.getNumFromRange(Functions.TREE_HEALTH_MAX, Functions.TREE_HEALTH_MIN));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents( this);

            world.addEntity(tree);
            if (tree instanceof Active)
                ((Active)tree).scheduleAction(scheduler, world, imageStore);

            return true;
        }

        return super.transform(world, scheduler, imageStore);
    }

}
