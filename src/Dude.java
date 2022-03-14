import processing.core.PImage;

import java.util.List;

abstract class Dude extends Mobile {
    protected int resourceLimit;
    protected PathingStrategy strategy = new AStarPathingStrategy();

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

    protected void getPath(WorldModel world, Point destPos)
    {
        //recalculate path every step
        this.path = strategy.computePath(
                this.position,
                destPos,
                p -> world.withinBounds(p)
                    && (!world.isOccupied(p)
                        || world.getOccupant(p).getClass().equals(Stump.class)),    // canPassThrough
                (p1, p2) -> p1.adjacent(p2),                                        // withinReach
                PathingStrategy.CARDINAL_NEIGHBORS);                                // potentialNeighbours
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

    public boolean transformScared(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore,
            Dude dude)
    {
        Dude dudeScared = new DudeScared(this);

        return transform(world, scheduler, imageStore, dudeScared);
    }
}
