import processing.core.PImage;

import java.util.*;

interface Entity {

    //getters/setters for entity-wide variables

    String getId();

    List<PImage> getImages();

    int getImageIndex();

    void setImageIndex(int index);

    int getHealthLimit();

    int getHealth();

    void decrementHealth();

    Point getPosition();

    void setPosition(Point newPosition);


    //review for possible refactoring

    int getResourceLimit();

    int getResourceCount();


    static int getNumFromRange(int max, int min) {
        Random rand = new Random();
        return min + rand.nextInt(max - min);
    }

    default PImage getCurrentImage() { return this.getImages().get(this.getImageIndex());}

    //Execute various kinds of activities
    void executeActivity(WorldModel world,
                             ImageStore imageStore,
                             EventScheduler scheduler);

}
