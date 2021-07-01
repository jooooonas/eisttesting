import GameBoard.GameBoardUI;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class AsteroidsApplication extends Application {

    private static final int GRID_LAYOUT_PADDING = 5;
    private static final int GRID_LAYOUT_PREF_HEIGHT = 350;
    private static final int GRID_LAYOUT_PREF_WIDTH = 505;

    /**
     * Starts the Bumpers Window by setting up a new tool bar, a new user interface
     * and adding them to the stage.
     *
     * @param primaryStage the primary stage for this application, onto which the
     *                     application scene can be set.
     */
    @Override
    public void start(Stage primaryStage) {
        // the tool bar object with start and stop buttons

        GameBoardUI gameBoardUI = new GameBoardUI();
        Pane gridLayout = createLayout(gameBoardUI);

        // scene and stages
        Scene scene = new Scene(gridLayout, Paint.valueOf("black"));
        gameBoardUI.setScene(scene);
        gameBoardUI.setup();

        primaryStage.setTitle("Bumpers");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(closeEvent -> gameBoardUI.stopGame());
        primaryStage.show();
        gameBoardUI.startGame();
    }

    /**
     * Creates a new {@link Pane} that arranges the game's UI elements.
     */
    private static Pane createLayout(GameBoardUI gameBoardUI) {
        // GridPanes are divided into columns and rows, like a table
        GridPane gridLayout = new GridPane();
        gridLayout.setPrefSize(GRID_LAYOUT_PREF_WIDTH, GRID_LAYOUT_PREF_HEIGHT);
        gridLayout.setVgap(GRID_LAYOUT_PADDING);
        gridLayout.setPadding(new Insets(GRID_LAYOUT_PADDING));

        // add all components to the gridLayout
        // second parameter is column index, second parameter is row index of grid
        gridLayout.add(gameBoardUI, 0, 1);
        return gridLayout;
    }


    public static void startApp(String[] args) {
        launch(args);
    }
}
