package Collision;

import GameObjectRepository.GameObject;
import GameObjectRepository.Planet;

public class CircleCollision implements CollisionType {
    private boolean isCollision;
    private GameObject object1;
    private GameObject object2;

    public CircleCollision(GameObject object1, GameObject object2) {
        this.object1 = object1;
        this.object2 = object2;
        isCollision = detectCollision();
    }

    @Override
    public boolean detectCollision() {
        Planet planet;
        GameObject object;
        if (object1 instanceof Planet) {
            planet = (Planet) object1;
            object = object2;
        } else {
            planet = (Planet) object2;
            object = object1;
        }

        double planetRadius =  -30 + planet.getSize().getWidth() / 2 ;
        // Todo: don't just compare with object's width since height might be totally different. Instead, take the max. of the two
        double max;
        if (object.getSize().getWidth() > object.getSize().getHeight()) max = object.getSize().getWidth();
        else max = object.getSize().getHeight();
        double distance = planet.getCenteredPosition().distanceTo(object.getCenteredPosition()) - planetRadius - max;

        return distance <= 0;
    }

    @Override
    public boolean isCrash() {
        return isCollision;
    }
}
