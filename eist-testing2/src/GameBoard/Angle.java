package GameBoard;

import java.util.Random;

public class Angle {
    // 0° equals facing south/down
    // 90° equals facing west/left

    private double angle;

    public Angle(double a) {
        angle = a;
    }

    public double get() {
        return angle;
    }

    public void set(double angle) {
        this.angle = angle;
    }

    public void add(double delta) {
        angle = renormalize(angle + delta);
    }
    public void mul(double a) {
        angle = renormalize(angle * a);
    }

    public static double renormalize(double angle) {
        return (((angle  % 360) + 360) % 360);
    }
    private static final Random rand = new Random();

    public static Angle random() {
        return new Angle(rand.nextDouble() * 360);
    }

    public Angle clone() {
        return new Angle(angle);
    }
    @Override
    public String toString() {
        return "Angle{" + angle + '}';
    }
}
