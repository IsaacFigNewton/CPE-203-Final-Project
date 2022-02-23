import java.util.Random;

abstract class Plant implements Active{
    protected int health;

    public int getHealth() {
        return health;
    }

    static int getNumFromRange(int max, int min) {
        Random rand = new Random();
        return min + rand.nextInt(max - min);
    }
}
