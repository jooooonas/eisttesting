package Player;

import GameBoard.GameBoard;
import GameObjectRepository.Projectile;
import SpaceShip.SpaceShip;
import GameBoard.Vector2D;

import java.util.HashSet;
import java.util.Set;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Player implements PlayerInterface {
	private SpaceShip spaceShip;
	private Set<KeyCode> pressedKeys;
	private GameBoard gameBoard;
	private int cooldown; // counts up to assure the spaceship does not shoot too frequently

	public static final KeyCode ACCELERATE_KEY = KeyCode.W;
	public static final KeyCode SHOOT_KEY = KeyCode.SPACE;
	public static final int COOLDOWN_TIME = 10;

	public Player(Scene scene, GameBoard gameBoard) {
		this.gameBoard = gameBoard;
		spaceShip = new SpaceShip(gameBoard.randomPoint(), gameBoard.randomDirection(), this);
		// assure that spaceship is not set into another Object: setSpaceShip returns
		// false, if spaceship would have been placed into another Object
		while (!gameBoard.setSpaceShip(spaceShip)) {
			spaceShip = new SpaceShip(gameBoard.randomPoint(), gameBoard.randomDirection(), this);
		}
		scene.setOnKeyPressed(this::handleKeyPressed);
		scene.setOnKeyReleased(this::handleKeyReleased);
		pressedKeys = new HashSet<>();
		cooldown = COOLDOWN_TIME;
	}

	public Player(GameBoard gameBoard) {
		this.gameBoard = gameBoard;
		spaceShip = new SpaceShip(gameBoard.randomPoint(), gameBoard.randomDirection(), this);
		// assure that spaceship is not set into another Object: setSpaceShip returns
		// false, if spaceship would have been placed into another Object
		while (!gameBoard.setSpaceShip(spaceShip)) {
			spaceShip = new SpaceShip(gameBoard.randomPoint(), gameBoard.randomDirection(), this);
		}
		pressedKeys = new HashSet<>();
		cooldown = COOLDOWN_TIME;
	}

	private void handleKeyPressed(KeyEvent keyEvent) {
		pressedKeys.add(keyEvent.getCode());
	}

	private void handleKeyReleased(KeyEvent keyEvent) {
		pressedKeys.remove(keyEvent.getCode());
	}

	public void steer() {
		if (cooldown <= COOLDOWN_TIME) {
			cooldown++;
		}
		for (KeyCode keyCode : pressedKeys) {
			if (keyCode == ACCELERATE_KEY) {
				spaceShip.accelerate();
			} else if (keyCode == KeyCode.A) {
				spaceShip.rotateLeft();
			} else if (keyCode == KeyCode.D) {
				spaceShip.rotateRight();
			} else if (keyCode == SHOOT_KEY) {
				if (cooldown > COOLDOWN_TIME) {
					shoot();
					// pressedKeys.remove(KeyCode.SPACE);
					// reset shooting-cooldown
					cooldown = 0;
				}
			}
		}
	}

	// Method to shoot projectile, which then is returned
	public void shoot() {
		Vector2D spawnPoint = spaceShip.getCenteredPosition();
		spawnPoint.add(Vector2D.fromPolar(50, spaceShip.getDirection().get()));

		Vector2D velocity = spaceShip.getVelocity().clone();
		velocity.add(Vector2D.fromPolar(Projectile.PROJECTILE_SPEED, spaceShip.getDirection().get()));
		Projectile projectile = new Projectile(spawnPoint, spaceShip.getDirection().get(), velocity);
		gameBoard.addObject(projectile);
	}

	// Getter
	public SpaceShip getSpaceShip() {
		return spaceShip;
	}

	public boolean isKeyPressed() {
		return !pressedKeys.isEmpty();
	}

	public Set<KeyCode> getPressedKeys() {
		return pressedKeys;
	}
}
