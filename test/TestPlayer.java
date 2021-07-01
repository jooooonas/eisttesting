
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import GameBoard.Dimension2D;
import GameBoard.GameBoard;
import GameBoard.GameBoardUI;
import GameBoard.Vector2D;
import Player.Player;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

public class TestPlayer {
	private static final int GRID_LAYOUT_PADDING = 5;
	private static final int GRID_LAYOUT_PREF_HEIGHT = 350;
	private static final int GRID_LAYOUT_PREF_WIDTH = 505;

	@Test
	public void testPressedKeys() {
		GridPane gridLayout = new GridPane();
		gridLayout.setPrefSize(GRID_LAYOUT_PREF_WIDTH, GRID_LAYOUT_PREF_HEIGHT);
		gridLayout.setVgap(GRID_LAYOUT_PADDING);
		gridLayout.setPadding(new Insets(GRID_LAYOUT_PADDING));
		gridLayout.add(new GameBoardUI(), 0, 1);
		Scene scene = new Scene(gridLayout, Paint.valueOf("black"));
		Player player = new Player(scene, new GameBoard(scene, new Dimension2D(0, 0)));

		KeyEvent keyEvent = new KeyEvent(new EventType<KeyEvent>("test"), "", "", KeyCode.W, false, false, false,
				false);

		Vector2D oldVelocity = player.getSpaceShip().getVelocity();
		player.steer();
		Vector2D newVelocity = player.getSpaceShip().getVelocity();

		assertNotEquals(oldVelocity, newVelocity);
	}
}
