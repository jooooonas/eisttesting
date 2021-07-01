package GameBoard;

import GameObjectRepository.Debris;
import GameObjectRepository.GameObject;
import SpaceShip.SpaceShip;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class GameBoardUI extends Canvas {
	private static final int UPDATE_PERIOD = 1000 / 25;
	private static final int DEFAULT_WIDTH = 1280;
	private static final int DEFAULT_HEIGHT = 720;
	private static final Dimension2D DEFAULT_SIZE = new Dimension2D(DEFAULT_WIDTH, DEFAULT_HEIGHT);

	private Scene scene;
	private Timer gameTimer;

	private HashMap<String, Image> imageCache;
	private GameBoard gameBoard;

	private GameToolBar toolBar;

	public GameBoardUI() {
		this.toolBar = new GameToolBar();
	}

	public void setup() {
		setupGameBoard();
		setupImageCache();
		paint();
	}

	public void startGame() {
		if (!this.gameBoard.isRunning()) {
			this.gameBoard.startGame();
			startTimer();
			paint();
		}
	}

	private void startTimer() {
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				updateGame();
			}
		};
		if (this.gameTimer != null) {
			this.gameTimer.cancel();
		}
		this.gameTimer = new Timer();
		this.gameTimer.scheduleAtFixedRate(timerTask, UPDATE_PERIOD, UPDATE_PERIOD);
	}

	private void updateGame() {
		if (gameBoard.isRunning()) {
			// updates car positions and re-renders graphics
			this.gameBoard.tick();
			// when this.gameBoard.getOutcome() is OPEN, do nothing

			paint();
		}
	}

	private void setupGameBoard() {
		Dimension2D size = DEFAULT_SIZE;
		this.gameBoard = new GameBoard(scene, size);
		widthProperty().set(size.getWidth());
		heightProperty().set(size.getHeight());
	}

	private Image getImage(String carImageFilePath) {
		URL imageUrl = getClass().getClassLoader().getResource(carImageFilePath);
		if (imageUrl == null) {
			throw new IllegalArgumentException(
					"Please ensure that your resources folder contains the appropriate files for this exercise.");
		}
		return new Image(imageUrl.toExternalForm());
	}

	private void setupImageCache() {
		this.imageCache = new HashMap<>();

		File dir = new File("./resources");
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				this.imageCache.computeIfAbsent(child.getName(), this::getImage);
			}
		} else {
			// Handle the case where dir is not really a directory.
			// Checking dir.isDirectory() above would not be sufficient
			// to avoid race conditions with another process that deletes
			// directories.
		}
//      Todo:  e.g. this.imageCache.computeIfAbsent(car.getBackIconLocation(), this::getImage);
	}

	private void paint() {
		getGraphicsContext2D().setFill(Paint.valueOf("black"));
		getGraphicsContext2D().fillRect(0, 0, getWidth(), getHeight());

		// Todo: paint background
		for (GameObject gameObject : this.gameBoard.getObjects()) {
			paintObject(gameObject);
		}

	}

	private void paintObject(GameObject gameObject) {

		// Todo
		String iconPath = gameObject.getIconLocation();
		Image img = this.imageCache.get(iconPath);
		Vector2D pos = gameObject.getPosition();
		Dimension2D size = gameObject.getSize();
		Angle direction = gameObject.getDirection();
		GraphicsContext context = getGraphicsContext2D();

		ImageView imgView = new ImageView(img);
		imgView.setRotate(direction.get());
		SnapshotParameters params = new SnapshotParameters();
		params.setFill(Paint.valueOf("black"));

		drawRotatedImage(context, imgView.getImage(), direction.get(), pos.getX(), pos.getY(), size.getWidth(),
				size.getHeight());
	}

	public static void drawRotatedImage(GraphicsContext gc, Image image, double angle, double tlpx, double tlpy,
			double width, double height) {
		gc.save();
		rotate(gc, angle, tlpx + width / 2, tlpy + height / 2);
		gc.drawImage(image, tlpx, tlpy, width, height);
		gc.restore();
	}

	private static void rotate(GraphicsContext gc, double angle, double px, double py) {
		Rotate r = new Rotate(angle, px, py);
		gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
	}

	private void drawTriangle(double x, double y, int direction) {
		Polygon p = getTrianglePolygon();
		p.getTransforms().add(new Rotate(direction));
		p.getTransforms().add(new Translate(x, y));
		double[] xPoints = Arrays.stream(p.getPoints().toArray(new Double[0])).mapToDouble(Double::doubleValue)
				.toArray();
		double[] yPoints = Arrays.stream(p.getPoints().toArray(new Double[1])).mapToDouble(Double::doubleValue)
				.toArray();
		getGraphicsContext2D().strokePolygon(xPoints, yPoints, p.getPoints().size());
	}

	private Polygon getTrianglePolygon() {
		Polygon polygon = new Polygon();
		polygon.getPoints().addAll(0.0, 0.0, 20.0, 10.0, 10.0, 20.0);
		return polygon;
	}

	public void stopGame() {
		gameBoard.endGame();
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

	// restart Game when player clicks on restart button
	public void restartGame() {
		if (toolBar.reset()) {
			stopGame();
			setup();
			startGame();
		}
	}

	public GameBoard getGameBoard() {
		return gameBoard;
	}
}
