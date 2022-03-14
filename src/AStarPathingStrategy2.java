import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class AStarPathingStrategy2 implements PathingStrategy{
    @Override
    public List<Point> computePath(Point start, Point end, Predicate<Point> canPassThrough, BiPredicate<Point, Point> withinReach, Function<Point, Stream<Point>> potentialNeighbors) {
        List<Point> path = new ArrayList<Point>();
        path.add(start);

        Map<Point, Node> cameFrom = new HashMap<Point, Node>();
        Map<Point, Node> futureMap = new HashMap<Point, Node>();

        PriorityQueue<Node> goingTo = new PriorityQueue<Node>(Comparator.comparingInt(Node::getfScoreInt));

        Node current = new Node(start,null,0, Math.sqrt(Point.distanceSquared2(start,end)), Math.sqrt(Point.distanceSquared2(start, end)));
        goingTo.add(current);
        while (goingTo.size() != 0){
            current = goingTo.remove();
            cameFrom.put(current.getPosition(),current);

            if (withinReach.test(current.getPosition(), end)){
                path.add(current.getPosition());
                do {
                    current = current.getPrevNode();
                    Point lastLoco = current.getPosition();
                    path.add(current.getPosition());
                }
                while(current.getPrevNode() != null);
                Collections.reverse(path);
                path.remove(path.size()-1);
                return path;
            }
            List<Point> neighbors = potentialNeighbors.apply(current.getPosition())
                    .filter(WorldModel::withinBounds)
                    .filter(canPassThrough)
                    .filter(point -> !point.equals(end)).toList();
            List<Node> nNodes = new ArrayList<Node>();
            for (Point neighbor:neighbors) {
                if (cameFrom.containsKey(neighbor)) continue;
                nNodes.add(nextTo(current,neighbor,end));
            }
            for(Node cNode:nNodes){
                if (futureMap.containsKey(cNode.getPosition())){
                    if (futureMap.get(cNode.getPosition()).getgScore() < cNode.getgScore()){
                        goingTo.add(cNode);
                        goingTo.remove(futureMap.get(cNode.getPosition()));
                        futureMap.replace(cNode.getPosition(),cNode);
                    }
                }else{
                    futureMap.put(cNode.getPosition(),cNode);
                    goingTo.add(cNode);
                }
                cameFrom.put(cNode.getPosition(),cNode);
            }
        }


        path.add(current.getPosition());
        while(current.getPrevNode() != null) {
            current = current.getPrevNode();
            Point lastLoco = current.getPosition();
            path.add(lastLoco);
        }

        Collections.reverse(path);
        path.remove(path.size()-1);
        return path;
    }
    
    public Node nextTo(Node current,Point loco, Point end){
        return new Node(loco,current, current.getgScore()+1,Math.sqrt(Point.distanceSquared2(loco,end)),(current.getgScore()+1+Math.sqrt(Point.distanceSquared2(loco,end))));
    }
}

class Node{
    private Point position;
    private Node previous;
    private double gScore;
    private double hScore;
    private double fScore;

    public Node(Point position, Node previous,double gScore, double hScore, double fScore){
        this.previous = previous;
        this.position = position;
        this.gScore = gScore;
        this.hScore = hScore;
        this.fScore = fScore;
    }

    public Point getPosition(){return position;}
    public Node getPrevNode(){return previous;}

    public double getgScore() {
        return gScore;
    }

    public double gethScore() {
        return hScore;
    }

    public double getfScore() {
        return fScore;
    }
    public int getfScoreInt() {
        return (int)fScore;
    }
}
