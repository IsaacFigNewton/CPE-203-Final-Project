import processing.core.PImage;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

abstract class Mobile extends Active{
    protected PathingStrategy strategy = new AStarPathingStrategy();
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
            //System.out.println("test2");
            if(world.getOccupant(nextPos).isPresent()) {
                System.out.println("test3");
                if (world.getOccupant(nextPos).get().getClass().equals(Swamp.class) && (this instanceof Dude)) {
                    ((Dude) this).transformScared(world, scheduler, imageStore, (Dude) this);
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
        this.getPath(world, destPos);

        //return the next position in the path
        if (this.path.size() > 0)
            return this.path.remove(0);

        return this.position;
    }

}
