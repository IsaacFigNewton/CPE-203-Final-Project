import java.util.*;

import processing.core.PImage;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public final class Entity
{
    public EntityKind kind;
    public String id;
    public Point position;
    public List<PImage> images;
    public int imageIndex;
    public int resourceLimit;
    public int resourceCount;
    public int actionPeriod;
    public int animationPeriod;
    public int health;
    public int healthLimit;

    public Entity(
            EntityKind kind,
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
        this.kind = kind;
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

    //Getters
    //************************************************************************
    public PImage getCurrentImage() { return this.images.get(this.imageIndex);}

    public int getAnimationPeriod() {
        switch (this.kind) {
            case DUDE_FULL:
            case DUDE_NOT_FULL:
            case OBSTACLE:
            case FAIRY:
            case SAPLING:
            case TREE:
                return this.animationPeriod;
            default:
                throw new UnsupportedOperationException(
                        String.format("getAnimationPeriod not supported for %s",
                                this.kind));
        }
    }

    public void nextImage() {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }


    //Execute various kinds of activities
    public void executeSaplingActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        this.health++;
        this.executeTreeActivity(world, imageStore, scheduler);
    }

    public void executeTreeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {

        if (!this.transformPlant(world, scheduler, imageStore)) {

            scheduler.scheduleEvent(this,
                    Functions.createActivityAction(this, world, imageStore),
                    this.actionPeriod);
        }
    }

    public boolean transformPlant( WorldModel world,
                                          EventScheduler scheduler,
                                          ImageStore imageStore)
    {
        if (this.kind == EntityKind.TREE)
        {
            return this.transformTree(this, world, scheduler, imageStore);
        }
        else if (this.kind == EntityKind.SAPLING)
        {
            return transformSapling(this, world, scheduler, imageStore);
        }
        else
        {
            throw new UnsupportedOperationException(
                    String.format("transformPlant not supported for %s", this));
        }
    }

    public static boolean transformTree(
            Entity entity,
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        if (entity.health <= 0) {
            Entity stump = Functions.createStump(entity.id,
                    entity.position,
                    Functions.getImageList(imageStore, Functions.STUMP_KEY));

            Functions.removeEntity(world, entity);
            scheduler.unscheduleAllEvents(entity);

            Functions.addEntity(world, stump);
            scheduler.scheduleActions(stump, world, imageStore);

            return true;
        }

        return false;
    }

    public static boolean transformSapling(
            Entity entity,
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        if (entity.health <= 0) {
            Entity stump = Functions.createStump(entity.id,
                    entity.position,
                    Functions.getImageList(imageStore, Functions.STUMP_KEY));

            Functions.removeEntity(world, entity);
            scheduler.unscheduleAllEvents(entity);

            Functions.addEntity(world, stump);
            scheduler.scheduleActions(stump, world, imageStore);

            return true;
        }
        else if (entity.health >= entity.healthLimit)
        {
            Entity tree = Functions.createTree("tree_" + entity.id,
                    entity.position,
                    Functions.getNumFromRange(Functions.TREE_ACTION_MAX, Functions.TREE_ACTION_MIN),
                    Functions.getNumFromRange(Functions.TREE_ANIMATION_MAX, Functions.TREE_ANIMATION_MIN),
                    Functions.getNumFromRange(Functions.TREE_HEALTH_MAX, Functions.TREE_HEALTH_MIN),
                    Functions.getImageList(imageStore, Functions.TREE_KEY));

            Functions.removeEntity(world, entity);
            scheduler.unscheduleAllEvents( entity);

            Functions.addEntity(world, tree);
            scheduler.scheduleActions(tree, world, imageStore);

            return true;
        }

        return false;
    }
}
