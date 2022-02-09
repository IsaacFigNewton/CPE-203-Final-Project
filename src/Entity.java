import processing.core.PImage;

import java.util.*;

interface Entity {
    String id = "";
    Point position = new Point(0, 0);
    List<PImage> images = new ArrayList<>();
    int imageIndex = 0;
    int resourceLimit = 0;
    int resourceCount = 0;
    int actionPeriod = 0;
    int animationPeriod = 0;
    int health = 0;
    int healthLimit = 0;

    default String getId() {
        return id;
    }

    default Point getPosition() {
        return position;
    }

    default void setPosition(Point position) {
        this.position = position;
    }

    default List<PImage> getImages() {
        return images;
    }

    default int getImageIndex() {
        return imageIndex;
    }

    default int getResourceLimit() {
        return resourceLimit;
    }

    default int getResourceCount() {
        return resourceCount;
    }

    default int getActionPeriod() {
        return actionPeriod;
    }

    default int getHealth() {
        return health;
    }

    default int getHealthLimit() {
        return healthLimit;
    }

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

    default PImage getCurrentImage() { return this.images.get(this.imageIndex);}
    
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
