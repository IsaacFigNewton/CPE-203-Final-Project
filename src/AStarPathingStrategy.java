import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//design A* in lab 7 and import code to use for compute path
//refactor imported code into stream format

public class AStarPathingStrategy implements PathingStrategy {

    public boolean heuristic(Point pt, Point start, Point end) {
        if (!pt.equals(start)                                                               //if the candidate point isn't the starting point
                && !pt.equals(end)                                                          //... or the end point
                && Math.abs(end.getX() - pt.getX()) <= Math.abs(end.getX() - start.getX())  //... and it's closer to the end point's x
                && Math.abs(end.getY() - pt.getY()) <= Math.abs(end.getY() - start.getY())) //... and y values than to the start point.
            return true;
        return false;
    }

    public List<Point> computePath(WorldModel world,
                                   Point start,
                                   Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors) {

        PriorityQueue<Point> openList = new PriorityQueue<>(
            (p1, p2) -> (int) (p1.totalDist(end) - p2.totalDist(end))
        );

        HashSet<Point> closedList = new HashSet<>();

        openList.add(start);
        Point pos = start;
        Point currentPoint = start;

        //while there are still unchecked nodes and the goal hasn't been reached
        while (!openList.isEmpty() && !withinReach.test(currentPoint, end)) {
            pos = openList.remove();
            currentPoint = pos;

            //if the current point is within reach of the goal point
            if (withinReach.test(currentPoint, end))
                break;

            closedList.add(pos);

            //compose a list of possible points around the current point
            List<Point> neighbors = Arrays.asList(
                    new Point(currentPoint.getX() + 1, currentPoint.getY(), pos),
                    new Point(currentPoint.getX(), currentPoint.getY() + 1, pos),
                    new Point(currentPoint.getX() - 1, currentPoint.getY(), pos),
                    new Point(currentPoint.getX(), currentPoint.getY() - 1, pos));

            //for each neighbor in the list of neighbors

            for (Point neighbor : neighbors) {
                //access the neighbor's point
                Point neighborPoint = neighbor;

                //test if this is a valid grid cell
                if (canPassThrough.test(neighborPoint) && !closedList.contains(neighbor)) {

                    //causing out of memory errors
                    //if a node is already in the open list and the new value is less than the previous value
                    if (openList.contains(neighbor))
                        openList.remove(neighbor);

                    //add the neighbor node to the open list
                    openList.add(neighbor);
                }
            }

        }

        //Recurse through the top GridNode (which is at the goal) to compose the shortest path
        ArrayList<Point> path = PathingStrategy.buildPath(pos);
//        int desiredPathLength = 5;
        if (PathingStrategy.isValidPath(path, start, end)) //desiredPathLength, start, end))
            return path;
        else
            System.out.println("The path generated failed the isValid() test.");

        return new ArrayList<>();
    }
}
