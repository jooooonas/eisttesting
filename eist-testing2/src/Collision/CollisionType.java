package Collision;

import GameObjectRepository.GameObject;

public interface CollisionType {
    boolean isCrash();
    boolean detectCollision();
}
