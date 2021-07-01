
import org.easymock.EasyMockExtension;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import GameBoard.GameBoardUI;
import GameBoard.GameToolBar;
import GameBoard.Vector2D;
import GameObjectRepository.GameObject;

@ExtendWith(EasyMockExtension.class)
public class MockTest {
	@TestSubject
	private Vector2D vector = new Vector2D(0, 0);

	@Mock
	private Vector2D vectorMock;

	@Test
	void testVector() {
		expect(vectorMock.distanceTo(vector)).andReturn(2.5);
		replay(vectorMock);
	}
}
