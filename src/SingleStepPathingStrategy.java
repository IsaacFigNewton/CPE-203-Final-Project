import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SingleStepPathingStrategy implements PathingStrategy {

    public boolean heuristic(Point pt, Point start, Point end) {
        if (!pt.equals(start)                                                               //if the candidate point isn't the starting point
                && !pt.equals(end)                                                          //... or the end point
                && Math.abs(end.getX() - pt.getX()) <= Math.abs(end.getX() - start.getX())  //... and it's closer to the end point's x
                && Math.abs(end.getY() - pt.getY()) <= Math.abs(end.getY() - start.getY())) //... and y values.
            return true;
        return false;
    }

    @Override
    public List<Point> computePath(WorldModel world, Point start, Point end) {
        PriorityQueue<WorldNode> openList = new PriorityQueue<>(
                (p1, p2) -> (int) (p1.totalDist(end) - p2.totalDist(end))
        );
        HashSet<WorldNode> closedList = new HashSet<>();

        openList.add(new WorldNode(start));
        WorldNode pos;
        Point currentPoint = start;

        //while there are still unchecked nodes and the goal hasn't been reached
        while (!openList.isEmpty() && world.getOccupant(currentPoint) != world.getOccupant(end)) {
//      for (int i = 0; i < 1000000; i++) {
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
        return PathingStrategy.buildPath(openList.peek());
    }

//    public List<Point> computePath(Point start, Point end,
//                                   Predicate<Point> canPassThrough,
//                                   BiPredicate<Point, Point> withinReach,
//                                   Function<Point, Stream<Point>> potentialNeighbors) {
//        /* Does not check withinReach.  Since only a single step is taken
//         * on each call, the caller will need to check if the destination
//         * has been reached.
//         */
//        return potentialNeighbors.apply(start)
//                .filter(canPassThrough)
//                .filter(pt ->
//                        !pt.equals(start)
//                                && !pt.equals(end)
//                                && Math.abs(end.getX() - pt.getX()) <= Math.abs(end.getX() - start.getX())
//                                && Math.abs(end.getY() - pt.getY()) <= Math.abs(end.getY() - start.getY()))
//                .limit(1)
//                .collect(Collectors.toList());
//    }
}
