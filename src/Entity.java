import processing.core.PImage;
import java.util.*;

interface Entity {

    String getId();

    List<PImage> getImages();

    int getImageIndex();

    Point getPosition();

    void setPosition(Point newPosition);

    default PImage getCurrentImage() { return this.getImages().get(this.getImageIndex());}

}
