package GameObjectRepository;

import GameBoard.Angle;
import GameBoard.Dimension2D;
import GameBoard.Vector2D;

public abstract class GameObject {
	protected Vector2D position;
	protected double speed; // How fast the game object can accelerate.
	protected Vector2D velocity; // How fast and where the object is flying.
	protected Dimension2D size;
	protected double mass;
	protected String iconLocation;
	protected boolean destroyed;
	protected Angle direction; // Which direction does the object face.

	public GameObject(Vector2D position, Vector2D velocity, double speed, double direction, double mass) {
		this.position = position;
		this.speed = speed;
		this.direction = new Angle(direction);
		this.velocity = velocity;
		this.mass = mass;
	}

	// Methode zum Crashen. Gibt true zurück, falls erfolgreich gecrashed
	public abstract boolean crash(GameObject gameObject);

	public abstract void fly(Dimension2D gameBoardSize);

	// Gibt die Position des Objects zurück
	public Vector2D getPosition() {
		return position;
	}

	public Vector2D getCenteredPosition() {
		Vector2D offset = new Vector2D(size.getWidth() / 2, size.getHeight() / 2);
		Vector2D centeredPosition = position.clone();
		centeredPosition.add(offset);
		return centeredPosition;
	}

	public double getSpeed() {
		return speed;
	}

	public Vector2D getVelocity() {
		return velocity;
	}

	public Dimension2D getSize() {
		return size;
	}

	public String getIconLocation() {
		return iconLocation;
	}

	public boolean isDestroyed() {
		return destroyed;
	}

	public Angle getDirection() {
		return direction;
	}

	public double getMass() {
		return mass;
	}

	protected boolean isOffScreen(Dimension2D gameBoardSize) {
		double maxX = gameBoardSize.getWidth();
		double maxY = gameBoardSize.getHeight();
		double x = position.x;
		double y = position.y;
		return (y < 0 || y + this.size.getHeight() > maxY || x < 0 || x + this.size.getWidth() > maxX);
	}
}
