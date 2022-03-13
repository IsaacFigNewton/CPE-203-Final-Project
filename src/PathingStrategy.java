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
                            .add(new Point(point.getX(), point.getY() - 1, point))
                            .add(new Point(point.getX(), point.getY() + 1, point))
                            .add(new Point(point.getX() - 1, point.getY(), point))
                            .add(new Point(point.getX() + 1, point.getY(), point))
                            .build();


    List<Point> computePath(Point start,
                            Point end,
                            Predicate<Point> canPassThrough,
                            BiPredicate<Point, Point> withinReach,
                            Function<Point,Stream<Point>> potentialNeighbors);

     static ArrayList<Point> buildPath(Point end) {
        ArrayList<Point> path = new ArrayList<>();
        Point currentNode = end;

        while (currentNode.getPreviousNode() != null) {
            path.add(0, currentNode);
            currentNode = currentNode.getPreviousNode();
        }

        return path;
    }

    static boolean isValidPath(ArrayList<Point> path, Point start, Point end) {     //int expectedLeng, Point start, Point end) {
//        System.out.println("\nChecking path validity:");
//
//        System.out.println("Start point:" + start);
//        System.out.println("End point:" + end);
//        System.out.println("Path:");
//        String stringToPrint = "{\n";
//        for (int i = 0; i < path.size(); i++) {
//            stringToPrint += path.get(i) + ",\n";
//        }
//        System.out.println(stringToPrint + "}");

        //check path length
        int minPathLength = Math.abs(end.getX() - start.getX()) + Math.abs(end.getY() - start.getY()) - 1;
        if (path.size() < minPathLength) {
//            System.out.println("The path is " + path.size() + " points long but should be at least "
//                    + minPathLength + " points long.");
            return false;
        }

        //check endpoints
        if (path.size() > 0 && !(path.get(0).adjacent(start) && path.get(path.size() - 1).adjacent(end))) {
//            System.out.println("A start or end point in the path was not adjacent to the starting position or goal.");
            return false;
        }

        //check intermediate points' adjacency
        for (int i = 1; i < path.size(); i++) {
            //if there are 2 points that aren't adjacent
            if (!path.get(i).adjacent(path.get(i-1))) {
//                System.out.println("Not all intermediate points in the path were adjacent to eachother.");
                return false;
            }
        }

        return true;
    }

}
