public interface ActivityEnjoyer extends Animated{
    //Execute various kinds of activities
    void executeActivity(WorldModel world,
                         ImageStore imageStore,
                         EventScheduler scheduler);
}
