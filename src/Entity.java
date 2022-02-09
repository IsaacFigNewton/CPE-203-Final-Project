import processing.core.PImage;

import java.util.*;

interface Entity {

    String getId();

    List<PImage> getImages();

    int getImageIndex();

    void setImageIndex(int index);

    int getResourceLimit();

    int getResourceCount();

    int getActionPeriod();

    int getHealthLimit();

    int getHealth();

    void incrementHealth();

    void decrementHealth();

    Point getPosition();

    void setPosition(Point newPosition);


    static int getNumFromRange(int max, int min) {
        Random rand = new Random();
        return min + rand.nextInt(max - min);
    }

    default void scheduleAction (EventScheduler eventScheduler, WorldModel world, ImageStore imageStore) {
        eventScheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                this.getActionPeriod());
        eventScheduler.scheduleEvent(this,
                this.createAnimationAction(0), 0);
    }

    default Action createAnimationAction(int repeatCount) {
        return new Action(ActionKind.ANIMATION, this, null, null,
                repeatCount);
    }

    default Action createActivityAction(WorldModel world, ImageStore imageStore)
    {
        return new Action(ActionKind.ACTIVITY, this, world, imageStore, 0);
    }

    default PImage getCurrentImage() { return this.getImages().get(this.getImageIndex());}
    
    //void nextImage();

    //Execute various kinds of activities
    void executeActivity(WorldModel world,
                             ImageStore imageStore,
                             EventScheduler scheduler);

//    void scheduleAction (EventScheduler eventScheduler, WorldModel world, ImageStore imageStore);
//
//    void executeTreeActivity(WorldModel world,
//                            ImageStore imageStore,
//                            EventScheduler scheduler);
//
//    void executeFairyActivity(
//            WorldModel world,
//            ImageStore imageStore,
//            EventScheduler scheduler);
//
//    void executeDudeNotFullActivity(
//            WorldModel world,
//            ImageStore imageStore,
//            EventScheduler scheduler);
//
//    void executeDudeFullActivity(
//            WorldModel world,
//            ImageStore imageStore,
//            EventScheduler scheduler);

//    boolean transformNotFull(
//            WorldModel world,
//            EventScheduler scheduler,
//            ImageStore imageStore);
//
//    void transformFull(
//            WorldModel world,
//            EventScheduler scheduler,
//            ImageStore imageStore);
//
//    boolean moveToFairy(
//            WorldModel world,
//            Entity target,
//            EventScheduler scheduler);
//
//    boolean moveToNotFull(
//            WorldModel world,
//            Entity target,
//            EventScheduler scheduler);
//
//    boolean moveToFull(
//            WorldModel world,
//            Entity target,
//            EventScheduler scheduler);

//    Point nextPositionFairy(WorldModel world, Point destPos);
//
//    Point nextPositionDude(WorldModel world, Point destPos);


}
