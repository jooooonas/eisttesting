
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import GameBoard.Dimension2D;
import GameBoard.Vector2D;
import GameObjectRepository.Planet;
import SpaceShip.SpaceShip;

public class TestSpaceShip {
	@Test
	public void testFly() {
		Vector2D position = new Vector2D(5, 5);
		Vector2D positionExpected = new Vector2D(5, 5);

		double direction = 0;
		Dimension2D gameBoardSize = new Dimension2D(100, 100);
		SpaceShip spaceShip = new SpaceShip(position, direction, null);

		spaceShip.fly(gameBoardSize);
		// SpaceShip should still be on the same spot since velocity = 0
		assertEquals(spaceShip.getPosition(), positionExpected);

		spaceShip.accelerate();
		Vector2D velocity = new Vector2D(spaceShip.getVelocity().getX(), spaceShip.getVelocity().getY());

		spaceShip.fly(gameBoardSize);
		Vector2D expectedPosition = new Vector2D(positionExpected.getX() + velocity.x,
				positionExpected.getY() + velocity.y);
		assertEquals(expectedPosition, spaceShip.getPosition());
	}

	@Test
	public void testCrashPlanet() {
		Vector2D position = new Vector2D(5, 5);
		double direction = 0;
		Dimension2D gameBoardSize = new Dimension2D(10, 10);
		SpaceShip spaceShip = new SpaceShip(position, direction, null);
		Planet planet = new Planet(position);
		assertTrue(spaceShip.crash(planet));
	}
}
