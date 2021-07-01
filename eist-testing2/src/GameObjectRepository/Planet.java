package GameObjectRepository;


import GameBoard.Dimension2D;
import GameBoard.Vector2D;
import java.util.Random;

public class Planet extends Obstacle {
    public static final int PLANET_WIDTH = 100;
    public static final int PLANET_HEIGTH = 100;
    public static final double PLANET_MASS = 3;


    //TODO Size bei einem Planeten Veränderbar?
    public Planet(Vector2D position) {
        super(position, 0, 0, PLANET_MASS);
        Random rand = new Random();
        this.destroyed = false;
        int size = PLANET_WIDTH / 2 + rand.nextInt(PLANET_WIDTH);
        this.size = new Dimension2D(size, size);
        this.mass = PLANET_MASS * size;
        int iconNumber = rand.nextInt(4) + 1;
        this.iconLocation = "planet" + iconNumber + ".png";
    }

    @Override
    public boolean crash(GameObject object) {
        return false;
    }

    @Override
    public void fly(Dimension2D gameBoardSize) {
        return;
    }
}
