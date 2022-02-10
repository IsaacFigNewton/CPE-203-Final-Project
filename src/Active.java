public interface Active extends Dynamic {

    void executeActivity(WorldModel world,
                         ImageStore imageStore,
                         EventScheduler scheduler);

    int getActionPeriod();

    Action createActivityAction(WorldModel world, ImageStore imageStore);
}
