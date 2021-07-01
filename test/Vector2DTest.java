
import org.junit.jupiter.api.Test;

import GameBoard.Vector2D;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class Vector2DTest {

	@Test
	void add() {
		Vector2D zero = new Vector2D(0, 0);
		Vector2D v1 = new Vector2D(2, 4);
		Vector2D v2 = new Vector2D(-5, 1);

		Vector2D newV1 = v1.clone();
		newV1.add(zero);
		assertEquals(newV1, v1);

		Vector2D newV2 = v2.clone();
		newV2.add(v1);
		assertEquals(-3, newV2.x);
		assertEquals(5, newV2.y);
	}

	@Test
	void testEquals() {
		Vector2D zero = new Vector2D(0, 0);
		Vector2D v1 = new Vector2D(2, 4);
		Vector2D v2 = new Vector2D(-5, 1);
		Vector2D v3 = new Vector2D(2, 4);

		assertEquals(v1, v3);
		assertNotEquals(v1, v2);
		assertNotEquals(v3, v2);
		assertNotEquals(zero, v1);
		assertNotEquals(zero, v2);
		assertNotEquals(zero, v3);
	}

	@Test
	void zero() {
		Vector2D zero = Vector2D.zero();

		assertEquals(0, zero.x);
		assertEquals(0, zero.y);
	}
}
