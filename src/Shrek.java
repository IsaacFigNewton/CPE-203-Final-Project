import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Shrek extends Mobile {
    private PathingStrategy strategy = new AStarPathingStrategy();
    private boolean hungie = true;

    public Shrek(
            String id,
            Point position,
            List<PImage> images,
            int animationPeriod,
            int actionPeriod)
    {
        super(id, position, images, animationPeriod, actionPeriod);
    }

    public Shrek(Shrek other)
    {
        super(other.getId(), other.getPosition(), other.getImages(), other.getAnimationPeriod(), other.getActionPeriod());
    }

    protected void getPath(WorldModel world, Point destPos)
    {
        //recalculate path every step
        this.path = strategy.computePath(
                this.position,
                destPos,
                p -> world.withinBounds(p)
                        && !world.isOccupied(p),                                    // canPassThrough
                (p1, p2) -> p1.adjacent(p2),                                        // withinReach
                PathingStrategy.CARDINAL_NEIGHBORS);                                // potentialNeighbours
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        //if hungry
        if (this.hungie) {

            Optional<Entity> target =
                    world.findNearest(this.position, new ArrayList<>(Arrays.asList(Dude.class)));

            if (!(target.isPresent() && this.moveTo(world, target.get(), scheduler))) {
                scheduler.scheduleEvent(this,
                        this.createActivityAction(world, imageStore),
                        this.actionPeriod);

                this.hungie = false;
//                ShrekFull.scheduleAction(scheduler, world, imageStore);
            }

        //else if full
        } else {
            //return to swamp
            Optional<Entity> target =
                    world.findNearest(this.position, new ArrayList<>(Arrays.asList(Swamp.class)));


            if (!(target.isPresent() && this.moveTo(world, target.get(), scheduler))) {
                world.removeEntity(this);
                scheduler.unscheduleAllEvents(this);
            }

        }
    }

    public boolean moveToActivity(
            WorldModel world,
            Entity target,
            EventScheduler scheduler) {
        if (this.hungie && target.getClass() == Dude.class) {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
        }
        return true;
    }

}
