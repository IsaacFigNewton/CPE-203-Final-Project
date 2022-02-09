public interface Animated extends Entity {

    int getAnimationPeriod();

    default void nextImage() {
        this.setImageIndex((this.getImageIndex() + 1) % this.getImages().size());
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
