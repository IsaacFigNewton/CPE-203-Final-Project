import processing.core.PImage;

import java.util.*;

interface Entity {

    String getId();

    List<PImage> getImages();

    int getImageIndex();

    void setImageIndex(int index);

    Point getPosition();

    void setPosition(Point newPosition);


    static int getNumFromRange(int max, int min) {
        Random rand = new Random();
        return min + rand.nextInt(max - min);
    }

    default PImage getCurrentImage() { return this.getImages().get(this.getImageIndex());}

}
