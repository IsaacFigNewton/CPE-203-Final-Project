import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Fairy extends Mobile {

    public Fairy(
            String id,
            Point position,
            List<PImage> images,
            int animationPeriod,
            int actionPeriod)
    {
        super(id, position, images, animationPeriod, actionPeriod);
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> fairyTarget =
                world.findNearest(this.position, new ArrayList<>(Arrays.asList(Stump.class)));

        if (fairyTarget.isPresent()) {
            Point tgtPos = fairyTarget.get().getPosition();

            if (this.moveTo(world, fairyTarget.get(), scheduler)) {
                // health starts at 0 and builds up until ready to convert to Tree
                Active sapling = new Sapling("sapling_" + this.id, tgtPos,
                        imageStore.getImageList(Functions.SAPLING_KEY),
                        Functions.SAPLING_ACTION_ANIMATION_PERIOD,
                        Functions.SAPLING_ACTION_ANIMATION_PERIOD,
                        0, Functions.SAPLING_HEALTH_LIMIT);

                world.addEntity(sapling);
                if (sapling instanceof Active)
                    sapling.scheduleAction(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                this.actionPeriod);
    }

    public boolean moveToActivity(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
    }

    public Point nextPosition(WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.getX() - this.position.getX());
        Point newPos = new Point(this.position.getX() + horiz, this.position.getY());

        if (horiz == 0 || world.isOccupied(newPos)) {
            int vert = Integer.signum(destPos.getY() - this.position.getY());
            newPos = new Point(this.position.getX(), this.position.getY() + vert);

            if (vert == 0 || world.isOccupied(newPos)) {
                newPos = this.position;
            }
        }

        return newPos;
    }

}
