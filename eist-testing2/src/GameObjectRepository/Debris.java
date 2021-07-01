package GameObjectRepository;

import GameBoard.Angle;
import GameBoard.Dimension2D;
import GameBoard.Vector2D;
import SpaceShip.SpaceShip;

import java.util.Random;

public class Debris extends GameObject {
	private boolean small;
	private boolean pulverized;
	private boolean newlySpawned;

	public static final int DEFAULT_DEBRIS_WIDTH = 50;
	public static final int DEFAULT_DEBRIS_HEIGHT = 50;
	public static final int SMALL_DEBRIS_WIDTH = 25;
	public static final int SMALL_DEBRIS_HEIGHT = 25;
	public static final int DEFAULT_DEBRIS_MASS = 1;

	public static final int DEBRIS_MAX_VELOCITY_MAG = 7;
	public static final int DEBRIS_MIN_VELOCITY_MAG = 2;

	public Debris(Vector2D position, int speed, double direction) {
		super(position, randomDebrisVelocity(), speed, direction, DEFAULT_DEBRIS_WIDTH / 5);
		destroyed = false;
		this.small = false;
		this.size = new Dimension2D(DEFAULT_DEBRIS_WIDTH, DEFAULT_DEBRIS_HEIGHT);
		// TODO icon location
		this.iconLocation = "bigDebris.gif";
		newlySpawned = true;
	}

	public Debris(Vector2D position, Vector2D velocity, double speed, double direction, boolean small) {
		super(position, velocity, speed, direction, DEFAULT_DEBRIS_WIDTH / 5);
		destroyed = false;
		this.small = small;
		this.size = new Dimension2D(SMALL_DEBRIS_WIDTH, SMALL_DEBRIS_HEIGHT);
		// TODO icon location
		this.iconLocation = "smallDebris.gif";
		newlySpawned = true;
	}

	public Debris(Vector2D position, Vector2D velocity, double speed, double direction) {
		super(position, velocity, speed, direction, DEFAULT_DEBRIS_WIDTH / 5);
		destroyed = false;
		this.small = false;
		this.size = new Dimension2D(DEFAULT_DEBRIS_WIDTH, DEFAULT_DEBRIS_HEIGHT);
		// TODO icon location
		this.iconLocation = "bigDebris.gif";
		newlySpawned = true;
	}

	public static Vector2D randomDebrisVelocity() {
		return Vector2D.randomWithMag(DEBRIS_MIN_VELOCITY_MAG, DEBRIS_MAX_VELOCITY_MAG);
	}

	public static double randomDebrisVelocityMag() {
		return DEBRIS_MIN_VELOCITY_MAG
				+ (new Random().nextDouble() * (DEBRIS_MAX_VELOCITY_MAG - DEBRIS_MIN_VELOCITY_MAG));
	}

	@Override
	public boolean crash(GameObject object) {
		if (object instanceof Projectile && !small) {
			small = true;
			this.size = new Dimension2D(SMALL_DEBRIS_WIDTH, SMALL_DEBRIS_HEIGHT);
			pulverized = true;
		} else if (object instanceof Obstacle) {
			if (small) {
				destroyed = true;
			} else {
				turnCollision(object);
				// velocity.reverse();
			}
		} else if (object instanceof SpaceShip) {
			velocity.add(object.getVelocity().clone());
		} else if (object instanceof Debris) {
			turnCollision(object);
			// velocity.reverse();
		}

		return destroyed;
	}

	private void turnCollision(GameObject with) {
		Dimension2D currentSize = this.getSize();

		Vector2D withPosition = with.getPosition();
		Dimension2D withSize = with.getSize();

		Vector2D copyVelocity = velocity.clone();
		Vector2D oldPosition = position.clone();

		copyVelocity.reverse();
		copyVelocity.addMagnitude(speed);
		oldPosition.add(copyVelocity);

		double oldX = oldPosition.getX();
		double oldY = oldPosition.getY();

		boolean above = oldY + currentSize.getHeight() < withPosition.getY();

		if (above) {
			// von oben
			if (oldX - (withPosition.getX() + withSize.getWidth()) > oldY + size.getHeight() - withPosition.getY()
					&& withPosition.getX() - (oldX + size.getWidth()) > oldY + size.getHeight() - withPosition.getY()) {
				velocity = new Vector2D(velocity.getX(), velocity.y * (-1));
			} else {
				velocity = new Vector2D(velocity.getX() * (-1), velocity.getY());
			}
		} else {
			// von oben
			if (oldX - (withPosition.getX() + withSize.getWidth()) > oldY - withPosition.getY() + withSize.getHeight()
					&& withPosition.getX() - (oldX + size.getWidth()) > oldY - withPosition.getY()
							+ withSize.getHeight()) {
				velocity = new Vector2D(velocity.getX(), velocity.y * (-1));
			} else {
				velocity = new Vector2D(velocity.getX() * (-1), velocity.getY());
			}
		}
	}

	@Override
	public void fly(Dimension2D gameBoardSize) {
		if (destroyed) {
			return;
		}

		position.add(velocity);

		if (!newlySpawned && isOffScreen(gameBoardSize)) {
			destroyed = true;
		} else if (newlySpawned && !isOffScreen(gameBoardSize)) {
			newlySpawned = false;
		}
	}

	// Getter

	public boolean isSmall() {
		return small;
	}

	// Setter
	public void setVelocity(Vector2D newVelocity) {
		this.velocity = newVelocity;
	}

	public void setDirection(Angle direction) {
		this.direction = direction;
	}

	public boolean isPulverized() {
		return pulverized;
	}

	public void setPulverized(boolean pulverized) {
		this.pulverized = pulverized;
	}

	public boolean isNewlySpawned() {
		return newlySpawned;
	}

	public void setNewlySpawned(boolean newlySpawned) {
		this.newlySpawned = newlySpawned;
	}
}
