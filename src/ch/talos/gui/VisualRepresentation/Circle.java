package ch.talos.gui.VisualRepresentation;

import java.awt.image.BufferedImage;
import java.util.Random;


public class Circle {
    private Vector position;
    private double radius;

    public Circle(Vector position, double radius) {
        this.position = position;
        this.radius = radius;
    }

    public void draw(BufferedImage image, double x1, double x2, double y1, double y2) {
        Vector dl = position.add(new Vector(-radius, -radius));
        Vector dr = position.add(new Vector(radius, -radius));
        Vector ul = position.add(new Vector(-radius, radius));
        Vector ur = position.add(new Vector(radius, radius));

        int sx1 = (int) (((dl.x - x1) / (x2 - x1)) * image.getWidth());
        int sx2 = (int) (((dr.x - x1) / (x2 - x1)) * image.getWidth());
        int sy1 = (int) (((dl.y - y1) / (y2 - y1)) * image.getHeight());
        int sy2 = (int) (((ul.y - y1) / (y2 - y1)) * image.getHeight());

        //System.out.println(sx1 + " " + sx2 + " " + sy1 + " " + sy2);

        for(int i = sx1; i < sx2; ++i) {
            for(int j = sy1; j < sy2; ++j) {
                //i and j are the coordinate of a specific pixel => we want to know if it's in the circle
                Vector examinedPoint = new Vector(
                        x1 + ((x2 - x1) * i) / image.getWidth(),
                        y1 + ((y2 - y1) * j) / image.getHeight()
                );
                Vector r = examinedPoint.add(position.scale(-1));
                if(Math.pow(r.x, 2) + Math.pow(r.y, 2) <= Math.pow(radius, 2)) {
                    int color = new Random().nextInt();
                    //color = 0x15000000 | color;
                    DrawGraph.safeSetRGB(image, new Point(i,j), color);
                }
            }
        }
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

}
