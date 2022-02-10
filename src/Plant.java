import java.util.Random;

public interface Plant extends Active{
    int getHealth();

    void decrementHealth();

    static int getNumFromRange(int max, int min) {
        Random rand = new Random();
        return min + rand.nextInt(max - min);
    }
}
