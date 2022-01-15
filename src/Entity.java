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

        if (!transformPlant(this, world, scheduler, imageStore)) {

            scheduler.scheduleEvent(this,
                    createActivityAction(this, world, imageStore),
                    this.actionPeriod);
        }
    }

}
