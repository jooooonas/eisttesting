package GameObjectRepository;

import GameBoard.Dimension2D;
import GameBoard.Vector2D;
import SpaceShip.SpaceShip;

public class Projectile extends GameObject {
	private static final int DEFAULT_PROJECTILE_WIDTH = 10;
	private static final int DEFAULT_PROJECTILE_HEIGHT = 30;
	public static final double PROJECTILE_SPEED = 10;
	private static final double PROJECTILE_MASS = 1;

	public Projectile(Vector2D position, double direction) {
		super(position, Vector2D.fromPolar(PROJECTILE_SPEED, direction), PROJECTILE_SPEED, direction, PROJECTILE_MASS);
		this.destroyed = false;
		this.size = new Dimension2D(DEFAULT_PROJECTILE_WIDTH, DEFAULT_PROJECTILE_HEIGHT);
		this.iconLocation = "projectile.gif";
	}

	public Projectile(Vector2D position, double direction, Vector2D velocity) {
		super(position, velocity, PROJECTILE_SPEED, direction, PROJECTILE_MASS);
		this.destroyed = false;
		this.size = new Dimension2D(DEFAULT_PROJECTILE_WIDTH, DEFAULT_PROJECTILE_HEIGHT);
		this.iconLocation = "projectile.gif";
	}

	@Override
	public boolean crash(GameObject object) {
		// Schauen, mit wem das Projectile kollidiert
		if (object instanceof Obstacle) {
			destroyed = true;
		} else if (object instanceof Debris) {
			destroyed = true;
		} else if (object instanceof SpaceShip) {
			destroyed = true;
		}
		return destroyed;
	}

	@Override
	public void fly(Dimension2D gameBoardSize) {
		if (destroyed) {
			return;
		}
		double maxX = gameBoardSize.getWidth();
		double maxY = gameBoardSize.getHeight();
		position.add(velocity);
		double newX = position.x;
		double newY = position.y;

		if (newY < 0 || newY + this.size.getHeight() > maxY || newX < 0 || newX + this.size.getWidth() > maxX) {
			destroyed = true;
			return;
		}
	}
}
