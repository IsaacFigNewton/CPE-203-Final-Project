import java.util.List;
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
//
//    public List<Point> computePath(Point start, Point end,
//                                   Predicate<Point> canPassThrough,
//                                   BiPredicate<Point, Point> withinReach,
//                                   Function<Point, Stream<Point>> potentialNeighbors) {
//        /* Does not check withinReach.  Since only a single step is taken
//         * on each call, the caller will need to check if the destination
//         * has been reached.
//         */
//
//
//        //returns a list consisting of the next point according to the heuristic above
//        return potentialNeighbors.apply(start)  //get the neighboring cells around the starting point
//                .filter(canPassThrough)         //filter out any neighbors that have an obstacle or something in them
//                .filter((pt) -> heuristic(pt, start, end))  //heuristic
//                .limit(1)                       //return 1 neighbor that matched all criteria (or the first checked one)
//                .collect(Collectors.toList());  //return a list of that 1 neighbor
//    }
}
