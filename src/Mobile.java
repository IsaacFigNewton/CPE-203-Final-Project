import processing.core.PImage;

import java.util.List;
import java.util.Optional;

abstract class Mobile extends Active{

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

    protected abstract boolean moveToActivity(
            WorldModel world,
            Entity target,
            EventScheduler scheduler);

    public abstract Point nextPosition(WorldModel world, Point destPos);
}
