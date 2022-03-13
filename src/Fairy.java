import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class Fairy extends Mobile {
    protected PathingStrategy strategy = new AStarPathingStrategy();

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

    protected void getPath(WorldModel world, Point destPos)
    {
        //recalculate path every step
        this.path = strategy.computePath(
                this.position,
                destPos,
                p -> world.withinBounds(p)
                        && !(world.isOccupied(p)),              // canPassThrough
                (p1, p2) -> p1.adjacent(p2),            // withinReach
                PathingStrategy.CARDINAL_NEIGHBORS);    // potentialNeighbours
    }

}
