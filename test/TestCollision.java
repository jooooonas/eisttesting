
import Collision.CollisionContext;
import Collision.CollisionPolicy;
import GameBoard.Vector2D;
import GameObjectRepository.Debris;
import GameObjectRepository.Projectile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestCollision {
	@Test
	void succesfulCollisionTest() {
		Vector2D position = new Vector2D(5, 5);
		double direction = 90;
		Projectile testProjectile = new Projectile(position, direction);
		Debris testDebris = new Debris(new Vector2D(6, 6), 4, 4);
		CollisionContext collisionContext = new CollisionContext(testProjectile, testDebris);
		CollisionPolicy collisionPolicy = new CollisionPolicy(collisionContext);
		collisionPolicy.configure(testProjectile, testDebris);
		assertTrue(collisionContext.getCollisionType().isCrash());
	}

	@Test
	void failedCollisionTest() {
		Vector2D position = new Vector2D(5, 5);
		double direction = 90;
		Projectile testProjectile = new Projectile(position, direction);
		Debris testDebris = new Debris(new Vector2D(100, 100), 4, 4);
		CollisionContext collisionContext = new CollisionContext(testProjectile, testDebris);
		CollisionPolicy collisionPolicy = new CollisionPolicy(collisionContext);
		collisionPolicy.configure(testProjectile, testDebris);
		assertFalse(collisionContext.getCollisionType().isCrash());
	}
}
