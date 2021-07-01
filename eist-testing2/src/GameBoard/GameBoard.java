package GameBoard;

import GameObjectRepository.*;
import Player.Player;
import SpaceShip.SpaceShip;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import Collision.*;

import java.security.Policy;
import java.util.*;

public class GameBoard {
	private final Dimension2D size;
	private List<GameObject> objects;
	private SpaceShip spaceShip;
	Player player;
	public static final double G = 1; // Gravitational constant
	private static final double MIN_PLANET_DISTANCE = 200;
	private boolean running;
	private static final double RANDOM_DEBRIS_SPAWN_RATE = 0.05;
	private CollisionContext collisionContext;
	private CollisionPolicy collisionPolicy;

	public GameBoard(Scene scene, Dimension2D size) {
		this.size = size;
		objects = new ArrayList<>();
		player = new Player(scene, this);
		createDebris(10);
		createPlanets(5);
	}

	public GameBoard(Dimension2D size) {
		this.size = size;
		objects = new ArrayList<>();
		player = new Player(this);
		createDebris(10);
		createPlanets(5);
	}

	public void addObject(GameObject obj) {
		objects.add(obj);
	}

	public void tick() {
		player.steer();
		updateObjectsPositions();
		detectCollisions();
		applyGravity();

		if (Math.random() > 1.0 - RANDOM_DEBRIS_SPAWN_RATE) {
			spawnRandomDebrisOffScreen();
		}

		if (player.isKeyPressed()) {
			handlePressedKeys(player.getPressedKeys());
		}

	}

	private void createDebris(int number) {
		for (int i = 0; i < number; i++) {
			objects.add(new Debris(randomPoint(), 5, randomDirection()));
		}
	}

	private void createPlanets(int number) {
		List<Obstacle> planets = new ArrayList<>();
		for (int i = 0; i < number; i++) {
			Vector2D newPos = null;
			boolean collidesWithOtherPlanet = true;
			int tries = 0;
			while (collidesWithOtherPlanet && tries < 100) {
				newPos = randomPoint();
				collidesWithOtherPlanet = false;
				for (Obstacle planet : planets) {
					if (newPos.distanceTo(planet.getPosition()) < MIN_PLANET_DISTANCE) {
						collidesWithOtherPlanet = true;
						break;
					}
				}
				tries++;
			}
			Planet neuerPlanet = new Planet(newPos);
			Random rand = new Random();
			List<Moon> monde = createMoon(neuerPlanet, rand.nextInt(3));
			for (int j = 0; j < monde.size(); j++) {
				planets.add(monde.get(j));
			}
			planets.add(neuerPlanet);
		}

		objects.addAll(planets);
	}

	private List<Moon> createMoon(Planet planet, int number) {
		List<Moon> monde = new ArrayList<>();
		for (int i = 0; i < number; i++) {
			monde.add(new Moon(planet, i * 500));
		}
		return monde;
	}

	private void handlePressedKeys(Set<KeyCode> keyCodes) {
		// Todo: handle all keys that refer to t
	}

	private void spawnRandomDebrisOffScreen() {
		Random rand = new Random();
		int side = rand.nextInt(4);
		double spawnX, spawnY;
		switch (side) {
		case 0 -> {
			// up
			spawnX = rand.nextDouble() * this.size.getWidth();
			spawnY = -100;
		}
		case 1 -> {
			// right
			spawnX = this.size.getWidth() + 100;
			spawnY = rand.nextDouble() * this.size.getHeight();
		}
		case 2 -> {
			// down
			spawnX = rand.nextDouble() * this.size.getWidth();
			spawnY = this.size.getHeight() + 100;
		}
		case 3 -> {
			// left
			spawnX = -100;
			spawnY = rand.nextDouble() * this.size.getHeight();
		}
		default -> throw new IllegalStateException("Unexpected value: " + side);
		}

		Vector2D newPos = new Vector2D(spawnX, spawnY);
		Vector2D newVelocity = randomPoint();
		newVelocity.subtract(newPos);
		newVelocity = newVelocity.getNormalized();
		newVelocity.multiply(rand.nextDouble() * Debris.DEBRIS_MAX_VELOCITY_MAG);

		addObject(new Debris(newPos, newVelocity, 5, Angle.random().get()));
	}

	public Vector2D getCenter() {
		return new Vector2D(this.getSize().getWidth() / 2, this.getSize().getHeight() / 2);
	}

	private void updateObjectsPositions() {
		List<GameObject> objectsToAdd = new ArrayList<>();
		for (Iterator<GameObject> it = objects.iterator(); it.hasNext();) {
			GameObject object = it.next();
			// Falls das Objekt zerstört worden ist, wird es gelöscht
			if (object.isDestroyed()) {
				it.remove();
			} else {
				if (object instanceof Debris) {
					Debris debris = (Debris) object;

					// create smaller debris pieces
					if (debris.isPulverized()) {
						Vector2D center = debris.getCenteredPosition();
						for (int i = 0; i < 3; i++) {
							Vector2D newPos = debris.getPosition().clone();
							newPos.add(Vector2D.random(-5, -5, 5, 5));
							Vector2D newVelocity = newPos.clone();
							newVelocity.subtract(center);
							objectsToAdd.add(new Debris(newPos, newVelocity, debris.getSpeed(),
									debris.getDirection().get(), true));
						}
						debris.setPulverized(false);
					}
				}
				object.fly(size);
			}
		}
		objects.addAll(objectsToAdd);
	}

	private void detectCollisions() {
		for (int i = 0; i < objects.size(); i++) {
			GameObject object1 = objects.get(i);
			for (int j = i + 1; j < objects.size(); j++) {
				GameObject object2 = objects.get(j);
				collisionContext = new CollisionContext(object1, object2);
				collisionPolicy = new CollisionPolicy(collisionContext);
				collisionPolicy.configure(object1, object2);

				if (collisionContext.getCollisionType().isCrash()) {
					object1.crash(object2);
					object2.crash(object1);
					// TODO fly schritt eingebaut um die hin und her gewackle zu vermeiden
					object1.fly(size);
					object2.fly(size);
				}
			}
		}
	}

	// For each planet, apply gravity to player and moons
	private void applyGravity() {
		for (GameObject planet : objects) {
			if (!(planet instanceof Planet))
				continue;

			Vector2D gravityAcceleration = getPlanetGravityAcceleration((Planet) planet, player.getSpaceShip());
			player.getSpaceShip().accelerate(gravityAcceleration);
		}
	}

	// see Newton’s law of universal gravitation
	// (http://djbinder.com/articles/NBody/)
	private Vector2D getPlanetGravityAcceleration(Planet planet, GameObject object) {
		Vector2D planetPos = planet.getPosition();
		Vector2D objectPos = object.getPosition();
		Vector2D delta = Vector2D.subtract(planetPos, objectPos);
		double distance = delta.getMagnitude();
		double force = G * planet.getMass() / Math.pow(distance, 2);

		return delta.getNormalized().getMultiplied(force);
	}

	public void startGame() {
		this.running = true;
	}

	public void endGame() {
		this.running = false;
	}

	// Getter
	public Dimension2D getSize() {
		return size;
	}

	public boolean isRunning() {
		return running;
	}

	// jetzt GameObjectRepository
	public List<GameObject> getObjects() {
		return objects;
	}

	public Vector2D randomPoint() {
		return Vector2D.random(size.getWidth(), size.getHeight());
	}

	public double randomDirection() {
		return new Random().nextDouble() * 360;
	}

	public SpaceShip getSpaceShip() {
		return spaceShip;
	}

	public boolean setSpaceShip(SpaceShip spaceShip) {
		this.spaceShip = spaceShip;
		// assure that spaceship is not set into another Object
		for (int j = 0; j < objects.size(); j++) {
			GameObject object2 = objects.get(j);
			Collision collision = new Collision(spaceShip, object2);
			if (collision.isCrash()) {
				return false;
			}
		}
		addObject(spaceShip);
		return true;
	}
}
