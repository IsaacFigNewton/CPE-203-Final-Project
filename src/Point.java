import processing.core.PImage;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A simple class representing a location in 2D space.
 */
public final class Point
{
    private final int x;
    private final int y;

    private Point previousNode;
    private int startDist;

    private Point (int x, int y, Point prev, int startDist) {
        assert(prev != null);

        this.x = x;
        this.y = y;
        this.previousNode = prev;
        this.startDist = startDist;
    }

    public Point (int x, int y, Point prev) {
        this(x, y, prev, prev.startDist + 1);
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { return x;}
    public int getY() { return y;}

    public Point getPreviousNode() {
        return previousNode;
    }

    public void setPreviousNode(Point previousNode) {
        this.previousNode = previousNode;
    }

    public int getStartDist() {
        return startDist;
    }

    public void setStartDist(int startDist) {
        this.startDist = startDist;
    }

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

    public boolean adjacent(Point other) {
        return (this.x == other.x && Math.abs(this.y - other.y) == 1) || (this.y == other.y
                && Math.abs(this.x - other.x) == 1);
    }

    //manhattan distance from the current point to the end point
    public int endDist(Point targetPt) {
        return  Math.abs(this.x - targetPt.x) + Math.abs(this.y - targetPt.y); //this.distanceSquared(targetPt); //
    };

    public double totalDist(Point targetPt) {
        return this.startDist + this.endDist(targetPt);
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

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Point point = (Point) o;
//        return x == point.x && y == point.y;
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(x, y);
//    }
}
