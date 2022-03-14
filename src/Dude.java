import processing.core.PImage;

import java.util.List;

abstract class Dude extends Mobile {
    protected int resourceLimit;
    protected PathingStrategy strategy = new AStarPathingStrategy();
    protected Point lastPos;
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
        this.lastPos = null;
    }
    public String getId(){return this.id;}
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
                        || world.getOccupancyCell(p).getClass().equals(Stump.class)
                            || world.getOccupancyCell(p).getClass().equals(Swamp.class)),    // canPassThrough
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

        if(dude instanceof DudeScared){
            dude.executeActivityCondition(world, imageStore, scheduler);
            dude.executeActivityCondition(world, imageStore, scheduler);
            dude.executeActivityCondition(world, imageStore, scheduler);

            Shrek shrek = new Shrek("shrek",this.lastPos, imageStore.getImageList(Functions.SHREK_KEY),Functions.SHREK_ANIMATION_PERIOD,Functions.SHREK_ACTION_PERIOD);

            //schedule shrek's birthing
            scheduler.scheduleEvent(shrek, new SpawnEntity(shrek, world, imageStore), 10);
        }

        return true;
    }

    public boolean transformScared(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore,
            Dude dude, Point nextPos)
    {
        this.lastPos = nextPos;
        Dude dudeScared = new DudeScared(Functions.DUDESCARED_KEY, imageStore.getImageList(Functions.DUDESCARED_KEY), dude);
        dudeScared.scheduleAction(scheduler, world, imageStore);

        return transform(world, scheduler, imageStore, dudeScared);
    }
}
