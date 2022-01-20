import java.util.*;

import processing.core.PImage;

/**
 * This class contains many functions written in a procedural style.
 * You will reduce the size of this class over the next several weeks
 * by refactoring this codebase to follow an OOP style.
 */
public final class Functions
{
    public static final Random rand = new Random();

    public static final int PROPERTY_KEY = 0;

    public static final List<String> PATH_KEYS = new ArrayList<>(Arrays.asList("bridge", "dirt", "dirt_horiz", "dirt_vert_left", "dirt_vert_right",
            "dirt_bot_left_corner", "dirt_bot_right_up", "dirt_vert_left_bot"));

    public static final String SAPLING_KEY = "sapling";
    public static final int SAPLING_HEALTH_LIMIT = 5;
    public static final int SAPLING_ACTION_ANIMATION_PERIOD = 1000; // have to be in sync since grows and gains health at same time
    public static final int SAPLING_NUM_PROPERTIES = 4;
    public static final int SAPLING_ID = 1;
    public static final int SAPLING_COL = 2;
    public static final int SAPLING_ROW = 3;
    public static final int SAPLING_HEALTH = 4;

    public static final String BGND_KEY = "background";
    public static final int BGND_NUM_PROPERTIES = 4;
    public static final int BGND_ID = 1;
    public static final int BGND_COL = 2;
    public static final int BGND_ROW = 3;

    public static final String OBSTACLE_KEY = "obstacle";
    public static final int OBSTACLE_NUM_PROPERTIES = 5;
    public static final int OBSTACLE_ID = 1;
    public static final int OBSTACLE_COL = 2;
    public static final int OBSTACLE_ROW = 3;
    public static final int OBSTACLE_ANIMATION_PERIOD = 4;

    public static final String DUDE_KEY = "dude";
    public static final int DUDE_NUM_PROPERTIES = 7;
    public static final int DUDE_ID = 1;
    public static final int DUDE_COL = 2;
    public static final int DUDE_ROW = 3;
    public static final int DUDE_LIMIT = 4;
    public static final int DUDE_ACTION_PERIOD = 5;
    public static final int DUDE_ANIMATION_PERIOD = 6;

    public static final String HOUSE_KEY = "house";
    public static final int HOUSE_NUM_PROPERTIES = 4;
    public static final int HOUSE_ID = 1;
    public static final int HOUSE_COL = 2;
    public static final int HOUSE_ROW = 3;

    public static final String FAIRY_KEY = "fairy";
    public static final int FAIRY_NUM_PROPERTIES = 6;
    public static final int FAIRY_ID = 1;
    public static final int FAIRY_COL = 2;
    public static final int FAIRY_ROW = 3;
    public static final int FAIRY_ANIMATION_PERIOD = 4;
    public static final int FAIRY_ACTION_PERIOD = 5;

    public static final String STUMP_KEY = "stump";

    public static final String TREE_KEY = "tree";
    public static final int TREE_NUM_PROPERTIES = 7;
    public static final int TREE_ID = 1;
    public static final int TREE_COL = 2;
    public static final int TREE_ROW = 3;
    public static final int TREE_ANIMATION_PERIOD = 4;
    public static final int TREE_ACTION_PERIOD = 5;
    public static final int TREE_HEALTH = 6;

    public static final int TREE_ANIMATION_MAX = 600;
    public static final int TREE_ANIMATION_MIN = 50;
    public static final int TREE_ACTION_MAX = 1400;
    public static final int TREE_ACTION_MIN = 1000;
    public static final int TREE_HEALTH_MAX = 3;
    public static final int TREE_HEALTH_MIN = 1;


    public static int getNumFromRange(int max, int min)
    {
        Random rand = new Random();
        return min + rand.nextInt(
                max
                        - min);
    }


    public static void shift(Viewport viewport, int col, int row) {
        viewport.col = col;
        viewport.row = row;
    }

    public static boolean contains(Viewport viewport, Point p) {
        return p.y >= viewport.row && p.y < viewport.row + viewport.numRows
                && p.x >= viewport.col && p.x < viewport.col + viewport.numCols;
    }

    public static void load(
            Scanner in, WorldModel world, ImageStore imageStore)
    {
        int lineNumber = 0;
        while (in.hasNextLine()) {
            try {
                if (!processLine(in.nextLine(), world, imageStore)) {
                    System.err.println(String.format("invalid entry on line %d",
                            lineNumber));
                }
            }
            catch (NumberFormatException e) {
                System.err.println(
                        String.format("invalid entry on line %d", lineNumber));
            }
            catch (IllegalArgumentException e) {
                System.err.println(
                        String.format("issue on line %d: %s", lineNumber,
                                e.getMessage()));
            }
            lineNumber++;
        }
    }

    public static boolean processLine(
            String line, WorldModel world, ImageStore imageStore)
    {
        String[] properties = line.split("\\s");
        if (properties.length > 0) {
            switch (properties[PROPERTY_KEY]) {
                case BGND_KEY:
                    return world.parseBackground(properties, imageStore);
                case DUDE_KEY:
                    return world.parseDude(properties, imageStore);
                case OBSTACLE_KEY:
                    return world.parseObstacle(properties, imageStore);
                case FAIRY_KEY:
                    return world.parseFairy(properties, imageStore);
                case HOUSE_KEY:
                    return world.parseHouse(properties, imageStore);
                case TREE_KEY:
                    return world.parseTree(properties, imageStore);
                case SAPLING_KEY:
                    return world.parseSapling(properties, imageStore);
            }
        }

        return false;
    }

    public static void tryAddEntity(WorldModel world, Entity entity) {
        if (isOccupied(world, entity.position)) {
            // arguably the wrong type of exception, but we are not
            // defining our own exceptions yet
            throw new IllegalArgumentException("position occupied");
        }

        world.addEntity(entity);
    }

    public static boolean withinBounds(WorldModel world, Point pos) {
        return pos.y >= 0 && pos.y < world.numRows && pos.x >= 0
                && pos.x < world.numCols;
    }

    public static boolean isOccupied(WorldModel world, Point pos) {
        return withinBounds(world, pos) && world.getOccupancyCell(pos) != null;
    }

    public static Optional<Entity> nearestEntity(
            List<Entity> entities, Point pos)
    {
        if (entities.isEmpty()) {
            return Optional.empty();
        }
        else {
            Entity nearest = entities.get(0);
            int nearestDistance = distanceSquared(nearest.position, pos);

            for (Entity other : entities) {
                int otherDistance = distanceSquared(other.position, pos);

                if (otherDistance < nearestDistance) {
                    nearest = other;
                    nearestDistance = otherDistance;
                }
            }

            return Optional.of(nearest);
        }
    }

    public static int distanceSquared(Point p1, Point p2) {
        int deltaX = p1.x - p2.x;
        int deltaY = p1.y - p2.y;

        return deltaX * deltaX + deltaY * deltaY;
    }

    public static Point viewportToWorld(Viewport viewport, int col, int row) {
        return new Point(col + viewport.col, row + viewport.row);
    }

    public static Point worldToViewport(Viewport viewport, int col, int row) {
        return new Point(col - viewport.col, row - viewport.row);
    }

    public static int clamp(int value, int low, int high) {
        return Math.min(high, Math.max(value, low));
    }

    public static void shiftView(WorldView view, int colDelta, int rowDelta) {
        int newCol = clamp(view.viewport.col + colDelta, 0,
                view.world.numCols - view.viewport.numCols);
        int newRow = clamp(view.viewport.row + rowDelta, 0,
                view.world.numRows - view.viewport.numRows);

        shift(view.viewport, newCol, newRow);
    }

    public static void drawBackground(WorldView view) {
        for (int row = 0; row < view.viewport.numRows; row++) {
            for (int col = 0; col < view.viewport.numCols; col++) {
                Point worldPoint = viewportToWorld(view.viewport, col, row);
                Optional<PImage> image =
                        view.world.getBackgroundImage(worldPoint);
                if (image.isPresent()) {
                    view.screen.image(image.get(), col * view.tileWidth,
                            row * view.tileHeight);
                }
            }
        }
    }

    public static void drawEntities(WorldView view) {
        for (Entity entity : view.world.entities) {
            Point pos = entity.position;

            if (contains(view.viewport, pos)) {
                Point viewPoint = worldToViewport(view.viewport, pos.x, pos.y);
                view.screen.image(entity.getCurrentImage(),
                        viewPoint.x * view.tileWidth,
                        viewPoint.y * view.tileHeight);
            }
        }
    }

    public static void drawViewport(WorldView view) {
        drawBackground(view);
        drawEntities(view);
    }

    public static Action createAnimationAction(Entity entity, int repeatCount) {
        return new Action(ActionKind.ANIMATION, entity, null, null,
                repeatCount);
    }

    public static Action createActivityAction(
            Entity entity, WorldModel world, ImageStore imageStore)
    {
        return new Action(ActionKind.ACTIVITY, entity, world, imageStore, 0);
    }

    public static Entity createHouse(
            String id, Point position, List<PImage> images)
    {
        return new Entity(EntityKind.HOUSE, id, position, images, 0, 0, 0,
                0, 0, 0);
    }

    public static Entity createObstacle(
            String id, Point position, int animationPeriod, List<PImage> images)
    {
        return new Entity(EntityKind.OBSTACLE, id, position, images, 0, 0, 0,
                animationPeriod, 0, 0);
    }

    public static Entity createTree(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            int health,
            List<PImage> images)
    {
        return new Entity(EntityKind.TREE, id, position, images, 0, 0,
                actionPeriod, animationPeriod, health, 0);
    }

    public static Entity createStump(
            String id,
            Point position,
            List<PImage> images)
    {
        return new Entity(EntityKind.STUMP, id, position, images, 0, 0,
                0, 0, 0, 0);
    }

    // health starts at 0 and builds up until ready to convert to Tree
    public static Entity createSapling(
            String id,
            Point position,
            List<PImage> images)
    {
        return new Entity(EntityKind.SAPLING, id, position, images, 0, 0,
                SAPLING_ACTION_ANIMATION_PERIOD, SAPLING_ACTION_ANIMATION_PERIOD, 0, SAPLING_HEALTH_LIMIT);
    }

    public static Entity createFairy(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images)
    {
        return new Entity(EntityKind.FAIRY, id, position, images, 0, 0,
                actionPeriod, animationPeriod, 0, 0);
    }

    // need resource count, though it always starts at 0
    public static Entity createDudeNotFull(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            int resourceLimit,
            List<PImage> images)
    {
        return new Entity(EntityKind.DUDE_NOT_FULL, id, position, images, resourceLimit, 0,
                actionPeriod, animationPeriod, 0, 0);
    }

    // don't technically need resource count ... full
    public static Entity createDudeFull(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            int resourceLimit,
            List<PImage> images) {
        return new Entity(EntityKind.DUDE_FULL, id, position, images, resourceLimit, 0,
                actionPeriod, animationPeriod, 0, 0);
    }
}
