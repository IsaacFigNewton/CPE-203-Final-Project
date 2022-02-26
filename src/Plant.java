import processing.core.PImage;

import java.util.List;
import java.util.Random;

abstract class Plant extends Active {
    protected int health;

    public Plant(
                 String id,
                 Point position,
                 List<PImage> images,
                 int animationPeriod,
                 int actionPeriod,
                 int health) {
        super(id, position, images, animationPeriod, actionPeriod);
        this.health = health;
    }

    public int getHealth() {
        return health;
    }

    public static int getNumFromRange(int max, int min) {
        Random rand = new Random();
        return min + rand.nextInt(max - min);
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
            Entity stump = new Stump(this.getId(), this.getPosition(), imageStore.getImageList(Functions.STUMP_KEY));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(stump);

            return true;
        }

        return false;
    }
}
