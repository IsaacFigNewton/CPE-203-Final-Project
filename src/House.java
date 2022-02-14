import processing.core.PImage;

import java.util.List;

public class House implements Entity {
    private final String id;
    private Point position;
    private final List<PImage> images;
    private final int imageIndex;

    public House(
    String id,
    Point position,
    List<PImage> images)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
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

}
