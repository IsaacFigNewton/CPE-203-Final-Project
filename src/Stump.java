import processing.core.PImage;

import java.util.List;

public class Stump implements Entity {
    private final String id;
    private Point position;
    private final List<PImage> images;

    public Stump(
    String id,
    Point position,
    List<PImage> images)
    {
        this.id = id;
        this.position = position;
        this.images = images;
    }

    public String getId() {
        return id;
    }

    public List<PImage> getImages() {
        return images;
    }

    public int getImageIndex() {
        return 0;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point newPosition) {
        this.position = newPosition;
    }
}
