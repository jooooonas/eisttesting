
import Collision.CollisionContext;
import Collision.CollisionPolicy;
import GameBoard.Dimension2D;
import GameBoard.Vector2D;
import GameObjectRepository.Debris;
import GameObjectRepository.Planet;
import GameObjectRepository.Projectile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestGameObjects {
	@Test
	void testProjectileMovement() {
		double defaultSpeed = 10;
		Vector2D position = new Vector2D(5, 5);
		double direction = 90;
		Dimension2D gameboardSize = new Dimension2D(100, 100);
		Projectile testProjectile = new Projectile(position, direction);
		Vector2D positionAfter = position.clone();
		positionAfter.add(Vector2D.fromPolar(defaultSpeed, direction));
		testProjectile.fly(gameboardSize);
		assertEquals(positionAfter, testProjectile.getPosition());
	}

	@Test
	void testProjectileCrash() {
		Vector2D position = new Vector2D(5, 5);
		double direction = 90;
		Projectile testProjectile = new Projectile(position, direction);
		assertTrue(testProjectile.crash(new Planet(new Vector2D(5, 5))));
	}

	@Test
	void testDebrisCrashDebris() {
		Vector2D position = new Vector2D(10, 10);
		Debris testDebris = new Debris(position, 5, 90);
		assertFalse(testDebris.crash(new Debris(position, 5, 180)));
	}

	@Test
	void testSmallDebrisCrashPlanet() {
		Vector2D position = new Vector2D(10, 10);
		Debris testDebris = new Debris(position, new Vector2D(1, 2), 5, 90, true);
		assertTrue(testDebris.crash(new Planet(new Vector2D(5, 5))));
	}

}
