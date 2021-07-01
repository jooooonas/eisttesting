package GameBoard;

import java.util.Random;

public class Vector2D {

	public double x;
	public double y;

	public Vector2D() {
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public void set(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getMagnitude() {
		return Math.sqrt(x * x + y * y);
	}

	public double distanceSq(double vx, double vy) {
		vx -= x;
		vy -= y;
		return (vx * vx + vy * vy);
	}

	public double distanceTo(Vector2D v) {
		double vx = v.x - this.x;
		double vy = v.y - this.y;
		return Math.sqrt(vx * vx + vy * vy);
	}

	public double getAngle() {
		return Math.atan2(y, x);
	}

	public Vector2D getNormalized() {
		double magnitude = getMagnitude();
		if (magnitude == 0) {
			return fromPolar(1.0, getAngle());
		}
		return new Vector2D(x / magnitude, y / magnitude);
	}

	public void add(Vector2D v) {
		this.x += v.x;
		this.y += v.y;
	}

	public void subtract(Vector2D v) {
		this.x -= v.x;
		this.y -= v.y;
	}

	public static Vector2D subtract(Vector2D v1, Vector2D v2) {
		Vector2D newVector = v1.clone();
		newVector.subtract(v2);
		return newVector;
	}

	public void multiply(double scalar) {
		x *= scalar;
		y *= scalar;
	}

	public Vector2D getMultiplied(double scalar) {
		Vector2D newVector = clone();
		newVector.multiply(scalar);
		return newVector;
	}

	public void divide(double scalar) {
		x /= scalar;
		y /= scalar;
	}

	public double dotProduct(Vector2D v) {
		return (this.x * v.x + this.y * v.y);
	}

	public double crossProduct(Vector2D v) {
		return (this.x * v.y - this.y * v.x);
	}

	public void rotateBy(double angle) {
		// Wikipedia only shows a rotation counter-clockwise and I am too stupid to
		// change it to clockwise. Therefore I changed the input angle
		angle = (360 - angle);
		double oldX = x;
		x = Math.cos(angle) * oldX + Math.sin(Math.toRadians(angle)) * (-1) * y;
		y = Math.sin(Math.toRadians(angle)) * oldX + Math.cos(Math.toRadians(angle)) * y;
	}

	public static Vector2D fromPolar(double mag, double angle) {
		angle = Angle.renormalize(-angle + 90);
		return new Vector2D(mag * Math.cos(Math.toRadians(angle)), -mag * Math.sin(Math.toRadians(angle)));
	}

	public void reverse() {
		x = -x;
		y = -y;
	}

	public void addMagnitude(double amount) {
		Vector2D addedVelocity = getNormalized();
		addedVelocity.multiply(amount);
		add(addedVelocity);
	}

	@Override
	public Vector2D clone() {
		return new Vector2D(x, y);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof Vector2D) {
			Vector2D v = (Vector2D) obj;
			return (x == v.x) && (y == v.y);
		}
		return false;
	}

	@Override
	public String toString() {
		return "Vector2D(" + x + ", " + y + ")";
	}

	public static final Vector2D zero() {
		return new Vector2D(0, 0);
	}

	private static final Random random = new Random();

	public static Vector2D random(double maxX, double maxY) {
		return new Vector2D(random.nextDouble() * maxX, random.nextDouble() * maxY);
	}

	public static Vector2D random(double minX, double minY, double maxX, double maxY) {
		return new Vector2D(minX + random.nextDouble() * (maxX - minX), minY + random.nextDouble() * (maxY - minY));
	}

	public static Vector2D randomWithMag(double minMag, double maxMag) {
		return fromPolar(minMag + random.nextDouble() * (maxMag - minMag), Angle.random().get());
	}
}
