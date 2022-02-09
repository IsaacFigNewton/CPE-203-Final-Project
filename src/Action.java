/**
 * An action that can be taken by an entity
 */
public final class Action
{
    private ActionKind kind;
    private Entity entity;
    private WorldModel world;
    private ImageStore imageStore;
    private int repeatCount;

    public Action(
            ActionKind kind,
            Entity entity,
            WorldModel world,
            ImageStore imageStore,
            int repeatCount)
    {
        this.kind = kind;
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }

    public ActionKind getKind() {
        return kind;
    }

    public Entity getEntity() {
        return entity;
    }

    public WorldModel getWorld() {
        return world;
    }

    public ImageStore getImageStore() {
        return imageStore;
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    public void executeAction(EventScheduler scheduler) {
        switch (this.kind) {
            case ACTIVITY:
                this.executeActivityAction(scheduler);
                break;

            case ANIMATION:
                this.executeAnimationAction(scheduler);
                break;
        }
    }

    private void executeActivityAction(EventScheduler scheduler)
    { this.entity.executeActivity(this.world, this.imageStore, scheduler); }

    private void executeAnimationAction(EventScheduler scheduler)
    {
        if (this.entity instanceof Animated) {
            ((Animated) this.entity).nextImage();

            if (this.repeatCount != 1) {
                scheduler.scheduleEvent(this.entity,
                        this.entity.createAnimationAction(Math.max(this.repeatCount - 1, 0)),
                        ((Animated) this.entity).getAnimationPeriod());
            }
        }
    }




}
