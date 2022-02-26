import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

        if (target.isPresent() && this.moveTo(world, target.get(), scheduler)) {
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
        // need resource count, though it always starts at 0

        Dude dudeNotFull = new DudeNotFull(this, 0);

        return super.transform(world, scheduler, imageStore, dudeNotFull);
    }
}
