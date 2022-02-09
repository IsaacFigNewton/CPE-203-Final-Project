public interface Animated extends Entity {

    default Action createAnimationAction(int repeatCount) {
        return new Action(ActionKind.ANIMATION, this, null, null,
                repeatCount);
    }

    default Action createActivityAction(WorldModel world, ImageStore imageStore)
    {
        return new Action(ActionKind.ACTIVITY, this, world, imageStore, 0);
    }

    int getAnimationPeriod();

    default void nextImage() {
        this.setImageIndex((this.getImageIndex() + 1) % this.getImages().size());
    }

    //additional problems created by casting animated entities to active ones
    default void scheduleAction (EventScheduler eventScheduler, WorldModel world, ImageStore imageStore) {
        eventScheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                this.getActionPeriod());

        eventScheduler.scheduleEvent(this,
                this.createAnimationAction(0),
                this.getAnimationPeriod());
    }

}
