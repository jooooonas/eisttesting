package Collision;


import GameObjectRepository.GameObject;


public class CollisionPolicy {
    private CollisionContext context;

    public CollisionPolicy(CollisionContext context) {
        this.context = context;
    }

    public void configure(GameObject object1, GameObject object2) {
        if (context.isRectangle()) {
            context.setCollisionType(new RectangleCollision(object1, object2));
        }
        else {
            context.setCollisionType(new CircleCollision(object1, object2));
        }
    }
}
