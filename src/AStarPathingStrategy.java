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

    public List<Point> computePath(WorldModel world, Point start, Point end) {
//                                   Predicate<Point> canPassThrough,
//                                   BiPredicate<Point, Point> withinReach,
//                                   Function<Point, Stream<Point>> potentialNeighbors) {

        PriorityQueue<WorldNode> openList = new PriorityQueue<>(
            (p1, p2) -> (int) (p1.totalDist(end) - p2.totalDist(end))
        );
        HashSet<WorldNode> closedList = new HashSet<>();

        WorldNode startNode = new WorldNode(start);
        openList.add(startNode);
        WorldNode pos = startNode;
        Point currentPoint = start;

        //while there are still unchecked nodes and the goal hasn't been reached
        while (!openList.isEmpty() && world.getOccupant(currentPoint) != world.getOccupant(end)) {
            pos = openList.remove();
            currentPoint = pos.getPoint();

            //if the current point is within reach of the goal point
            if (PathingStrategy.adjacent(currentPoint, end))
                break;

            closedList.add(pos);

            //compose a list of possible points around the current point
            List<WorldNode> neighbors = Arrays.asList(
                    new WorldNode(new Point(currentPoint.getX() + 1, currentPoint.getY()), pos),
                    new WorldNode(new Point(currentPoint.getX(), currentPoint.getY() + 1), pos),
                    new WorldNode(new Point(currentPoint.getX() - 1, currentPoint.getY()), pos),
                    new WorldNode(new Point(currentPoint.getX(), currentPoint.getY() - 1), pos));

            //for each neighbor in the list of neighbors
            for (WorldNode neighbor : neighbors) {
                //access the neighbor's point
                Point neighborPoint = neighbor.getPoint();

                //test if this is a valid grid cell
                if (world.withinBounds(neighborPoint) &&
                        world.isOccupied(neighborPoint) &&
                        !closedList.contains(neighbor)) {

                    openList.add(neighbor);
                }
            }

        }


//        ArrayList<Point> path = buildPath(openList.peek());
//        int desiredPathLength = 5;
//        if (isValidPath(path, desiredPathLength, start, end))
//            return path;
//        else
//            System.out.println("A Star might've failed");
//
//        return new ArrayList<>();

        //Recurse through the top GridNode (which is at the goal) to compose the shortest path
            return PathingStrategy.buildPath(pos);
        
//        //returns a list consisting of the next point according to the heuristic above
//        return potentialNeighbors.apply(start)  //get the neighboring cells around the starting point
//                .filter(canPassThrough)         //filter out any neighbors that have an obstacle or something in them
//                .filter((pt) -> heuristic(pt, start, end))  //heuristic
//                .limit(1)                       //return 1 neighbor that matched all criteria (or the first checked one)
//                .collect(Collectors.toList());  //return a list of that 1 neighbor
    }
//
//    private boolean adjacent(Point p1, Point p2) {
//        int absXDist = Math.abs(p1.getX() - p2.getX());
//        int absYDist = Math.abs(p1.getY() - p2.getY());
//
//        if (absXDist <= 1 && absYDist <= 1 && absXDist + absYDist == 1)
//            return true;
//
//        return false;
//    }
//
//    private static ArrayList<Point> buildPath(WorldNode end) {
//        ArrayList<Point> path = new ArrayList<>();
//        WorldNode currentNode = end;
//
//        while (currentNode != null) {
//            path.add(currentNode.getPoint());
//            currentNode = currentNode.getPreviousNode();
//        }
//
//        return path;
//    }
//
//    private boolean isValidPath(ArrayList<Point> path, int expectedLeng, Point start, Point end) {
//        //check endpoints
//        System.out.println(path.get(0) + " should be the same as " + start);
//        System.out.println(path.get(path.size() - 1) + " should be the same as " + end);
//        if (!path.get(0).equals(start) || !path.get(path.size() - 1).equals(end))
//            return false;
//
//        //check adjacency
//        Point prevPt = path.get(0);
//        for (Point currentPt : path) {
//            //if there are 2 points that aren't adjacent
//            if (!adjacent(currentPt, prevPt))
//                return false;
//
//            prevPt = currentPt;
//        }
//
//        System.out.println("The path is " + path.size() + " points long and should be " + expectedLeng + "points long.");
//        //check path length
//        if (path.size() == expectedLeng)
//            return true;
//
//        return false;
//    }
}
