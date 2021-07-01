package GameObjectRepository;


import GameBoard.Vector2D;

public abstract class Obstacle extends GameObject {
    
    public Obstacle(Vector2D position, int speed, int direction, double mass) {
        super(position, Vector2D.zero(), speed, direction, mass);
    }
}
