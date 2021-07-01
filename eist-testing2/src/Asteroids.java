
public final class Asteroids {

    private Asteroids() {
        // Private constructor because a utility class should not be instantiable.
    }

    public static void main(String[] args) {
        // This is a workaround for a known issue when starting JavaFX applications
        AsteroidsApplication.startApp(args);
    }
}
