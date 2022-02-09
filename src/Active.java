public interface Active extends Animated{
    int getActionPeriod();

    default void scheduleAction (EventScheduler eventScheduler, WorldModel world, ImageStore imageStore) {
                eventScheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                this.getActionPeriod());

                eventScheduler.scheduleEvent(this,
                this.createAnimationAction(0),
                0);
    }
}
