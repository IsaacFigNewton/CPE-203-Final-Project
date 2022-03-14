import processing.core.PImage;

import java.util.*;

/**
 * Represents the 2D World in which this simulation is running.
 * Keeps track of the size of the world, the background image for each
 * location in the world, and the entities that populate the this.
 */
public final class WorldModel
{
    private int numRows;
    private int numCols;
    private Background background[][];
    private Entity occupancy[][];
    public Set<Entity> entities;

    public WorldModel(int numRows, int numCols, Background defaultBackground) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.background = new Background[numRows][numCols];
        this.occupancy = new Entity[numRows][numCols];
        this.entities = new HashSet<>();

        for (int row = 0; row < numRows; row++) {
            Arrays.fill(this.background[row], defaultBackground);
        }
    }

    public int getNumCols() { return this.numCols;}
    public int getNumRows() { return this.numRows;}

    public void load(
            Scanner in, ImageStore imageStore)
    {
        int lineNumber = 0;
        while (in.hasNextLine()) {
            try {
                if (!this.processLine(in.nextLine(), imageStore)) {
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

    private boolean processLine(
            String line, ImageStore imageStore)
    {
        String[] properties = line.split("\\s");
        if (properties.length > 0) {
            switch (properties[Functions.PROPERTY_KEY]) {
                case Functions.BGND_KEY:
                    return this.parseBackground(properties, imageStore);
                case Functions.DUDE_KEY:
                    return this.parseDude(properties, imageStore);
                case Functions.OBSTACLE_KEY:
                    return this.parseObstacle(properties, imageStore);
                case Functions.FAIRY_KEY:
                    return this.parseFairy(properties, imageStore);
                case Functions.HOUSE_KEY:
                    return this.parseHouse(properties, imageStore);
                case Functions.TREE_KEY:
                    return this.parseTree(properties, imageStore);
                case Functions.SAPLING_KEY:
                    return this.parseSapling(properties, imageStore);
            }
        }

        return false;
    }

    private void tryAddEntity(Entity entity) {
        if (this.isOccupied(entity.getPosition())) {
            // arguably the wrong type of exception, but we are not
            // defining our own exceptions yet
            throw new IllegalArgumentException("position occupied");
        }

        this.addEntity(entity);
    }

    public boolean withinBounds(Point pos) {
        return pos.y >= 0 && pos.y < this.numRows && pos.x >= 0
                && pos.x < this.numCols;
    }

    public boolean isOccupied(Point pos) {
        //modified to allow everything to pass over swamp entities/tiles
        return this.withinBounds(pos) && !(this.getOccupancyCell(pos) == null || this.getOccupancyCell(pos).getClass().equals(Swamp.class));
    }

    public Optional<Entity> findNearest(Point pos, List<Class<? extends Entity>> kinds)
    {
        //A linked list of all entities of a certain type in this world
        List<Entity> ofType = new LinkedList<>();

        //for all kinds of entity
        for (Class<? extends Entity> kind: kinds)
        {
            //for all entities
            for (Entity entity : this.entities) {
                //if the current entity is of a desired kind
                if (entity.getClass() == kind) {
                    ofType.add(entity);
                }
            }
        }

        return pos.nearestEntity(ofType);
    }

    /*
           Assumes that there is no entity currently occupying the
           intended destination cell.
        */
    public void addEntity(Entity entity) {
        if (this.withinBounds(entity.getPosition())) {
            this.setOccupancyCell(entity.getPosition(), entity);
            this.entities.add(entity);
        }
    }

    public void moveEntity(Entity entity, Point pos) {
        Point oldPos = entity.getPosition();
        if (this.withinBounds(pos) && !pos.equals(oldPos)) {
            this.setOccupancyCell(oldPos, null);
            this.removeEntityAt(pos);
            this.setOccupancyCell(pos, entity);
            entity.setPosition(pos);
        }
    }

    public void removeEntity(Entity entity) {
        this.removeEntityAt(entity.getPosition());
    }

    private void removeEntityAt(Point pos) {
        if (this.withinBounds(pos) && this.getOccupancyCell(pos) != null) {
            Entity entity = this.getOccupancyCell(pos);

            /* This moves the entity just outside of the grid for
             * debugging purposes. */
            entity.setPosition(new Point(-1, -1));
            this.entities.remove(entity);
            this.setOccupancyCell(pos, null);
        }
    }

    public Optional<PImage> getBackgroundImage(Point pos)
    {
        if (this.withinBounds(pos)) {
            return Optional.of(this.getBackgroundCell(pos).getCurrentImage());
        }
        else {
            return Optional.empty();
        }
    }

    public void setBackground(Point pos, Background background)
    {
        if (this.withinBounds(pos)) {
            this.setBackgroundCell(pos, background);
        }
    }

    public Optional<Entity> getOccupant(Point pos) {
        if (this.isOccupied(pos)) {
            return Optional.of(this.getOccupancyCell(pos));
        }
        else {
            return Optional.empty();
        }
    }

    public Entity getOccupancyCell(Point pos) {
        return this.occupancy[pos.y][pos.x];
    }

    private void setOccupancyCell(Point pos, Entity entity)
    {
        this.occupancy[pos.y][pos.x] = entity;
    }

    private Background getBackgroundCell(Point pos) {
        return this.background[pos.y][pos.x];
    }

    private void setBackgroundCell(Point pos, Background background)
    {
        this.background[pos.y][pos.x] = background;
    }

    private boolean parseBackground(
            String[] properties, ImageStore imageStore)
    {
        if (properties.length == Functions.BGND_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[Functions.BGND_COL]),
                    Integer.parseInt(properties[Functions.BGND_ROW]));
            String id = properties[Functions.BGND_ID];
            this.setBackground(pt, new Background(id, imageStore.getImageList(id)));
        }

        return properties.length == Functions.BGND_NUM_PROPERTIES;
    }

    private boolean parseSapling(
            String[] properties, ImageStore imageStore)
    {
        if (properties.length == Functions.SAPLING_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[Functions.SAPLING_COL]),
                    Integer.parseInt(properties[Functions.SAPLING_ROW]));
            String id = properties[Functions.SAPLING_ID];
            int health = Integer.parseInt(properties[Functions.SAPLING_HEALTH]);
            Entity entity = new Sapling(id, pt, imageStore.getImageList(Functions.SAPLING_KEY),
                    Functions.SAPLING_ACTION_ANIMATION_PERIOD, Functions.SAPLING_ACTION_ANIMATION_PERIOD, health, Functions.SAPLING_HEALTH_LIMIT);
            this.tryAddEntity(entity);
        }

        return properties.length == Functions.SAPLING_NUM_PROPERTIES;
    }

    private boolean parseDude(
            String[] properties, ImageStore imageStore)
    {
        if (properties.length == Functions.DUDE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[Functions.DUDE_COL]),
                    Integer.parseInt(properties[Functions.DUDE_ROW]));
            Entity entity = new DudeNotFull(properties[Functions.DUDE_ID], pt,
                    imageStore.getImageList(Functions.DUDE_KEY),
                    Integer.parseInt(properties[Functions.DUDE_ANIMATION_PERIOD]),
                    Integer.parseInt(properties[Functions.DUDE_ACTION_PERIOD]),
                    Integer.parseInt(properties[Functions.DUDE_LIMIT]), 0);
            this.tryAddEntity(entity);
        }

        return properties.length == Functions.DUDE_NUM_PROPERTIES;
    }

    private boolean parseFairy(
            String[] properties, ImageStore imageStore)
    {
        if (properties.length == Functions.FAIRY_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[Functions.FAIRY_COL]),
                    Integer.parseInt(properties[Functions.FAIRY_ROW]));
            Entity entity = new Fairy(properties[Functions.FAIRY_ID], pt,
                    imageStore.getImageList(Functions.FAIRY_KEY),
                    Integer.parseInt(properties[Functions.FAIRY_ANIMATION_PERIOD]),
                    Integer.parseInt(properties[Functions.FAIRY_ACTION_PERIOD]));
            this.tryAddEntity(entity);
        }

        return properties.length == Functions.FAIRY_NUM_PROPERTIES;
    }

    private boolean parseTree(
            String[] properties, ImageStore imageStore)
    {
        if (properties.length == Functions.TREE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[Functions.TREE_COL]),
                    Integer.parseInt(properties[Functions.TREE_ROW]));
            Entity entity = new Tree(properties[Functions.TREE_ID], pt,
                    imageStore.getImageList(Functions.TREE_KEY),
                    Integer.parseInt(properties[Functions.TREE_ANIMATION_PERIOD]),
                    Integer.parseInt(properties[Functions.TREE_ACTION_PERIOD]),
                    Integer.parseInt(properties[Functions.TREE_HEALTH]));
            this.tryAddEntity(entity);
        }

        return properties.length == Functions.TREE_NUM_PROPERTIES;
    }

    private boolean parseObstacle(
            String[] properties, ImageStore imageStore)
    {
        if (properties.length == Functions.OBSTACLE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[Functions.OBSTACLE_COL]),
                    Integer.parseInt(properties[Functions.OBSTACLE_ROW]));
            Entity entity = new Obstacle(properties[Functions.OBSTACLE_ID], pt,
                    imageStore.getImageList(Functions.OBSTACLE_KEY),
                    Integer.parseInt(properties[Functions.OBSTACLE_ANIMATION_PERIOD]));
            this.tryAddEntity(entity);
        }

        return properties.length == Functions.OBSTACLE_NUM_PROPERTIES;
    }

    private boolean parseHouse(
            String[] properties, ImageStore imageStore)
    {
        if (properties.length == Functions.HOUSE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[Functions.HOUSE_COL]),
                    Integer.parseInt(properties[Functions.HOUSE_ROW]));
            Entity entity = new House(properties[Functions.HOUSE_ID], pt,
                    imageStore.getImageList(Functions.HOUSE_KEY));
            this.tryAddEntity(entity);
        }

        return properties.length == Functions.HOUSE_NUM_PROPERTIES;
    }
}
