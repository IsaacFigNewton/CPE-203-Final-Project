import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

interface PathingStrategy {
    /*
     * Returns a prefix of a path from the start point to a point within reach
     * of the end point.  This path is only valid ("clear") when returned, but
     * may be invalidated by movement of other entities.
     *
     * The prefix includes neither the start point nor the end point.
     */

    static final Function<Point, Stream<Point>> CARDINAL_NEIGHBORS =
            point ->
                    Stream.<Point>builder()
                            .add(new Point(point.getX(), point.getY() - 1))
                            .add(new Point(point.getX(), point.getY() + 1))
                            .add(new Point(point.getX() - 1, point.getY()))
                            .add(new Point(point.getX() + 1, point.getY()))
                            .build();



//    abstract boolean heuristic(Point pt, Point start, Point end);

    List<Point> computePath(WorldModel world,
                            Point start,
                            Point end,
                            Predicate<Point> canPassThrough,
                            BiPredicate<Point, Point> withinReach,
                            Function<Point, Stream<Point>> potentialNeighbors);

     static ArrayList<Point> buildPath(Point end) {
        ArrayList<Point> path = new ArrayList<>();
        //truncate the path before the end node
        Point currentNode = end;

        //truncate the path after the first node
        while (currentNode.getPreviousNode() != null) {
            path.add(0, currentNode);
            currentNode = currentNode.getPreviousNode();
        }

//        String pathToPrint = "{";
//        for (Point pt : path)
//            pathToPrint += " " + pt;
//        pathToPrint += "}";
//        System.out.println(pathToPrint);

        return path;
    }

    static boolean isValidPath(ArrayList<Point> path, Point start, Point end) {     //int expectedLeng, Point start, Point end) {
        //check endpoints
//        System.out.println(path.get(0) + " should be adjacent to " + start);
//        System.out.println(path.get(path.size() - 1) + " should be adjacent to " + end);
        if (!(path.get(0).adjacent(start) && path.get(path.size() - 1).adjacent(end))) {
            System.out.println("A start or end point in the path was not adjacent to the starting position or goal.");
            return false;
        }

        //check adjacency
        Point prevPt = path.get(0);
        for (Point currentPt : path) {
            //if there are 2 points that aren't adjacent
            if (!currentPt.adjacent(prevPt)) {
                System.out.println("Not all intermediate points in the path were not adjacent to eachother.");
                return false;
            }

            prevPt = currentPt;
        }

//        System.out.println("The path is " + path.size() + " points long and should be " + expectedLeng + "points long.");
//        //check path length
//        if (path.size() == expectedLeng)
//            return true;

        return true;
    }

}
