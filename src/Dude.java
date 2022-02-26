import processing.core.PImage;

import java.util.List;

abstract class Dude extends Mobile {
    protected int resourceLimit;

    public Dude(
            String id,
            Point position,
            List<PImage> images,
            int animationPeriod,
            int actionPeriod,
            int resourceLimit)
    {
        super(id, position, images, animationPeriod, actionPeriod);
        this.resourceLimit = resourceLimit;
    }

    public Dude(Dude other)
    {
        super(other.getId(), other.getPosition(), other.getImages(), other.getAnimationPeriod(), other.getActionPeriod());
        this.resourceLimit = other.getResourceLimit();
    }

    public int getResourceLimit () { return this.resourceLimit;}

    public Point nextPosition(WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.getX() - this.position.getX());
        Point newPos = new Point(this.position.getX() + horiz, this.position.getY());

        if (horiz == 0 || world.isOccupied(newPos) && !(world.getOccupancyCell(newPos) instanceof Stump)) {
            int vert = Integer.signum(destPos.getY() - this.position.getY());
            newPos = new Point(this.position.getX(), this.position.getY() + vert);

            if (vert == 0 || world.isOccupied(newPos) &&  !(world.getOccupancyCell(newPos) instanceof Stump)) {
                newPos = this.position;
            }
        }

        return newPos;
    }

    protected abstract boolean executeActivityCondition(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler);

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        if (this.executeActivityCondition(world, imageStore, scheduler))
        {
            scheduler.scheduleEvent(this,
                    this.createActivityAction(world, imageStore),
                    this.actionPeriod);
        }
    }

    public boolean transform(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore,
            Dude dude)
    {
        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(dude);
        dude.scheduleAction(scheduler, world, imageStore);

        return true;
    }

}
