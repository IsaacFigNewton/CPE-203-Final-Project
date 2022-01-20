import processing.core.PImage;

import java.util.*;

/**
 * Represents the 2D World in which this simulation is running.
 * Keeps track of the size of the world, the background image for each
 * location in the world, and the entities that populate the world.
 */
public final class WorldModel
{
    public int numRows;
    public int numCols;
    public Background background[][];
    public Entity occupancy[][];
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

    public Optional<Entity> findNearest(Point pos, List<EntityKind> kinds)
    {
        List<Entity> ofType = new LinkedList<>();
        for (EntityKind kind: kinds)
        {
            for (Entity entity : this.entities) {
                if (entity.kind == kind) {
                    ofType.add(entity);
                }
            }
        }

        return Functions.nearestEntity(ofType, pos);
    }

    /*
           Assumes that there is no entity currently occupying the
           intended destination cell.
        */
    public void addEntity(Entity entity) {
        if (Functions.withinBounds(this, entity.position)) {
            this.setOccupancyCell(entity.position, entity);
            this.entities.add(entity);
        }
    }

    public void moveEntity(Entity entity, Point pos) {
        Point oldPos = entity.position;
        if (Functions.withinBounds(this, pos) && !pos.equals(oldPos)) {
            this.setOccupancyCell(oldPos, null);
            this.removeEntityAt(pos);
            this.setOccupancyCell(pos, entity);
            entity.position = pos;
        }
    }

    public void removeEntity(Entity entity) {
        this.removeEntityAt(entity.position);
    }

    public void removeEntityAt(Point pos) {
        if (Functions.withinBounds(this, pos) && this.getOccupancyCell(pos) != null) {
            Entity entity = this.getOccupancyCell(pos);

            /* This moves the entity just outside of the grid for
             * debugging purposes. */
            entity.position = new Point(-1, -1);
            this.entities.remove(entity);
            this.setOccupancyCell(pos, null);
        }
    }

    public Optional<PImage> getBackgroundImage(Point pos)
    {
        if (Functions.withinBounds(this, pos)) {
            return Optional.of(this.getBackgroundCell(pos).getCurrentImage());
        }
        else {
            return Optional.empty();
        }
    }

    public void setBackground(Point pos, Background background)
    {
        if (Functions.withinBounds(this, pos)) {
            this.setBackgroundCell(pos, background);
        }
    }

    public Optional<Entity> getOccupant(Point pos) {
        if (Functions.isOccupied(this, pos)) {
            return Optional.of(this.getOccupancyCell(pos));
        }
        else {
            return Optional.empty();
        }
    }

    public Entity getOccupancyCell(Point pos) {
        return this.occupancy[pos.y][pos.x];
    }

    public void setOccupancyCell(Point pos, Entity entity)
    {
        this.occupancy[pos.y][pos.x] = entity;
    }

    public Background getBackgroundCell(Point pos) {
        return this.background[pos.y][pos.x];
    }

    public void setBackgroundCell(Point pos, Background background)
    {
        this.background[pos.y][pos.x] = background;
    }

    public boolean parseBackground(
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

    public boolean parseSapling(
            String[] properties, ImageStore imageStore)
    {
        if (properties.length == Functions.SAPLING_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[Functions.SAPLING_COL]),
                    Integer.parseInt(properties[Functions.SAPLING_ROW]));
            String id = properties[Functions.SAPLING_ID];
            int health = Integer.parseInt(properties[Functions.SAPLING_HEALTH]);
            Entity entity = new Entity(EntityKind.SAPLING, id, pt, imageStore.getImageList(Functions.SAPLING_KEY), 0, 0,
                    Functions.SAPLING_ACTION_ANIMATION_PERIOD, Functions.SAPLING_ACTION_ANIMATION_PERIOD, health, Functions.SAPLING_HEALTH_LIMIT);
            Functions.tryAddEntity(this, entity);
        }

        return properties.length == Functions.SAPLING_NUM_PROPERTIES;
    }

    public boolean parseDude(
            String[] properties, ImageStore imageStore)
    {
        if (properties.length == Functions.DUDE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[Functions.DUDE_COL]),
                    Integer.parseInt(properties[Functions.DUDE_ROW]));
            Entity entity = Functions.createDudeNotFull(properties[Functions.DUDE_ID],
                    pt,
                    Integer.parseInt(properties[Functions.DUDE_ACTION_PERIOD]),
                    Integer.parseInt(properties[Functions.DUDE_ANIMATION_PERIOD]),
                    Integer.parseInt(properties[Functions.DUDE_LIMIT]),
                    imageStore.getImageList(Functions.DUDE_KEY));
            Functions.tryAddEntity(this, entity);
        }

        return properties.length == Functions.DUDE_NUM_PROPERTIES;
    }

    public boolean parseFairy(
            String[] properties, ImageStore imageStore)
    {
        if (properties.length == Functions.FAIRY_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[Functions.FAIRY_COL]),
                    Integer.parseInt(properties[Functions.FAIRY_ROW]));
            Entity entity = Functions.createFairy(properties[Functions.FAIRY_ID],
                    pt,
                    Integer.parseInt(properties[Functions.FAIRY_ACTION_PERIOD]),
                    Integer.parseInt(properties[Functions.FAIRY_ANIMATION_PERIOD]),
                    imageStore.getImageList(Functions.FAIRY_KEY));
            Functions.tryAddEntity(this, entity);
        }

        return properties.length == Functions.FAIRY_NUM_PROPERTIES;
    }

    public boolean parseTree(
            String[] properties, ImageStore imageStore)
    {
        if (properties.length == Functions.TREE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[Functions.TREE_COL]),
                    Integer.parseInt(properties[Functions.TREE_ROW]));
            Entity entity = Functions.createTree(properties[Functions.TREE_ID],
                    pt,
                    Integer.parseInt(properties[Functions.TREE_ACTION_PERIOD]),
                    Integer.parseInt(properties[Functions.TREE_ANIMATION_PERIOD]),
                    Integer.parseInt(properties[Functions.TREE_HEALTH]),
                    imageStore.getImageList(Functions.TREE_KEY));
            Functions.tryAddEntity(this, entity);
        }

        return properties.length == Functions.TREE_NUM_PROPERTIES;
    }

    public boolean parseObstacle(
            String[] properties, ImageStore imageStore)
    {
        if (properties.length == Functions.OBSTACLE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[Functions.OBSTACLE_COL]),
                    Integer.parseInt(properties[Functions.OBSTACLE_ROW]));
            Entity entity = Functions.createObstacle(properties[Functions.OBSTACLE_ID], pt,
                    Integer.parseInt(properties[Functions.OBSTACLE_ANIMATION_PERIOD]),
                    imageStore.getImageList(Functions.OBSTACLE_KEY));
            Functions.tryAddEntity(this, entity);
        }

        return properties.length == Functions.OBSTACLE_NUM_PROPERTIES;
    }

    public boolean parseHouse(
            String[] properties, ImageStore imageStore)
    {
        if (properties.length == Functions.HOUSE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[Functions.HOUSE_COL]),
                    Integer.parseInt(properties[Functions.HOUSE_ROW]));
            Entity entity = Functions.createHouse(properties[Functions.HOUSE_ID], pt,
                    imageStore.getImageList(Functions.HOUSE_KEY));
            Functions.tryAddEntity(this, entity);
        }

        return properties.length == Functions.HOUSE_NUM_PROPERTIES;
    }
}
