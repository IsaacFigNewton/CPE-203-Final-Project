import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
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

    static List<Point> path = new ArrayList<>();

    static final Function<Point, Stream<Point>> CARDINAL_NEIGHBORS =
            point ->
                    Stream.<Point>builder()
                            .add(new Point(point.getX(), point.getY() - 1))
                            .add(new Point(point.getX(), point.getY() + 1))
                            .add(new Point(point.getX() - 1, point.getY()))
                            .add(new Point(point.getX() + 1, point.getY()))
                            .build();



//    abstract boolean heuristic(Point pt, Point start, Point end);

//    List<Point> computePath(Point start,
//                            Point end,
//                            Predicate<Point> canPassThrough,
//                            BiPredicate<Point, Point> withinReach,
//                            Function<Point, Stream<Point>> potentialNeighbors);


    List<Point> computePath(WorldModel world, Point start, Point end);
//                                           Predicate<Point> canPassThrough,
//                                           BiPredicate<Point, Point> withinReach,
//                                           Function<Point, Stream<Point>> potentialNeighbors) {
//        /* Does not check withinReach.  Since only a single step is taken
//         * on each call, the caller will need to check if the destination
//         * has been reached.
//         */
//
//        //returns a list consisting of the next point according to the heuristic above
//        return potentialNeighbors.apply(start)  //get the neighboring cells around the starting point
//                .filter(canPassThrough)         //filter out any neighbors that have an obstacle or something in them
//                .filter((p1) -> p1.equals(new Point(0,0)))  //?
//                    // (when implementing A*, you'll need to compare all available points)
//                .limit(1)                       //return 1 neighbor that matched all criteria (or the first checked one)
//                .collect(Collectors.toList());  //return a list of that 1 neighbor
//    }


    static boolean adjacent(Point p1, Point p2) {
        int absXDist = Math.abs(p1.getX() - p2.getX());
        int absYDist = Math.abs(p1.getY() - p2.getY());

        if (absXDist <= 1 && absYDist <= 1 && absXDist + absYDist == 1)
            return true;

        return false;
    }

     static ArrayList<Point> buildPath(WorldNode end) {
        ArrayList<Point> path = new ArrayList<>();
        WorldNode currentNode = end;

        while (currentNode != null) {
            path.add(currentNode.getPoint());
            currentNode = currentNode.getPreviousNode();
        }

        String pathToPrint = "{";
        for (Point pt : path)
            pathToPrint += " " + pt;
        pathToPrint += "}";
        System.out.println(pathToPrint);

        return path;
    }

    static boolean isValidPath(ArrayList<Point> path, int expectedLeng, Point start, Point end) {
        //check endpoints
        System.out.println(path.get(0) + " should be the same as " + start);
        System.out.println(path.get(path.size() - 1) + " should be the same as " + end);
        if (!path.get(0).equals(start) || !path.get(path.size() - 1).equals(end))
            return false;

        //check adjacency
        Point prevPt = path.get(0);
        for (Point currentPt : path) {
            //if there are 2 points that aren't adjacent
            if (!adjacent(currentPt, prevPt))
                return false;

            prevPt = currentPt;
        }

        System.out.println("The path is " + path.size() + " points long and should be " + expectedLeng + "points long.");
        //check path length
        if (path.size() == expectedLeng)
            return true;

        return false;
    }

}
