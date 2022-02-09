import processing.core.PImage;

import java.util.List;
import java.util.Optional;

/**
 * A simple class representing a location in 2D space.
 */
public final class Point
{
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { return x;}
    public int getY() { return y;}

    public Optional<Entity> nearestEntity(List<Entity> entities)
    {
        if (entities.isEmpty()) {
            return Optional.empty();
        }
        else {
            Entity nearest = entities.get(0);
            int nearestDistance = this.distanceSquared(nearest.getPosition());

            for (Entity other : entities) {
                int otherDistance = this.distanceSquared(other.getPosition());

                if (otherDistance < nearestDistance) {
                    nearest = other;
                    nearestDistance = otherDistance;
                }
            }

            return Optional.of(nearest);
        }
    }

    private int distanceSquared(Point other) {
        int deltaX = this.x - other.x;
        int deltaY = this.y - other.y;

        return deltaX * deltaX + deltaY * deltaY;
    }

    public Entity createHouse(
            String id, List<PImage> images)
    {
        return new House(id, this, images);
    }

    public Entity createObstacle(String id, int animationPeriod, List<PImage> images)
    {
        return new Obstacle(id, this, images, animationPeriod);
    }

    public Entity createTree(
            String id,
            int actionPeriod,
            int animationPeriod,
            int health,
            List<PImage> images)
    {
        return new Tree(id, this, images, actionPeriod, animationPeriod, health, 0);
    }

    public Entity createStump(String id, List<PImage> images)
    {
        return new Stump(id, this, images);
    }

    // health starts at 0 and builds up until ready to convert to Tree
    public Entity createSapling(String id, List<PImage> images)
    {
        return new Sapling(id, this, images, Functions.SAPLING_ACTION_ANIMATION_PERIOD,
                Functions.SAPLING_ACTION_ANIMATION_PERIOD, 0, Functions.SAPLING_HEALTH_LIMIT);
    }

    public Entity createFairy(
            String id,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images)
    {
        return new Fairy(id, this, images, actionPeriod, animationPeriod);
    }

    // need resource count, though it always starts at 0
    public Entity createDudeNotFull(
            String id,
            int actionPeriod,
            int animationPeriod,
            int resourceLimit,
            List<PImage> images)
    {
        return new DudeNotFull(id, this, images, resourceLimit, 0,
                actionPeriod, animationPeriod);
    }

    // don't technically need resource count ... full
    public Entity createDudeFull(
            String id,
            int actionPeriod,
            int animationPeriod,
            int resourceLimit,
            List<PImage> images) {
        return new DudeFull(id, this, images, resourceLimit, 0,
                actionPeriod, animationPeriod);
    }

    public boolean adjacent(Point other) {
        return (this.x == other.x && Math.abs(this.y - other.y) == 1) || (this.y == other.y
                && Math.abs(this.x - other.x) == 1);
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }

    public boolean equals(Object other) {
        return other instanceof Point && ((Point)other).x == this.x
                && ((Point)other).y == this.y;
    }

    public int hashCode() {
        int result = 17;
        result = result * 31 + x;
        result = result * 31 + y;
        return result;
    }
}
