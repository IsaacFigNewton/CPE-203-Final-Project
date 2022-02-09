import processing.core.PImage;

import java.util.*;

interface EntityInterface {
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
    
    EntityKind getKind();

    String getId();

    Point getPosition();

    void setPosition(Point position);

    List<PImage> getImages();

    int getImageIndex();

    int getResourceLimit();

    int getResourceCount() ;

    int getActionPeriod();

    int getHealth();

    int getHealthLimit();

    static int getNumFromRange(int max, int min) {
        Random rand = new Random();
        return min + rand.nextInt(max - min);
    }

    Action createAnimationAction(int repeatCount);

    Action createActivityAction(WorldModel world, ImageStore imageStore);

    PImage getCurrentImage();

    int getAnimationPeriod();
    
    void nextImage();

    //Execute various kinds of activities
    void executeSaplingActivity(WorldModel world,
                             ImageStore imageStore,
                             EventScheduler scheduler);

    void executeTreeActivity(WorldModel world,
                            ImageStore imageStore,
                            EventScheduler scheduler);

    boolean transformPlant( WorldModel world,
                            EventScheduler scheduler,
                            ImageStore imageStore);

    static boolean transformTree(
            Entity entity,
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        if (entity.getHealth() <= 0) {
            Entity stump = entity.getPosition().createStump(entity.getId(), imageStore.getImageList(Functions.STUMP_KEY));

            world.removeEntity(entity);
            scheduler.unscheduleAllEvents(entity);

            world.addEntity(stump);
            stump.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    static boolean transformSapling(
            Entity entity,
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        if (entity.getHealth() <= 0) {
            Entity stump = entity.getPosition().createStump(entity.getId(), imageStore.getImageList(Functions.STUMP_KEY));

            world.removeEntity(entity);
            scheduler.unscheduleAllEvents(entity);

            world.addEntity(stump);
            stump.scheduleActions(scheduler, world, imageStore);

            return true;
        }
        else if (entity.getHealth() >= entity.getHealthLimit())
        {
            Entity tree = entity.getPosition().createTree("tree_" + entity.getId(),
                    getNumFromRange(Functions.TREE_ACTION_MAX, Functions.TREE_ACTION_MIN),
                    getNumFromRange(Functions.TREE_ANIMATION_MAX, Functions.TREE_ANIMATION_MIN),
                    getNumFromRange(Functions.TREE_HEALTH_MAX, Functions.TREE_HEALTH_MIN),
                    imageStore.getImageList(Functions.TREE_KEY));

            world.removeEntity(entity);
            scheduler.unscheduleAllEvents( entity);

            world.addEntity(tree);
            tree.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }


    void executeFairyActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler);

    void executeDudeNotFullActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler);

    void executeDudeFullActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler);

    boolean transformNotFull(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore);

    void transformFull(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore);

    boolean moveToFairy(
            WorldModel world,
            Entity target,
            EventScheduler scheduler);

    boolean moveToNotFull(
            WorldModel world,
            Entity target,
            EventScheduler scheduler);

    boolean moveToFull(
            WorldModel world,
            Entity target,
            EventScheduler scheduler);

    Point nextPositionFairy(WorldModel world, Point destPos);

    Point nextPositionDude(WorldModel world, Point destPos);

    void scheduleKindAction (EventScheduler eventScheduler, WorldModel world, ImageStore imageStore);

    void scheduleActions (EventScheduler eventScheduler, WorldModel world, ImageStore imageStore);

}
