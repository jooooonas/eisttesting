package GameObjectRepository;

import GameBoard.Dimension2D;
import GameBoard.Vector2D;

public class Moon extends Obstacle {
    public static final int MOON_WIDTH = 20;
    public static final int MOON_HEIGTH = 20;
    public static final int MAX_UMDREHUNGEN = 10000;
    public static final int ABSTAND = 100; 
    
    private Planet planet;
    private final double radius;
    private double kreisPos;
    private final int maxUmfang;

    public Moon( Planet planet, int startMoon) {
        super(Vector2D.zero(), 2, 0, MOON_HEIGTH / 5);
        this.velocity = Vector2D.fromPolar(speed, 0);
        this.planet = planet;
        this.destroyed = false;
        this.size = new Dimension2D(MOON_WIDTH, MOON_HEIGTH);
        radius = planet.getSize().getHeight()/2 + ABSTAND;
        this.iconLocation = "Moon22.png";
        kreisPos = startMoon;
        Double umfang = 2 * Math.PI * radius;
        maxUmfang = umfang.intValue();
        
        //Berechnung der Startposition
        double xMittePlanet = planet.getPosition().getX() + planet.getSize().getWidth()/2;
        double yMittePlanet = planet.getPosition().getY() + planet.getSize().getHeight()/2;

        double newX = xMittePlanet + radius * Math.cos(2* Math.PI * ((kreisPos + speed) % MAX_UMDREHUNGEN) / maxUmfang);
        double newY = yMittePlanet + radius * Math.sin(2* Math.PI * ((kreisPos + speed) % MAX_UMDREHUNGEN) / maxUmfang);

        position = new Vector2D(newX, newY);
    }
    
    @Override
    public boolean crash(GameObject gameObject) {
        return false;
    }

    @Override
    public void fly(Dimension2D gameBoardSize) {
        double xMittePlanet = planet.getPosition().getX() + planet.getSize().getWidth()/2;
        double yMittePlanet = planet.getPosition().getY() + planet.getSize().getHeight()/2;
        // Es wird ein Schritt gegangen
        kreisPos = (kreisPos + speed) % MAX_UMDREHUNGEN;
        
        double newX = xMittePlanet + radius * Math.cos(2* Math.PI * kreisPos / maxUmfang);
        double newY = yMittePlanet + radius * Math.sin(2* Math.PI * kreisPos / maxUmfang);
        
        position = new Vector2D(newX, newY);
    }
    
    //Getter
    public Planet getPlanet() {
        return planet;
    }
}
