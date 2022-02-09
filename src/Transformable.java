public interface Transformable extends Entity {
    boolean transform(  WorldModel world,
                        EventScheduler scheduler,
                        ImageStore imageStore);
}
