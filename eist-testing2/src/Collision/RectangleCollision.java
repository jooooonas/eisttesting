package Collision;

import GameBoard.Dimension2D;
import GameBoard.Vector2D;
import GameObjectRepository.GameObject;

import java.awt.*;

public class RectangleCollision implements CollisionType {
    private boolean isCollision;
    private GameObject object1;
    private GameObject object2;

    public RectangleCollision(GameObject object1, GameObject object2) {
        this.object1 = object1;
        this.object2 = object2;
        this.isCollision = detectCollision();
    }

    @Override
    public boolean detectCollision() {
        Vector2D p1 = object1.getPosition();
        Dimension2D d1 = object1.getSize();
        Vector2D p2 = object2.getPosition();
        Dimension2D d2 = object2.getSize();

        Rectangle r1 = new Rectangle((int) p1.getX(), (int) p1.getY(), (int) d1.getWidth(), (int) d1.getHeight());
        Rectangle r2 = new Rectangle((int) p2.getX(), (int) p2.getY(), (int) d2.getWidth(), (int) d2.getHeight());

        return r1.intersects(r2);
    }

    @Override
    public boolean isCrash() {
        return isCollision;
    }
}
