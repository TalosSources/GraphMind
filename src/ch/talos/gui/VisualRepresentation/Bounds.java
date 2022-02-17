package ch.talos.gui.VisualRepresentation;

public class Bounds {
    public final double floor;
    public final double ceiling;
    public final double left;
    public final double right;

    public Bounds(double ground, double ceiling, double left, double right) {
        this.floor = ground;
        this.ceiling = ceiling;
        this.left = left;
        this.right = right;
    }
}
