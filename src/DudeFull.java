import processing.core.PImage;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

public class DudeFull extends Dude {

    public DudeFull(
            String id,
            Point position,
            List<PImage> images,
            int animationPeriod,
            int actionPeriod,
            int resourceLimit)
    {
        super(id, position, images, animationPeriod, actionPeriod, resourceLimit);
    }

    public DudeFull(Dude other)
    {
        super(other);
    }

    public boolean executeActivityCondition(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> target =
                world.findNearest(this.position, new ArrayList<>(Arrays.asList(House.class)));

        if (target.isPresent() && this.moveToDude(world, target.get(), scheduler,imageStore)) {
            this.transform(world, scheduler, imageStore);
            return false;
        }

        return true;
    }

    public boolean moveToActivity(
            WorldModel world,
            Entity target,
            EventScheduler scheduler) { return true; }

    //transform into a doodNotFull
    public boolean transform(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        //find a potentially valid spawn point around the point where the DudeFull is becoming a DudeNotFull
        Function<Integer, Integer> diff = (i) -> (int)(Math.random() * 2) - 1;
        Point potentialSpawnPoint = new Point(this.position.x + diff.apply(0), this.position.y + diff.apply(0));

        //make sure the point is valid
        while (world.isOccupied(potentialSpawnPoint)) {
            potentialSpawnPoint = new Point(this.position.x + diff.apply(0), this.position.y + diff.apply(0));
        }

        // need resource count, though it always starts at 0
        Dude dudeNotFull = new DudeNotFull(this, 0);
        //world.addEntity(dudeNotFull);

        //return true;
        babyMaker(world, scheduler, imageStore);
        return super.transform(world, scheduler, imageStore, dudeNotFull);
    }
    public void babyMaker(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore){
        //bring daBaby into this world
        Point babyLoco = this.position;
//
//        int[] difs = {-1, 0, 1};
//        Function<Point, List<Point>> getPossibleSpawnPoints = (p) -> Arrays.asList(
//                new Point(babyLoco.x-1,babyLoco.y),
//                new Point(babyLoco.x+1,babyLoco.y),
//                new Point(babyLoco.x,babyLoco.y+1),
//                new Point(babyLoco.x,babyLoco.y-1),
//                new Point(babyLoco.x+1,babyLoco.y+1),
//                new Point(babyLoco.x+1,babyLoco.y-1),
//                new Point(babyLoco.x-1,babyLoco.y+1),
//                new Point(babyLoco.x-1,babyLoco.y-1)
//        );
//
//        Point spawnPoint = getPossibleSpawnPoints.apply(babyLoco).stream()
//                .filter((p) -> !world.isOccupied(p))
//                .limit(1)
//                .findFirst()
//                .orElse(null);
//
//        if (spawnPoint != null) {
//            DudeNotFull daBaby = new DudeNotFull("dude", spawnPoint, this.images, this.animationPeriod, this.actionPeriod, this.resourceLimit, 0);
//            super.transform(world, scheduler, imageStore, daBaby);
//        }

        if(!world.isOccupied(this.position)){
            DudeNotFull daBaby = new DudeNotFull("dude",this.position,this.images,this.animationPeriod,this.actionPeriod,this.resourceLimit, 0);
            super.transform(world, scheduler, imageStore, daBaby);}
        else if(!world.isOccupied(new Point(babyLoco.x+1,babyLoco.y))){
            DudeNotFull daBaby = new DudeNotFull("dude",(new Point(babyLoco.x+1,babyLoco.y)),this.images,this.animationPeriod,this.actionPeriod,this.resourceLimit, 0);
            super.transform(world, scheduler, imageStore, daBaby);}
        else if(!world.isOccupied(new Point(babyLoco.x-1,babyLoco.y))){
            DudeNotFull daBaby = new DudeNotFull("dude",(new Point(babyLoco.x-1,babyLoco.y)),this.images,this.animationPeriod,this.actionPeriod,this.resourceLimit, 0);
            super.transform(world, scheduler, imageStore, daBaby);}
        else if(!world.isOccupied(new Point(babyLoco.x,babyLoco.y+1))){
            DudeNotFull daBaby = new DudeNotFull("dude",(new Point(babyLoco.x,babyLoco.y+1)),this.images,this.animationPeriod,this.actionPeriod,this.resourceLimit, 0);
            super.transform(world, scheduler, imageStore, daBaby);}
        else if(!world.isOccupied(new Point(babyLoco.x,babyLoco.y-1))){
            DudeNotFull daBaby = new DudeNotFull("dude",(new Point(babyLoco.x,babyLoco.y-1)),this.images,this.animationPeriod,this.actionPeriod,this.resourceLimit, 0);
//            super.transform(world, scheduler, imageStore, daBaby);}
//        else if(!world.isOccupied(new Point(babyLoco.x+1,babyLoco.y+1))){
//            DudeNotFull daBaby = new DudeNotFull("dude",(new Point(babyLoco.x+1,babyLoco.y+1)),this.images,this.animationPeriod,this.actionPeriod,this.resourceLimit, 0);
//            super.transform(world, scheduler, imageStore, daBaby);}
//        else if(!world.isOccupied(new Point(babyLoco.x-1,babyLoco.y-1))){
//            DudeNotFull daBaby = new DudeNotFull("dude",(new Point(babyLoco.x-1,babyLoco.y-1)),this.images,this.animationPeriod,this.actionPeriod,this.resourceLimit, 0);
//            super.transform(world, scheduler, imageStore, daBaby);}
//        else if(!world.isOccupied(new Point(babyLoco.x-1,babyLoco.y+1))){
//            DudeNotFull daBaby = new DudeNotFull("dude",(new Point(babyLoco.x-1,babyLoco.y+1)),this.images,this.animationPeriod,this.actionPeriod,this.resourceLimit, 0);
//            super.transform(world, scheduler, imageStore, daBaby);}
//        else if(!world.isOccupied(new Point(babyLoco.x+1,babyLoco.y-1))){
//            DudeNotFull daBaby = new DudeNotFull("dude",(new Point(babyLoco.x+1,babyLoco.y-1)),this.images,this.animationPeriod,this.actionPeriod,this.resourceLimit, 0);
//            super.transform(world, scheduler, imageStore, daBaby);}
        System.out.println("test1");

    }
}}

