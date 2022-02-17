package ch.talos.gui.VisualRepresentation;

import java.awt.image.BufferedImage;

public class PhysicObject { // make an interface, with those methods, and a draw : make some sort of structure
    public final static double collisionDrag = 1;

    private Vector position = Vector.ZERO;
    private Vector velocity = Vector.ZERO;
    private Circle circle;
    private int modifiedCounter = 0;
    private Bounds bounds;

    public PhysicObject(Circle circle, Bounds bounds) {
        this.circle = circle;
        this.bounds = bounds;
    }

    public Vector getPosition() {
        return position;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public void setPosition(Vector newPos) {
        this.position = newPos;
    }

    public void addVelocity(Vector deltaVel) {
        velocity = velocity.add(deltaVel);
    }

    public void updatePosition(double deltaTime) {
        position = position.add(velocity.scale(deltaTime));
        if(circle != null)
            circle.setPosition(position);
    }

    public void checkPhysics() {
        if(modifiedCounter == 0) {
            boolean modif = false;
            if(position.y <= bounds.floor || position.y >= bounds.ceiling) {
                velocity = new Vector(velocity.x, -velocity.y).scale(collisionDrag);
                modif = true;
            }
            if(position.x <= bounds.left || position.x >= bounds.right) {
                velocity = new Vector(-velocity.x, velocity.y).scale(collisionDrag);
                modif = true;
            }
            if(modif) modifiedCounter = 10;
        } else {
            modifiedCounter--;
        }

        /**
         * Note for later when I'll have the energy to implement that better :
         * The bounce vector v' of an incidence vector v on a line d is :
         * v - 2 * proj[d](v)
         */

    }

    public void draw(BufferedImage image, double x1, double x2, double y1, double y2) {
        circle.draw(image, x1, x2, y1, y2);
    }
}
