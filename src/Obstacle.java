import processing.core.PImage;

import java.util.List;

public class Obstacle implements Animated{
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int animationPeriod;

    public Obstacle(
    String id,
    Point position,
    List<PImage> images,
    int animationPeriod)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.animationPeriod = animationPeriod;
    }


    public String getId() {
        return id;
    }

    public List<PImage> getImages() {
        return images;
    }

    public int getImageIndex() {
        return imageIndex;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point newPosition) {
        this.position = newPosition;
    }

    public void setImageIndex(int index) { this.imageIndex = index; }

    public int getAnimationPeriod() {
        return this.animationPeriod;
    };
    public void scheduleAction (EventScheduler eventScheduler, WorldModel world, ImageStore imageStore) {
        eventScheduler.scheduleEvent(this,
                this.createAnimationAction(0),
                this.getAnimationPeriod());
    }

}
