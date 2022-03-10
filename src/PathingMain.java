//import java.util.*;
//
//import processing.core.*;
//
//public class PathingMain extends PApplet
//{
//   private List<PImage> imgs;
//   private int current_image;
//   private long next_time;
//   private PImage background;
//   private PImage obstacle;
//   private static Point wPos = new Point(2, 2);
//   private static Point goalLoc = new Point(14, 13);
//   private PImage goal;
//   private static List<Point> path;
//   Comparator<GridNode> comparePointHeuristics = (p1, p2) -> (int) (p1.totalDist()
//        - p2.totalDist());
//
//   private static final int TILE_SIZE = 32;
//
//   private static final int ANIMATION_TIME = 100;
//
//   private GridValues[][] grid;
//   private static final int ROWS = 15;
//   private static final int COLS = 20;
//
//   private enum GridValues { BACKGROUND, OBSTACLE, GOAL, SEARCHED };
//
//
//   private boolean drawPath = false;
//
//	public void settings() {
//      size(640,480);
//	}
//
//	/* runs once to set up world */
//   public void setup()
//   {
//      path = new LinkedList<>();
//      path.add(wPos); //added this
//      imgs = new ArrayList<>();
//      imgs.add(loadImage("images/wyvern1.bmp"));
//      imgs.add(loadImage("images/wyvern2.bmp"));
//      imgs.add(loadImage("images/wyvern3.bmp"));
//
//      background = loadImage("images/grass.bmp");
//      obstacle = loadImage("images/vein.bmp");
//      goal = loadImage("images/water.bmp");
//
//      grid = new GridValues[ROWS][COLS];
//      initialize_grid(grid);
//
//      current_image = 0;
//      next_time = System.currentTimeMillis() + ANIMATION_TIME;
//   }
//
//	/* set up a 2D grid to represent the world */
//   private static void initialize_grid(GridValues[][] grid)
//   {
//      for (int row = 0; row < grid.length; row++)
//      {
//         for (int col = 0; col < grid[row].length; col++)
//         {
//            grid[row][col] = GridValues.BACKGROUND;
//         }
//      }
//
//		//set up some obstacles
//      for (int row = 2; row < 8; row++)
//      {
//         grid[row][row + 5] = GridValues.OBSTACLE;
//      }
//
//      for (int row = 8; row < 12; row++)
//      {
//         grid[row][19 - row] = GridValues.OBSTACLE;
//      }
//
//      for (int col = 1; col < 8; col++)
//      {
//         grid[11][col] = GridValues.OBSTACLE;
//      }
//
//      grid[goalLoc.y][goalLoc.x] = GridValues.GOAL;
//   }
//
//   private void next_image()
//   {
//      current_image = (current_image + 1) % imgs.size();
//   }
//
//	/* runs over and over */
//   public void draw()
//   {
//      // A simplified action scheduling handler
//      long time = System.currentTimeMillis();
//      if (time >= next_time)
//      {
//         next_image();
//         next_time = time + ANIMATION_TIME;
//      }
//
//      draw_grid();
//      draw_path();
//
//      image(imgs.get(current_image), wPos.x * TILE_SIZE, wPos.y * TILE_SIZE);
//   }
//
//   private void draw_grid()
//   {
//      for (int row = 0; row < grid.length; row++)
//      {
//         for (int col = 0; col < grid[row].length; col++)
//         {
//            draw_tile(row, col);
//         }
//      }
//   }
//
//   private void draw_path()
//   {
//      if (drawPath)
//      {
//         for (Point p : path)
//         {
//            fill(128, 0, 0);
//            rect(p.x * TILE_SIZE + TILE_SIZE * 3 / 8,
//               p.y * TILE_SIZE + TILE_SIZE * 3 / 8,
//               TILE_SIZE / 4, TILE_SIZE / 4);
//         }
//      }
//   }
//
//   private void draw_tile(int row, int col)
//   {
//      switch (grid[row][col])
//      {
//         case BACKGROUND:
//            image(background, col * TILE_SIZE, row * TILE_SIZE);
//            break;
//         case OBSTACLE:
//            image(obstacle, col * TILE_SIZE, row * TILE_SIZE);
//            break;
//         case SEARCHED:
//            fill(0, 128);
//            rect(col * TILE_SIZE + TILE_SIZE / 4,
//               row * TILE_SIZE + TILE_SIZE / 4,
//               TILE_SIZE / 2, TILE_SIZE / 2);
//            break;
//         case GOAL:
//            image(goal, col * TILE_SIZE, row * TILE_SIZE);
//            break;
//      }
//   }
//
//   public static void main(String args[])
//   {
//      PApplet.main("PathingMain");
//
//   }
//
//   public void keyPressed()
//   {
//      if (key == ' ')
//      {
////         for (int i = 0; i < 1000; i++)
////            moveOnce(grid);
//
//            AStar(grid);
//
//         for (Point point : path)
//            System.out.println("(" + point.x + ", " + point.y + ")");
//         System.out.println("\n");
//      }
//      else if (key == 'p')
//      {
//         drawPath ^= true;
//      }
//      else if (key == 'c')
//      {
//         //clear out prior path and re-initialize grid
//         path.clear();
//         initialize_grid(grid);
//      }
//   }
//
//   //*******************************************************************************************************************
//
//
////   private void moveOnce(GridValues[][] grid) //Point pos, , List<Point> path
////   {
////      //make sure the stack is never empty by adding the starting position to the end of the path
////      if (path.size() == 0)
////         path.add(wPos);
////
////      Point pos = path.get(0);
////
////      if (grid[pos.y][pos.x] != GridValues.GOAL) {
////         //possible points
////         ArrayList<Point> neighbors = new ArrayList<>();
////
////         neighbors.add(new Point(pos.x + 1, pos.y));
////         neighbors.add(new Point(pos.x, pos.y + 1));
////         neighbors.add(new Point(pos.x - 1, pos.y));
////         neighbors.add(new Point(pos.x, pos.y - 1));
////
////         //set the next desired position to the current one
////         Point nextPos = pos;
////
////         //for each neighbor in a list of neighbors
////         for (Point neighbor : neighbors) {
////
////            //test if this is a valid grid cell
////            if (withinBounds(neighbor, grid) &&
////                    grid[neighbor.y][neighbor.x] != GridValues.OBSTACLE &&
////                    grid[neighbor.y][neighbor.x] != GridValues.SEARCHED) {
////
////               //check if the neighbor is the goal
////               if (grid[neighbor.y][neighbor.x] == GridValues.GOAL) {
////                  nextPos = neighbor;
////                  break;
////               }
////
////               //set this value as searched
////               grid[neighbor.y][neighbor.x] = GridValues.SEARCHED;
////               nextPos = neighbor;
////               break;
////            }
////         }
////
////         //if a next move was found, add it to the front of the path
////         if (!nextPos.equals(pos))
////            path.add(0, nextPos);
////
////            //if none of the neighbors would be a new move, backtrack by one
////         else
////            path.remove(0);
////      }
////   }
//
//   private class GridNode {
//      private Point point;
//      private GridNode previousNode;
//      private GridNode nextNode;
//      private int startDist;
//
//      public GridNode (Point point, GridNode prev, GridNode next, int startDist) {
//         assert(prev != null);
//
//         this.point = point;
//         this.previousNode = prev;
//         this.nextNode = next;
//         this.startDist = startDist;
//      }
//
//      public GridNode (Point point, GridNode prev) {
//         this(point, prev, null, prev.startDist + 1);
//      }
//
//      public GridNode (Point point) {
//         this.point = point;
//      }
//
//      public Point getPoint() {
//         return point;
//      }
//
//      public void setPoint(Point point) {
//         this.point = point;
//      }
//
//      public GridNode getPreviousNode() {
//         return previousNode;
//      }
//
//      public void setPreviousNode(GridNode previousNode) {
//         this.previousNode = previousNode;
//      }
//
//      public GridNode getNextNode() {
//         return nextNode;
//      }
//
//      public void setNextNode(GridNode nextNode) {
//         this.nextNode = nextNode;
//      }
//
//      public int getStartDist() {
//         return startDist;
//      }
//
//      public void setStartDist(int startDist) {
//         this.startDist = startDist;
//      }
//
//      //manhattan distance from the current point to the end point
//      public int endDist() {
//         return Math.abs(this.point.x - goalLoc.x) + Math.abs(this.point.y - goalLoc.y);
//      };
//
//      private double totalDist() {
////         System.out.println(this.startDist + this.endDist());
//         return this.startDist + this.endDist();
//      }
//
//      @Override
//      public boolean equals(Object o) {
//         if (this == o) return true;
//         if (o == null || getClass() != o.getClass()) return false;
//         GridNode gridNode = (GridNode) o;
//         return point.equals(gridNode.point);
//      }
//
//      @Override
//      public int hashCode() {
//         return Objects.hash(point);
//      }
//   }
//
//   //refactor AStar to use GridNodes and to use the manhattan distance for calculating start distance
//
//   private void AStar(GridValues[][] grid) //Point pos, , List<Point> path
//   {
//      PriorityQueue<GridNode> openList = new PriorityQueue<>(comparePointHeuristics);
//      HashSet<GridNode> closedList = new HashSet<>();
//
//      openList.add(new GridNode(wPos));
//      GridNode pos;
//      Point currentPoint = wPos;
//
//      //while there are still unchecked nodes and the goal hasn't been reached
//      while (!openList.isEmpty() && grid[currentPoint.y][currentPoint.x] != GridValues.GOAL) {
////      for (int i = 0; i < 1000000; i++) {
//         pos = openList.remove();
//         currentPoint = pos.getPoint();
//
//         //if the current point is within reach of the goal point
//         if (adjacent(currentPoint, goalLoc))
//            break;
//
//         closedList.add(pos);
//         grid[pos.getPoint().y][pos.getPoint().x] = GridValues.SEARCHED;
//
//         //compose a list of possible points around the current point
//         List<GridNode> neighbors = Arrays.asList(
//                  new GridNode(new Point(currentPoint.x + 1, currentPoint.y), pos),
//                  new GridNode(new Point(currentPoint.x, currentPoint.y + 1), pos),
//                  new GridNode(new Point(currentPoint.x - 1, currentPoint.y), pos),
//                  new GridNode(new Point(currentPoint.x, currentPoint.y - 1), pos));
//
//         //for each neighbor in the list of neighbors
//         for (GridNode neighbor : neighbors) {
//            //access the neighbor's point
//            Point neighborPoint = neighbor.getPoint();
//
//            //test if this is a valid grid cell
//            if (withinBounds(neighborPoint, grid) &&
//                    grid[neighborPoint.y][neighborPoint.x] != GridValues.OBSTACLE &&
//                    !closedList.contains(neighbor)) {
//
//               //add the current point to the priority queue
//               //check if it's already in the queue
////               if (openList.contains(neighbor))
////                  // if it is, update it's information
////                  openList = replaceNode(openList, neighbor);
////               else
//                  openList.add(neighbor);
//            }
//         }
//
//      }
//
//      //Recurse through the top GridNode (which is at the goal) to compose the shortest path
//      buildPath(openList.peek());
//   }
//
//
//   private boolean adjacent(Point p1, Point p2) {
//      int absXDist = Math.abs(p1.x - p2.x);
//      int absYDist = Math.abs(p1.y - p2.y);
//
//      if (absXDist <= 1 && absYDist <= 1 && absXDist + absYDist == 1)
//         return true;
//
//      return false;
//   }
//
//   private static void buildPath(GridNode end) {
//      if (end != null) {
//         buildPath(end.getPreviousNode());
//         path.add(end.getPoint());
//      }
//   }
//
//   private boolean isValidPath(int expectedLeng, Point start, Point end) {
//      Point prevPt = wPos;
//      for (Point currentPt : path) {
//         //if there are 2 points that aren't adjacent
//         if (!adjacent(currentPt, prevPt))
//            return false;
//
//         prevPt = currentPt;
//      }
//
//      //if all points in the path are adjacent to the previous points
//      //and the length of the path is the same as the expected shortest path length
//      if (path.size() == expectedLeng)
//         return true;
//      return false;
//   }
//
//   private static boolean withinBounds(Point p, GridValues[][] grid)
//   {
//      return p.y >= 0 && p.y < grid.length &&
//         p.x >= 0 && p.x < grid[0].length;
//   }
//}
