import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import processing.core.*;

//how to convert drawio file to graphml one
//does everything have to have health?

public final class VirtualWorld extends PApplet
{
    private static final int TIMER_ACTION_PERIOD = 100;

    private static final int VIEW_WIDTH = 640;
    private static final int VIEW_HEIGHT = 480;
    private static final int TILE_WIDTH = 32;
    private static final int TILE_HEIGHT = 32;
    private static final int WORLD_WIDTH_SCALE = 2;
    private static final int WORLD_HEIGHT_SCALE = 2;

    private static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
    private static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
    private static final int WORLD_COLS = VIEW_COLS * WORLD_WIDTH_SCALE;
    private static final int WORLD_ROWS = VIEW_ROWS * WORLD_HEIGHT_SCALE;

    private static final String IMAGE_LIST_FILE_NAME = "imagelist";
    private static final String DEFAULT_IMAGE_NAME = "background_default";
    private static final int DEFAULT_IMAGE_COLOR = 0x808080;

    private static String LOAD_FILE_NAME = "world.sav";

    private static final String FAST_FLAG = "-fast";
    private static final String FASTER_FLAG = "-faster";
    private static final String FASTEST_FLAG = "-fastest";
    private static final double FAST_SCALE = 0.5;
    private static final double FASTER_SCALE = 0.25;
    private static final double FASTEST_SCALE = 0.10;

    private static double timeScale = 1.0;

    private ImageStore imageStore;
    private WorldModel world;
    private WorldView view;
    private EventScheduler scheduler;

    private long nextTime;

    public void settings() {
        size(VIEW_WIDTH, VIEW_HEIGHT);
    }

    public ImageStore getImageStore() {
        return imageStore;
    }

    /*
           Processing entry point for "sketch" setup.
        */
    public void setup() {
        this.imageStore = new ImageStore(
                createImageColored(TILE_WIDTH, TILE_HEIGHT,
                                   DEFAULT_IMAGE_COLOR));
        this.world = new WorldModel(WORLD_ROWS, WORLD_COLS,
                                    createDefaultBackground(imageStore));
        this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world, TILE_WIDTH,
                                  TILE_HEIGHT);
        this.scheduler = new EventScheduler(timeScale);

        loadImages(IMAGE_LIST_FILE_NAME, imageStore, this);
        loadWorld(world, LOAD_FILE_NAME, imageStore);

        scheduleActions(world, scheduler, imageStore);

        nextTime = System.currentTimeMillis() + TIMER_ACTION_PERIOD;
        //world.addEntity(new Swamp(Functions.SWAMP_KEY, new Point(0,1), imageStore.getImageList(Functions.SWAMP_KEY)));
    }

    public void draw() {
        long time = System.currentTimeMillis();
        if (time >= nextTime) {
            this.scheduler.updateOnTime(time);
            nextTime = time + TIMER_ACTION_PERIOD;
        }

        view.drawViewport();
    }

    // Just for debugging and for P5
    // This should be refactored as appropriate
    public void mousePressed() {
        Point pressed = mouseToPoint(mouseX, mouseY);

//    Optional<Entity> entityOptional = world.getOccupant(pressed);
//
//        if (entityOptional.isPresent())
//        {
//            //System.out.println("test1");
//
//            Entity entity = entityOptional.get();
//            System.out.println(entity.getId() + ": " + entity.getClass() + ": " + entity.getId());
//            //System.out.print(entity.getClass());
////            if (entity instanceof Plant){
////                System.out.println(" : " + ((Plant)entity).getHealth());
////            }
////            if (entity instanceof Dude){
////                System.out.println(" Resources: " + ((Dude)entity).getResourceCount() + "/" + ((Dude)entity).getResourceLimit());
////            }
//        }
//        world.addEntity(new Swamp(Functions.SWAMP_KEY, pressed, imageStore.getImageList(Functions.SWAMP_KEY)));

        //set swamp backgrounds randomly around where the user clicked
        int radius = 7;
        int numTiles = 7;   //max number of tiles = round(radius / 2) ^ 2 ()
        Function<Integer, Integer> diff = (i) -> (int)(Math.random() * radius) - radius/2;
        ArrayList<Point> spawnPoints = new ArrayList<>();

        //generate 7 unique, valid spawn points around the point where the user clicked within the defined radius
        while (spawnPoints.size() < numTiles) {
            Point potentialSpawnPoint = new Point(pressed.x + diff.apply(0), pressed.y + diff.apply(0));
            if (!spawnPoints.contains(potentialSpawnPoint)
                    && (!world.isOccupied(potentialSpawnPoint)
                        //make sure a new Swamp can be added on top of an old one to avoid the program breaking
                        || world.getOccupant(potentialSpawnPoint).stream()
                            .findFirst()
                            .orElse(null) instanceof Swamp))
                spawnPoints.add(potentialSpawnPoint);
        }

        for (Point point : spawnPoints) {
            //set background tiles at those points to swamp tiles
            world.setBackground(point,
                    new Background("swamp",
                            imageStore.getImageList(Functions.SWAMP_KEY)));

            //set swamp entities on those tiles with transparent images so that they don't cover dude
            world.addEntity(new Swamp(Functions.TRANSPARENT_KEY, point, imageStore.getImageList(Functions.TRANSPARENT_KEY)));

//            System.out.println(point);
        }


        System.out.println();

        //if a dude or a fairy runs over a swamp tile, shrek spawns and hunts them down to eat them

        //once shrek has eaten a dude or fairy, he returns to the nearest swamp

    }

    private Point mouseToPoint(int x, int y)
    {
        return view.getViewport().viewportToWorld(mouseX/TILE_WIDTH, mouseY/TILE_HEIGHT);
    }

    public void keyPressed() {
        if (key == CODED) {
            int dx = 0;
            int dy = 0;

            switch (keyCode) {
                case UP:
                    dy = -1;
                    break;
                case DOWN:
                    dy = 1;
                    break;
                case LEFT:
                    dx = -1;
                    break;
                case RIGHT:
                    dx = 1;
                    break;
            }
            view.shiftView(dx, dy);
        }
    }

    private static Background createDefaultBackground(ImageStore imageStore) {
        return new Background(DEFAULT_IMAGE_NAME,
                              imageStore.getImageList(DEFAULT_IMAGE_NAME));
    }

    private static PImage createImageColored(int width, int height, int color) {
        PImage img = new PImage(width, height, RGB);
        img.loadPixels();
        for (int i = 0; i < img.pixels.length; i++) {
            img.pixels[i] = color;
        }
        img.updatePixels();
        return img;
    }

    public static void loadImages(
            String filename, ImageStore imageStore, PApplet screen)
    {
        try {
            Scanner in = new Scanner(new File(filename));
            imageStore.loadImages(in, screen);
        }
        catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void loadWorld(
            WorldModel world, String filename, ImageStore imageStore)
    {
        try {
            Scanner in = new Scanner(new File(filename));
            world.load(in, imageStore);
        }
        catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void scheduleActions(
            WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        for (Entity entity : world.entities) {
            if (entity instanceof Dynamic)
                ((Dynamic)entity).scheduleAction(scheduler, world, imageStore);
        }
    }

    private static void parseCommandLine(String[] args) {
        if (args.length > 1)
        {
            if (args[0].equals("file"))
            {

            }
        }

        for (String arg : args) {
            switch (arg) {
                case FAST_FLAG:
                    timeScale = Math.min(FAST_SCALE, timeScale);
                    break;
                case FASTER_FLAG:
                    timeScale = Math.min(FASTER_SCALE, timeScale);
                    break;
                case FASTEST_FLAG:
                    timeScale = Math.min(FASTEST_SCALE, timeScale);
                    break;
            }
        }
    }

    public static void main(String[] args) {
        parseCommandLine(args);
        PApplet.main(VirtualWorld.class);
    }
}
