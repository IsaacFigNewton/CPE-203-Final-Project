import java.util.Objects;

public class WorldNode {
        private Point point;
        private WorldNode previousNode;
        private WorldNode nextNode;
        private int startDist;

        public WorldNode (Point point, WorldNode prev, WorldNode next, int startDist) {
            assert(prev != null);

            this.point = point;
            this.previousNode = prev;
            this.nextNode = next;
            this.startDist = startDist;
        }

        public WorldNode (Point point, WorldNode prev) {
            this(point, prev, null, prev.startDist + 1);
        }

        public WorldNode (Point point) {
            this.point = point;
        }

        public Point getPoint() {
            return point;
        }

        public void setPoint(Point point) {
            this.point = point;
        }

        public WorldNode getPreviousNode() {
            return previousNode;
        }

        public void setPreviousNode(WorldNode previousNode) {
            this.previousNode = previousNode;
        }

        public WorldNode getNextNode() {
            return nextNode;
        }

        public void setNextNode(WorldNode nextNode) {
            this.nextNode = nextNode;
        }

        public int getStartDist() {
            return startDist;
        }

        public void setStartDist(int startDist) {
            this.startDist = startDist;
        }

        //manhattan distance from the current point to the end point
        public int endDist(Point goalLoc) {
            return Math.abs(this.point.getX() - goalLoc.getX()) + Math.abs(this.point.getY() - goalLoc.getY());
        };

        public double totalDist(Point goalLoc) {
//         System.out.println(this.startDist + this.endDist());
            return this.startDist + this.endDist(goalLoc);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            WorldNode WorldNode = (WorldNode) o;
            return point.equals(WorldNode.point);
        }

        @Override
        public int hashCode() {
            return Objects.hash(point);
        }
}
