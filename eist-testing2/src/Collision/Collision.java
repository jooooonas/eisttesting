package Collision;

import GameBoard.Dimension2D;
import GameObjectRepository.GameObject;
import GameObjectRepository.Planet;

import java.awt.Rectangle;

import GameBoard.Vector2D;

public class Collision {
    private boolean isCollision;
    private final GameObject object1;
    private final GameObject object2;

    public Collision(GameObject object1, GameObject object2) {
        this.object1 = object1;
        this.object2 = object2;
        this.isCollision = detectCollision();
    }

    public boolean isCrash() {
        return this.isCollision;
    }

    public boolean detectCollision() {
        // If planet, decide by calculating distance and checking if larger than radius.
        if (object1 instanceof Planet || object2 instanceof Planet) {
            Planet planet;
            GameObject object;
            if (object1 instanceof Planet) {
                planet = (Planet) object1;
                object = object2;
            } else {
                planet = (Planet) object2;
                object = object1;
            }

            double planetRadius =  -30 +planet.getSize().getWidth() / 2 ;
            // Todo: don't just compare with object's width since height might be totally different. Instead, take the max. of the two
            double max;
            if (object.getSize().getWidth() > object.getSize().getHeight()) max = object.getSize().getWidth();
            else max = object.getSize().getHeight();
            double distance = planet.getCenteredPosition().distanceTo(object.getCenteredPosition()) - planetRadius - max;

            return distance <= 0;
        }

        Vector2D p1 = object1.getPosition();
        Dimension2D d1 = object1.getSize();
        Vector2D p2 = object2.getPosition();
        Dimension2D d2 = object2.getSize();

        Rectangle r1 = new Rectangle((int) p1.getX(), (int) p1.getY(), (int) d1.getWidth(), (int) d1.getHeight());
        Rectangle r2 = new Rectangle((int) p2.getX(), (int) p2.getY(), (int) d2.getWidth(), (int) d2.getHeight());

        return r1.intersects(r2);
    }
}
