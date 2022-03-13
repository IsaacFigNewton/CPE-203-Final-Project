import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AStarPathingStrategy implements PathingStrategy {

    public List<Point> computePath(Point start,
                                   Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors) {

        //clean data for the previous node and start distance for the start and end nodes
        //this was the bug that took me like 4 hours or something to find
        start.setPreviousNode(null);
        start.setStartDist(0);
        end.setPreviousNode(null);
        end.setStartDist(0);

        PriorityQueue<Point> openList = new PriorityQueue<>(
            (p1, p2) -> (int) (p1.totalDist(end) - p2.totalDist(end))
        );

        HashSet<Point> closedList = new HashSet<>();

        openList.add(start);
        Point currentPoint = start;

        //while the open list isn't empty and the current point isn't adjacent to the goal
        while (!(openList.isEmpty() || withinReach.test(currentPoint, end))) {
            currentPoint = openList.remove();

            closedList.add(currentPoint);

            //compose a list of possible neighbors around the current point
            List<Point> neighbors = potentialNeighbors.apply(currentPoint)              //get the potential neighbors of a point
                    .filter(p -> canPassThrough.test(p) && !closedList.contains(p))     //only use valid grid cells
                    .collect(Collectors.toList());

            //for each neighbor in the list of neighbors
            for (Point neighbor : neighbors) {
                //if a node is already in the open list
                if (openList.contains(neighbor))
                    //try replacing it
                    openList = replacePQNode(openList, neighbor, end);

                //if it's not
                else
                    //add the neighbor node to the open list
                    openList.add(neighbor);
            }
        }

        //Recurse through the top GridNode (which is at the goal) to compose the shortest path
        ArrayList<Point> path = PathingStrategy.buildPath(currentPoint);

//        if (PathingStrategy.isValidPath(path, start, end))
            return path;

//        return new ArrayList<>();
    }

    private static PriorityQueue<Point> replacePQNode (PriorityQueue<Point> oldPQ, Point replacementPoint, Point end) {
//        PriorityQueue<Point> oldPQ = pq;
                PriorityQueue<Point> newPQ = new PriorityQueue<>(
                (p1, p2) -> (int) (p1.totalDist(end) - p2.totalDist(end))
        );

        while (!oldPQ.isEmpty()) {
            Point pointToMove = oldPQ.remove();

            //if the point being moved from pq to newPQ isn't the one to replace
            if (!pointToMove.equals(replacementPoint))
                //add it to the newPQ
                newPQ.add(pointToMove);
            //otherwise, if the point being moved is the one searched for
                // but has a smaller heuristic value than its replacement
            else if (pointToMove.equals(replacementPoint)
                    && pointToMove.totalDist(end) <= replacementPoint.totalDist(end))
                //add it to the newPQ
                newPQ.add(pointToMove);
            //otherwise, if the point being moved is the one searched for
                //and has a larger heuristic value than its replacement
            else
                //replace it
                newPQ.add(replacementPoint);
        }

        return newPQ;
    }
}
