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

    public List<Point> computePath(WorldModel world, Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors) {
        /* Does not check withinReach.  Since only a single step is taken
         * on each call, the caller will need to check if the destination
         * has been reached.
         */
        return potentialNeighbors.apply(start)
                .filter(canPassThrough)
                .filter(pt ->
                        !pt.equals(start)
                                && !pt.equals(end)
                                && Math.abs(end.getX() - pt.getX()) <= Math.abs(end.getX() - start.getX())
                                && Math.abs(end.getY() - pt.getY()) <= Math.abs(end.getY() - start.getY()))
                .limit(1)
                .collect(Collectors.toList());
    }
}
