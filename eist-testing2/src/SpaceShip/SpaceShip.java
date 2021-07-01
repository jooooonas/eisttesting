package SpaceShip;

import GameBoard.Angle;
import GameBoard.Dimension2D;
import GameBoard.Vector2D;
import GameObjectRepository.Debris;
import GameObjectRepository.GameObject;
import GameObjectRepository.Obstacle;
import GameObjectRepository.Projectile;
import Player.Player;
import javafx.scene.input.KeyCode;

public class SpaceShip extends GameObject {
	private Player owner;
	private final static Dimension2D DEFAULT_SIZE = new Dimension2D(30, 40);
	private final static String SPACESHIP_ICON = "spaceship.png";
	private final static String SPACESHIP_ICON_NOFIRE = "spaceship_nofire.png";
	private final static double SPACESHIP_SPEED = 0.5;
	private final static double SPACESHIP_MASS = 1;

	private boolean accelerating;

	public SpaceShip(Vector2D position, double direction, Player owner) {
		super(position, Vector2D.zero(), SPACESHIP_SPEED, direction, SPACESHIP_MASS);
		size = DEFAULT_SIZE;
		iconLocation = SPACESHIP_ICON;
		this.owner = owner;
		velocity = Vector2D.fromPolar(0, direction);
	}

	@Override
	public boolean crash(GameObject object) {
		// If spaceship crashes with an Obstacle: Game over
		if (object instanceof Obstacle) {
			this.destroyed = true;
			return true;
		} else if (object instanceof Debris) {
			crashDebris(object);
		}
		// Can the spaceship crash into another spaceship or into a projectile?
		return destroyed;
	}

	@Override
	public void fly(Dimension2D gameBoardSize) {
		// change spaceships position
		position.add(velocity);

		// check if spaceship is still on the screen
		if (isOffScreen(gameBoardSize)) {
			Vector2D reverse = new Vector2D(velocity.x, velocity.y);
			// Falls man nur in eine Richtung aus dem Spielfeld fliegt: neue Velocity =
			// Ausfallswinkel
			Vector2D newVelocity = null;
			if ((position.x < 0 || position.x > gameBoardSize.getWidth() - size.getWidth())
					&& position.y < gameBoardSize.getHeight() && position.y > 0) {
				newVelocity = new Vector2D(-position.x, 0);
				newVelocity.addMagnitude(velocity.getMagnitude() - newVelocity.getMagnitude());
			} else if ((position.y < 0 || position.y > gameBoardSize.getHeight() - size.getHeight())
					&& position.x < gameBoardSize.getHeight() && position.x > 0) {
				newVelocity = new Vector2D(0, -position.y);
				newVelocity.addMagnitude(velocity.getMagnitude() - newVelocity.getMagnitude());
			} else {
				newVelocity = new Vector2D(gameBoardSize.getWidth() / 2 - position.x,
						gameBoardSize.getHeight() / 2 - position.y);
				newVelocity.addMagnitude(velocity.getMagnitude() - newVelocity.getMagnitude());
			}
			velocity = newVelocity;
			position.add(velocity);
		}
		velocity.multiply(0.99); // friction
	}

	public void accelerate() {
		Vector2D addedVelocity = Vector2D.fromPolar(SPACESHIP_SPEED, direction.get());
		velocity.add(addedVelocity);
	}

	public void accelerate(Vector2D acceleration) {
		velocity.add(acceleration);
	}

	public void rotateLeft() {
		direction.add(-10);
	}

	public void rotateRight() {
		direction.add(10);
	}

	// crash of Debris and spaceship: Just swap their velocities and directions
	private void crashDebris(GameObject debris) {
		if (debris instanceof Debris) {
			Vector2D swapVelocity = this.velocity.clone();
//			this.velocity = debris.getVelocity().clone();
//			((Debris) debris).setVelocity(swapVelocity);

			Angle swapDirection = this.direction.clone();
//			this.direction = debris.getDirection().clone();
//			((Debris) debris).setDirection(swapDirection);
		}
	}

	// Method to shoot projectile, which then is returned
	public Projectile shoot() {
		// create a Vector with the correct distance to the spaceship and rotate it into
		// the direction the spaceship is facing
		Vector2D directionProjectile = new Vector2D(0, -DEFAULT_SIZE.getWidth());
		directionProjectile.rotateBy(direction.get());
		// spawnPoint = location immediately in front of spaceship to spawn new
		// Projectile
		double middleX = getCenteredPosition().x; // x coordinate of the center of the spaceship
		double middleY = getCenteredPosition().y; // y coordinate of the center of the spaceship
		Vector2D spawnPoint = new Vector2D(middleX + directionProjectile.x, middleY + directionProjectile.y);
		return new Projectile(spawnPoint, direction.get());
	}

	// Getter
	public Player getOwner() {
		return owner;
	}

	@Override
	public String getIconLocation() {
		if (owner.getPressedKeys().contains(Player.ACCELERATE_KEY))
			return SPACESHIP_ICON;
		else
			return SPACESHIP_ICON_NOFIRE;
	}
}
