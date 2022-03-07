import java.util.ArrayList;
import java.util.Arrays;
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

    static final Function<Point, Stream<Point>> CARDINAL_NEIGHBORS =
            point ->
                    Stream.<Point>builder()
                            .add(new Point(point.getX(), point.getY() - 1))
                            .add(new Point(point.getX(), point.getY() + 1))
                            .add(new Point(point.getX() - 1, point.getY()))
                            .add(new Point(point.getX() + 1, point.getY()))
                            .build();

    abstract boolean heuristic(Point pt, Point start, Point end);

//    List<Point> computePath(Point start,
//                            Point end,
//                            Predicate<Point> canPassThrough,
//                            BiPredicate<Point, Point> withinReach,
//                            Function<Point, Stream<Point>> potentialNeighbors);


    public default List<Point> computePath(Point start, Point end,
                                           Predicate<Point> canPassThrough,
                                           BiPredicate<Point, Point> withinReach,
                                           Function<Point, Stream<Point>> potentialNeighbors) {
        /* Does not check withinReach.  Since only a single step is taken
         * on each call, the caller will need to check if the destination
         * has been reached.
         */


        //returns a list consisting of the next point according to the heuristic above
        return potentialNeighbors.apply(start)  //get the neighboring cells around the starting point
                .filter(canPassThrough)         //filter out any neighbors that have an obstacle or something in them
                .filter((pt) -> heuristic(pt, start, end))  //heuristic
                    // (when implementing A*, you'll need to compare all available points)
                .limit(1)                       //return 1 neighbor that matched all criteria (or the first checked one)
                .collect(Collectors.toList());  //return a list of that 1 neighbor
    }

}
