import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DudeNotFull extends Dude {
    private int resourceCount = 0;

    public DudeNotFull(
            String id,
            Point position,
            List<PImage> images,
            int animationPeriod,
            int actionPeriod,
            int resourceLimit,
            int resourceCount)
    {
        super(id, position, images, animationPeriod, actionPeriod, resourceLimit);
        this.resourceCount = resourceCount;
    }

    public DudeNotFull(Dude other, int resourceCount)
    {
        super(other);
        this.resourceCount = resourceCount;
    }

    public int getResourceCount() { return this.resourceCount; }

    public boolean executeActivityCondition(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> target =
                world.findNearest(this.position, new ArrayList<>(Arrays.asList(Tree.class, Sapling.class)));

        if (!(target.isPresent() && this.moveToDude(world, target.get(), scheduler,imageStore))
                || !this.transform(world, scheduler, imageStore))
        {
            return true;
        }

        return false;
    }

    public boolean moveToActivity(
            WorldModel world,
            Entity target,
            EventScheduler scheduler) {
        this.resourceCount++;
        if (target.getClass() == Tree.class)
            ((Tree)target).decrementHealth();
        return true;
    }

    public boolean transform(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        if (this.resourceCount >= this.resourceLimit) {
            Dude dudeFull = new DudeFull(this);

            return super.transform(world, scheduler, imageStore, dudeFull);
        }

        return false;
    }
}
