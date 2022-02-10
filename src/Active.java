public interface Active extends Dynamic {

    void executeActivity(WorldModel world,
                         ImageStore imageStore,
                         EventScheduler scheduler);

    int getActionPeriod();

    Action createActivityAction(WorldModel world, ImageStore imageStore);

//    public Action createAnimationAction(int repeatCount) {
//        return new Action(ActionKind.ANIMATION, this, null, null,
//                repeatCount);
//    }
//
//    public Action createActivityAction(WorldModel world, ImageStore imageStore) {
//        return new Action(ActionKind.ACTIVITY, this, world, imageStore, 0);
//    }




//    default void scheduleAction (EventScheduler eventScheduler, WorldModel world, ImageStore imageStore) {
//        eventScheduler.scheduleEvent(this,
//                this.createActivityAction(world, imageStore),
//                this.getActionPeriod());
//
//        eventScheduler.scheduleEvent(this,
//                this.createAnimationAction(0),
//                this.getAnimationPeriod());
//    }
}
