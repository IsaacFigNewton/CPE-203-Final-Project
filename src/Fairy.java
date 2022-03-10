import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

    public Point nextPosition(WorldModel world, Point destPos)
    {
//        if (horiz == 0 || world.isOccupied(newPos) && !(world.getOccupancyCell(newPos) instanceof Stump)) {
//            int vert = Integer.signum(destPos.getY() - this.position.getY());
//            newPos = new Point(this.position.getX(), this.position.getY() + vert);
//
//            if (vert == 0 || world.isOccupied(newPos) &&  !(world.getOccupancyCell(newPos) instanceof Stump)) {
//                newPos = this.position;

//        int horiz = Integer.signum(destPos.getX() - this.position.getX());
//        Point newPos = new Point(this.position.getX() + horiz, this.position.getY());
//
//        if (horiz == 0 || world.isOccupied(newPos)) {
//            int vert = Integer.signum(destPos.getY() - this.position.getY());
//            newPos = new Point(this.position.getX(), this.position.getY() + vert);
//
//            if (vert == 0 || world.isOccupied(newPos)) {
//                newPos = this.position;
//            }
//        }
//
//        return newPos;
//
        //recalculate path every step
        this.path = strategy.computePath(world, this.position,
                destPos);
//                p -> world.withinBounds(p)
//                && !(world.isOccupied(p)),              // canPassThrough
//                (p1, p2) -> p1.adjacent(p2),            // withinReach
//                PathingStrategy.CARDINAL_NEIGHBORS);    // potentialNeighbours

        //return the next position in the path
        if (this.path.size() > 0)
            return this.path.remove(0);
        return this.position;
    }

}
