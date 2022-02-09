public interface Movable extends Active{
    boolean moveTo(
            WorldModel world,
            EntityOriginal target,
            EventScheduler scheduler);

    Point nextPosition(WorldModel world, Point destPos);
}
