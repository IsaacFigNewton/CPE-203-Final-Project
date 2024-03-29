import processing.core.PImage;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

abstract class Mobile extends Active{
    protected PathingStrategy strategy = new AStarPathingStrategy2();
    protected List<Point> path = new ArrayList<>();

    public Mobile(String id,
                  Point position,
                  List<PImage> images,
                  int animationPeriod,
                  int actionPeriod) {
        super(id, position, images, animationPeriod, actionPeriod);
    }

    public boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (this.position.adjacent(target.getPosition())) {
            return this.moveToActivity(world, target, scheduler);
        }
        else {
            Point nextPos = this.nextPosition(world, target.getPosition());

            if (!this.position.equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    public boolean moveToDude(
            WorldModel world,
            Entity target,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        if (this.position.adjacent(target.getPosition())) {
            return this.moveToActivity(world, target, scheduler);
        }

        else {
            Point nextPos = this.nextPosition(world, target.getPosition());
            if(world.getOccupant(nextPos).isPresent()) {
                System.out.println("test2");

                if (world.getOccupant(nextPos).get().getClass().equals(Swamp.class) && (this instanceof Dude)) {
                    System.out.println("test3");

                    Swamp nextSwamp = (Swamp) world.getOccupant(nextPos).get();
                    if(!nextSwamp.hasShrek()){
                        ((Dude) this).transformScared(world, scheduler, imageStore, (Dude) this,this.position);
                        nextSwamp.setHasShrek(true);
                        return false;
                    }
                }
            }
            if (!this.position.equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    protected abstract boolean moveToActivity(
            WorldModel world,
            Entity target,
            EventScheduler scheduler);

    protected abstract void getPath(WorldModel world, Point destPos);

    public Point nextPosition(WorldModel world, Point destPos)
    {
        //recalculate path every step
//        this.getPath(world, destPos);
//
//        //return the next position in the path
//        if (this.path.size() > 0)
//            return this.path.remove(0);
//
//        return this.position;
        PathingStrategy AXStrat = new AStarPathingStrategy2();
        List<Point> PointsList = AXStrat.computePath(getPosition(), destPos, canPassThrough(world), withinReach(), PathingStrategy.CARDINAL_NEIGHBORS);
        if (PointsList.size() != 1 && PointsList.get(1) != null)
            return PointsList.get(1);
        return getPosition();
    }
    public BiPredicate<Point, Point> withinReach(){
        return Point::adjacent;
    }

    public Predicate<Point> canPassThrough(WorldModel world) {
        if (this instanceof DudeScared)
            return p -> (world.withinBounds(p) && (!world.isOccupied(p) || world.getOccupancyCell(p).getClass() == Stump.class ));
        return p -> (world.withinBounds(p) && (!world.isOccupied(p) || world.getOccupancyCell(p).getClass() == Stump.class || world.getOccupancyCell(p).getClass() == Swamp.class ));
    }

}
