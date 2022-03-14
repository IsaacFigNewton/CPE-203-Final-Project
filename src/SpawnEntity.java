public class SpawnEntity implements Action{
    private Mobile entity;
    private WorldModel world;
    private ImageStore imageStore;

    public SpawnEntity(
            Mobile entity,
            WorldModel world,
            ImageStore imageStore)
    {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;;
    }

    public void executeAction(EventScheduler scheduler) {
        this.world.addEntity(this.entity);
        this.entity.scheduleAction(scheduler, world, imageStore);
    }
}
