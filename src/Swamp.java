import processing.core.PImage;

import java.util.List;

public class Swamp extends Entity {
    private boolean hasShrek;
    public Swamp(
            String id,
            Point position,
            List<PImage> images)
    {
        super(id, position, images);
        this.hasShrek = false;
    }
    public boolean hasShrek(){
        return hasShrek;
    }
    public void setHasShrek(boolean yn){
        this.hasShrek = yn;
    }
}