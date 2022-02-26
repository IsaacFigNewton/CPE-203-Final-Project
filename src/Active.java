import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;

abstract class Active extends Dynamic {
    protected int actionPeriod;

    public Active(String id,
                  Point position,
                  List<PImage> images,
                  int animationPeriod,
                  int actionPeriod) {
        super(id, position, images, animationPeriod);
        this.actionPeriod = actionPeriod;
    }

    public int getActionPeriod() {
        return actionPeriod;
    }

    public abstract void executeActivity(WorldModel world,
                         ImageStore imageStore,
                         EventScheduler scheduler);

//    public boolean checkTargetPresence(WorldModel world, ArrayList<Class<? extends Entity>> targetTypes) {
//        return world.findNearest(this.position, targetTypes).isPresent();
//    }

    public Action createActivityAction(WorldModel world, ImageStore imageStore) {
        return new Activity(this, world, imageStore);
    }

    public void scheduleAction (EventScheduler eventScheduler, WorldModel world, ImageStore imageStore) {
        eventScheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                this.getActionPeriod());

        eventScheduler.scheduleEvent(this,
                this.createAnimationAction(0),
                this.getAnimationPeriod());
    }
}
