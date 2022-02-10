import processing.core.PImage;

import java.util.*;

interface Entity {

    String getId();

    List<PImage> getImages();

    int getImageIndex();

    Point getPosition();

    void setPosition(Point newPosition);

    static int getNumFromRange(int max, int min) {
        Random rand = new Random();
        return min + rand.nextInt(max - min);
    }

    default PImage getCurrentImage() { return this.getImages().get(this.getImageIndex());}

    //Execute various kinds of activities
//    void executeActivity(WorldModel world,
//                         ImageStore imageStore,
//                         EventScheduler scheduler);
}
