import processing.core.PImage;

import java.util.List;

abstract class Dynamic extends Entity {

    protected int animationPeriod;

    public Dynamic(
            String id,
            Point position,
            List<PImage> images,
            int animationPeriod) {
        super(id, position, images);
        this.animationPeriod = animationPeriod;
    }

    public void setImageIndex(int index) { this.imageIndex = index; }

    public void nextImage() {
        this.setImageIndex((this.getImageIndex() + 1) % this.getImages().size());
    }

    public int getAnimationPeriod() {
        return this.animationPeriod;
    }

    public Action createAnimationAction(int repeatCount) {
        return new Animation(this, repeatCount);
    }

    public abstract void scheduleAction (EventScheduler eventScheduler, WorldModel world, ImageStore imageStore);

}
