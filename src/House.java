import processing.core.PImage;

import java.util.List;

public class House implements Entity {
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;

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

    public void setImageIndex(int index) { this.imageIndex = index; }

}
