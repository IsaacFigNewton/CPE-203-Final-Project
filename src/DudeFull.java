import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class DudeFull extends Dude {

    public DudeFull(
            String id,
            Point position,
            List<PImage> images,
            int animationPeriod,
            int actionPeriod,
            int resourceLimit)
    {
        super(id, position, images, animationPeriod, actionPeriod, resourceLimit);
    }

    public DudeFull(Dude other)
    {
        super(other);
    }

    public boolean executeActivityCondition(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> target =
                world.findNearest(this.position, new ArrayList<>(Arrays.asList(House.class)));

        if (target.isPresent() && this.moveToDude(world, target.get(), scheduler,imageStore)) {
            this.transform(world, scheduler, imageStore);
            return false;
        }

        return true;
    }

    public boolean moveToActivity(
            WorldModel world,
            Entity target,
            EventScheduler scheduler) { return true; }

    //transform into a doodNotFull
    public boolean transform(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        //find a potentially valid spawn point around the point where the DudeFull is becoming a DudeNotFull
        Function<Integer, Integer> diff = (i) -> (int)(Math.random() * 2) - 1;
        Point potentialSpawnPoint = new Point(this.position.x + diff.apply(0), this.position.y + diff.apply(0));

        //make sure the point is valid
        while (world.isOccupied(potentialSpawnPoint)) {
            potentialSpawnPoint = new Point(this.position.x + diff.apply(0), this.position.y + diff.apply(0));
        }

        //bring daBaby into this world
        DudeNotFull daBaby = new DudeNotFull(this, 0);
        world.addEntity(daBaby);
        daBaby.scheduleAction(scheduler, world, imageStore);

        // need resource count, though it always starts at 0
        Dude dudeNotFull = new DudeNotFull(this, 0);

        return super.transform(world, scheduler, imageStore, dudeNotFull);
    }
}
