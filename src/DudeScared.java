import processing.core.PImage;

import java.util.*;

public class DudeScared extends Dude{
    public DudeScared(
            String id,
            Point position,
            List<PImage> images,
            int animationPeriod,
            int actionPeriod,
            int resourceLimit)
    {
        super(id, position, images, animationPeriod, actionPeriod, resourceLimit);
    }

    public DudeScared(
            String id,
            List<PImage> images,
            Dude other)
    {
        super(other);
        this.id = id;
        this.images = images;
    }


    //Moves in a random 3x3 grid like a chicken with their head cut off.
    public boolean executeActivityCondition(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Random random = new Random();
        Point scaredPos = new Point(this.position.x + random.nextInt(-1, 1),
                this.position.y + random.nextInt(-1, 1));

        //while the scaredPos is occupied and not
        while(!(!world.isOccupied(scaredPos)
                //make sure a new Swamp can be added on top of an old one to avoid the program breaking
                || world.getOccupant(scaredPos).stream()
                .findFirst()
                .orElse(null) instanceof Swamp)) {
            scaredPos = new Point(this.position.x + random.nextInt(-1, 1),
                    this.position.y + random.nextInt(-1, 1));
        }

        world.moveEntity(this, scaredPos);

        return true;
    }

    public boolean moveToActivity(
            WorldModel world,
            Entity target,
            EventScheduler scheduler) { return true; }

    //Remove dude when eaten
    public boolean transform(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);
        return true;
    }
}