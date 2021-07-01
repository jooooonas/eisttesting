package Collision;


import GameObjectRepository.GameObject;
import GameObjectRepository.Planet;

public class CollisionContext {
    private CollisionType collisionType;
    private GameObject object1;
    private GameObject object2;

    public CollisionContext(GameObject object1, GameObject object2) {
        this.object1 = object1;
        this.object2 = object2;
    }
    
    public boolean isRectangle() {
        if (object1 instanceof Planet || object2 instanceof Planet) {
            return false;
        }
        else {
            return true;
        }
    }

    public CollisionType getCollisionType() {
        return collisionType;
    }

    public void setCollisionType(CollisionType collisionType) {
        this.collisionType = collisionType;
    }
}
