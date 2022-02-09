public interface Movable extends Active{
    boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler);

    Point nextPosition(WorldModel world, Point destPos);
}
