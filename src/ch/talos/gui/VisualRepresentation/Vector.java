package ch.talos.gui.VisualRepresentation;

import java.util.Random;

public class Vector {
    public final double x, y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector add(Vector other) {
        return new Vector(this.x + other.x, this.y + other.y);
    }

    public Vector scale(double a) {
        return new Vector(a * this.x, a * this.y);
    }

    final public static Vector ZERO = new Vector(0,0);

    @Override
    public String toString() {
        return "{" + x +
                ", " + y +
                '}';
    }

    public static Vector randomVector(Bounds bounds, Random RNG) {
        return new Vector (
                RNG.nextFloat() * (bounds.right - bounds.left) + bounds.left,
                RNG.nextFloat() * (bounds.ceiling - bounds.floor) + bounds.floor
        );
    }
}
