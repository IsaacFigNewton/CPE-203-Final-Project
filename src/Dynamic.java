public interface Dynamic extends Entity {
    int getAnimationPeriod();

    Action createAnimationAction(int repeatCount);

    void setImageIndex(int index);

    default void nextImage() {
        this.setImageIndex((this.getImageIndex() + 1) % this.getImages().size());
    }

    void scheduleAction (EventScheduler eventScheduler, WorldModel world, ImageStore imageStore);

}