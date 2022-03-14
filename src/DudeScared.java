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

    public DudeScared(Dude other)
    {
        super(other);
    }


    //Moves in a random 3x3 grid like a chicken with their head cut off.
    public boolean executeActivityCondition(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Random random = new Random();
        Point scaredPos = new Point(random.nextInt(-1, 1),
                random.nextInt(-1, 1));
        while(!world.withinBounds(scaredPos)) {
            scaredPos = new Point(random.nextInt(-1, 1),
                    random.nextInt(-1, 1));
        }
        scaredPos =  new Point(this.position.x + random.nextInt(-1, 1),
                this.position.y + random.nextInt(-1, 1));


        world.moveEntity(this,scaredPos);

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