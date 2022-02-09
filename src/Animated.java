public interface Animated extends Entity {
    default int getAnimationPeriod() {
        return this.animationPeriod;
    };

    default void nextImage() {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }

    default void scheduleAction (EventScheduler eventScheduler, WorldModel world, ImageStore imageStore) {
        eventScheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                this.getActionPeriod());
        eventScheduler.scheduleEvent(this,
                this.createAnimationAction(0),
                this.getAnimationPeriod());
    }
}
