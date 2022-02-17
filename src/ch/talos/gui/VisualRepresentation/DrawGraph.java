package ch.talos.gui.VisualRepresentation;

import ch.talos.model.ModifiableNode;
import ch.talos.model.MutableGraphState;
import ch.talos.model.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class DrawGraph {
    /*
    This is looking hella floppa good ! The next step :
    Add some kind of force, that for a given distance d, attracts 2 nodes if they're further appart than d, and
    repels then if they're closer. The force is scaled by the difference. Let's hope that it will naturally attract
    graphs to be in a nice state.
    The first step to do that logically would be to merge the vertex and physic object representations ig.

    OK now the next thing to do : a distance more than which the links cannot stretch. Use Sebastian Lague algorithm.
     */
    public static int RADIUS = 7;
    public static double DISTANCE = 100;
    public static double k = 1;
    public static double g = 0;

    public static double beginIter = 100;
    public static double iter = 0;

    public static int LINE_COLOR = 0xff4f4f4f;
    public static int NODE_COLOR = 0xffff00ff;
    public static int BACKGROUND_COLOR = 0xFF000007;

    final static public Random RNG = new Random();

    public static void performDrawing(MutableGraphState thoughtGraph, ModifiableNode startingNode) {
        BufferedImage image = new BufferedImage(1500, 1000, BufferedImage.TYPE_INT_ARGB);

        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new FlowLayout());
        JLabel label = new JLabel(new ImageIcon(image));
        frame.getContentPane().add(label);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        label.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));

        /* GRAPH STUFF */
        int moiIndex = 0;
        for(Node node : thoughtGraph.getGraph()) {
            if (node.equals(startingNode)) break;
            ++moiIndex;
        }
        Graph graph = graphFromGraph(thoughtGraph);
        int n = thoughtGraph.getGraph().size();
        Bounds bounds = new Bounds(RADIUS, image.getHeight() - RADIUS, RADIUS, image.getWidth() - RADIUS);
        PhysicGraph pg = new PhysicGraph(graph, n, bounds);
//        int[] colors = new int[n];
//        for(int i = 0; i < n; ++i) colors[i] = NODE_COLOR;
        int[] levels = computeLevels(graph, moiIndex);
        int[] levelColors = {/*0xffff00ff, */0xffff007f, 0xffff0000, /*0xffff7f00,*/ 0xffffff00,
                0xff7fff00, 0xff00ff00, 0xff00ff7f, 0xff00ffff, 0xff007fff, 0xff000000, 0xff000000, 0xff000000};
        int[] colors = new int[levels.length];
        for(int i = 0; i < colors.length; ++i) {
            if(levels[i] == -1) colors[i] = 0x0;
            else colors[i] = levelColors[levels[i]];
        }
        removeDistantVertices(graph, levels, 2);

        /* -------------------- */


        long t2, t1 = System.nanoTime();
        while(true) {

            //time stuff
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            t2 = System.nanoTime();
            double deltaTime = (t2 - t1) * 1e-9;
            t1 = System.nanoTime();
            ++iter;

            //resetting the image
            for(int i = 0; i < image.getWidth(); ++i) { //clear image
                for(int j = 0; j < image.getHeight(); ++j) {
                    image.setRGB(i,j,BACKGROUND_COLOR);
                }
            }


            List<Point> positions = new ArrayList<>();
            int i = 0;
            //looping over all the nodes
            for(PhysicObject object : pg.objects) {
                if(graph.graph.get(i) == null) {
                    positions.add(new Point(0,0));
                    ++i;
                    continue;
                }
                //Computing the forces (spring and gravity)
                Vector force = Vector.ZERO;
                for(int v : graph.graph.get(i)) {
                    PhysicObject other = pg.objects[v];
                    Vector deltaPos = other.getPosition().add(object.getPosition().scale(-1));
                    double norm = Math.sqrt(Math.pow(deltaPos.x, 2) + Math.pow(deltaPos.y, 2));
                    if(norm < DISTANCE) continue;
                    Vector normalized = deltaPos.scale(1/norm);
                    double factor = (iter < beginIter ? iter/100 : k) * Math.pow(norm - DISTANCE, 1);
                    Vector toAdd = normalized.scale(factor);
                    force = force.add(toAdd);
                }
                for(int j = 0; j < graph.graph.size(); ++j) {
                    //that should loop over all the other nodes
                    if(i == j) continue; //same node
                    PhysicObject other = pg.objects[j];
                    Vector deltaPos = other.getPosition().add(object.getPosition().scale(-1));
                    double norm = Math.sqrt(Math.pow(deltaPos.x, 2) + Math.pow(deltaPos.y, 2));
                    if(norm > DISTANCE) continue;
                    Vector normalized = deltaPos.scale(1/norm);
                    double factor = (iter < beginIter ? iter/100 : k) * Math.pow(norm - DISTANCE, 1);
                    Vector toAdd = normalized.scale(factor);
                    force = force.add(toAdd);
                }
                Vector gravityForce = new Vector(0, g);
                force = force.add(gravityForce);

                if(i == moiIndex) {
                    java.awt.Point mousePos = label.getMousePosition();
                    if(mousePos != null)
                        object.setPosition(new Vector(mousePos.x, mousePos.y));
                }
                else{
                    object.setPosition(object.getPosition().add(force.scale(deltaTime)));
                }

                object.updatePosition(deltaTime);
                object.checkPhysics();
                positions.add(new Point((int)object.getPosition().x, (int)object.getPosition().y));

                ++i;
            }

            drawGraph(positions, graph, image, colors);

            image.flush();
            label.setIcon(new ImageIcon(image));
        }
    }

    private static void removeDistantVertices(Graph graph, int[] levels, int bound) {
        for(List<Integer> l : graph.graph) {
            List<Integer> toRemove = new LinkedList<>();
            for (int i : l) if (levels[i] > bound) toRemove.add(i);
            l.removeAll(toRemove);
        }

        List<Integer> toRemove = new LinkedList<>();
        for(int i = 0; i < graph.graph.size(); ++i)
            if(levels[i] > bound) toRemove.add(i);

        for(int i : toRemove) graph.graph.set(i, null);

        int nonNull = 0;
        for(List<Integer> l : graph.graph) if(l != null) ++nonNull;
        System.out.println("We kept : " + nonNull);
    }

    public static void drawGraph(List<Point> positions, Graph graph, BufferedImage image, int[] colors) {
        int i = 0;
        for(List<Integer> vertices : graph.graph) {
            if(vertices != null)
                for(int vertex : vertices)
                    drawLine(positions.get(i), positions.get(vertex), image);
            ++i;
        }
        i = 0;
        for(List<Integer> vertex : graph.graph) {
            if(i >= positions.size()) break;
            if(vertex != null) {
                drawCircle(positions.get(i), image, colors[i]);
            }
            i += 1;
        }
    }

    static void drawCircle(Point position, BufferedImage image, int color) {
        for(int x = position.x - RADIUS; x < position.x + RADIUS; ++x) {
            for(int y = position.y - RADIUS; y < position.y + RADIUS; ++y) {
                if(Math.pow(x-position.x, 2) + Math.pow(y-position.y,2) < RADIUS * RADIUS)
                    safeSetRGB(image, new Point(x, y), color);
            }
        }
    }

    static void drawLine(Point p1, Point p2, BufferedImage image) {
        double x1 = p1.x;
        double x2 = p2.x;
        double y1 = p1.y;
        double y2 = p2.y;
        double x = x1;
        double y = y1;

        double norm = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        //System.out.println("norm : " + norm);
        double dx = (x2 - x1) / norm;
        double dy = (y2 - y1) / norm;
        if(dx == 0 && dy == 0) return;

        int color = LINE_COLOR;

        while(compare(x, x1, x2) && compare(y, y1, y2)) {
            safeSetRGB(image, new Point((int)x, (int)y), color);
            x += dx;
            y += dy;
        }
    }

    static boolean compare(double x, double x1, double x2) {
        if(x1 <= x2)
            return x <= x2;
        else
            return x >= x2;
    }

    static int randomColor() {
        int r = DrawGraph.RNG.nextInt(0xFF);
        int g = DrawGraph.RNG.nextInt(0xFF);
        int b = DrawGraph.RNG.nextInt(0xFF);

        return (r << 16) | (g << 8) | b;
    }

    static void safeSetRGB(BufferedImage image, Point p, int RGB){
        if(p.x < 0 || p.y < 0 || p.x >= image.getWidth() || p.y >= image.getHeight()) return;
        image.setRGB(p.x, p.y, RGB);
    }

    static Graph graphFromGraph(MutableGraphState graph) {
        Graph newGraph = new Graph();
        Map<Node, Integer> map = new TreeMap<>();
        int i = 0;
        for(Node node : graph.getGraph()) {
            newGraph.addVertex(i);
            map.put(node, i);
            ++i;
        }
        for(Node u : graph.getGraph()) {
            for(Node v : u.parents()) newGraph.addDirectedEdge(map.get(u), map.get(v));
            for(Node v : u.children()) newGraph.addDirectedEdge(map.get(u), map.get(v));
            for(Node v : u.siblings()) newGraph.addDirectedEdge(map.get(u), map.get(v));
        }
        return newGraph;
    }

    static Graph treeFromGraph(MutableGraphState graph) {
        Graph newGraph = new Graph();
        Map<Node, Integer> map = new TreeMap<>();
        int i = 0;
        for(Node node : graph.getGraph()) {
            newGraph.addVertex(i);
            map.put(node, i);
            ++i;
        }
        for(Node u : graph.getGraph()) {
            for(Node v : u.children()) newGraph.addDirectedEdge(map.get(u), map.get(v));
        }
        return newGraph;
    }

    static int[] computeLevels(Graph graph, int source) {
        Queue<Integer> queue = new LinkedList<>();
        int[] levels = new int[graph.graph.size()];
        for(int i = 0; i < levels.length; ++i) levels[i] = -1;
        queue.add(source);
        levels[source] = 0;
        while(!queue.isEmpty()) {
            int u = queue.remove();
            for(int v : graph.graph.get(u)) {
                if(levels[v] == -1) {
                    levels[v] = levels[u] + 1;
                    queue.add(v);
                }
            }
        }
        for(int i : levels) System.out.print(i + ", ");
        int count = 0;
        for(int i : levels) if(i == -1) ++count;
        System.out.println();
        System.out.println("count : " + count);
        return levels;
    }
}

class Graph {
    List<List<Integer>> graph;

    public Graph() {
        graph = new ArrayList<>();
    }

    public void addVertex(int v) {

        if(graph.size() <= v || graph.get(v) == null) {
            for(int i = graph.size()-1; i <= v; ++i)
                graph.add(new LinkedList<>());
            graph.set(v, new LinkedList<>());
        }
    }

    public void addDirectedEdge(int v1, int v2) {
        graph.get(v1).add(v2);
    }

    public void addEdge(int v1, int v2) {
        addDirectedEdge(v1, v2);
        addDirectedEdge(v2, v1);
    }
}

class Point {
    int x;
    int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    static Point randomPoint(int w, int h) {
        return new Point(
                DrawGraph.RNG.nextInt(
                        w-2*DrawGraph.RADIUS)+DrawGraph.RADIUS,
                DrawGraph.RNG.nextInt(
                        h-2*DrawGraph.RADIUS)+DrawGraph.RADIUS);
    }
}

class PhysicGraph {
    /*GRAPH STUFF*/
    public final int n;
    Graph graph;
    PhysicObject[] objects;

    public PhysicGraph(Graph graph, int n, Bounds bounds) {
        this.n = n;
        this.graph = graph;
        this.objects = new PhysicObject[n];

        for(int i = 0; i < n; ++i) {
            PhysicObject newObject = new PhysicObject(null, bounds);
            objects[i] = newObject;
            newObject.setPosition(Vector.randomVector(bounds, DrawGraph.RNG));
        }
    }
}
