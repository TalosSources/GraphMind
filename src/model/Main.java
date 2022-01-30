package model;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        BFSTest();
    }

    public static void BFSTest() {
        Set<NodeBFS> graph = buildBFSGraph(simpleGraph());

        NodeBFS source = null;
        for(NodeBFS node : graph) {
            if(node.node.name().equals("EPFL")) {
                source = node;
                break;
            }
        }
        if(source == null) throw new IllegalStateException("Source not found :(");

        breadthFirstSearch(graph, source);

        for(NodeBFS nodeBFS : graph) {
            Node node = nodeBFS.node;
            System.out.println("Distance between " + source.node.name() +
                    " and " + node.name() + " : " + (
                    nodeBFS.distance == Integer.MAX_VALUE ? "NOT REACHABLE" :
                            nodeBFS.distance +
                                    " with path " + (Objects.isNull(nodeBFS.parent) ? "itself" :
                                    printCollection(pathToVertex(graph, nodeBFS))))
            ); }
    }

    public static void simpleGraphTest() {
        Set<Node> graph = simpleGraph();

        for(Node node : graph) System.out.print(node.textRepresentation());
    }

    public static Set<Node> simpleGraph() {
        Set<Node> graph = new TreeSet<>();

        ModifiableNode Moi = new SimpleNode("Moi");
        ModifiableNode ComputerScience = new SimpleNode("CompSci");
        ModifiableNode EPFL = new SimpleNode("EPFL");
        ModifiableNode MaterialScience = new SimpleNode("MaterialScience");
        ModifiableNode BA3 = new SimpleNode("BA3");

        String epflText =
                "L'ecole polytechnique federale de Lausanne, ou je suis inscrit. Plutot pas mal dans l'ensemble.";
        String MatSciText =
                "La science qui etudie les materiaux, a priori aucun rapport avec moi.";

        EPFL.setText(epflText);
        MaterialScience.setText(MatSciText);

        ComputerScience.setName("ComputerScience");

        EPFL.setUrl("www.epfl.ch");

        connectParentChild(Moi, EPFL);
        connectParentChild(Moi, ComputerScience);
        connectParentChild(EPFL, BA3);
        connectSiblings(ComputerScience, EPFL);
        connectSiblings(MaterialScience, EPFL);

        return Set.of(Moi, ComputerScience, EPFL, MaterialScience, BA3);
    }

    private static void breadthFirstSearch(Set<NodeBFS> borderGraph, NodeBFS source) {
        for(NodeBFS c : borderGraph) {
            c.parent = null;
            c.distance = -1;
        }
        source.distance = 0;

        Queue<NodeBFS> queue = new LinkedList<>();
        queue.add(source);

        while(!queue.isEmpty()) {
            NodeBFS u = queue.remove();
            for(NodeBFS v : u.adjacents) {
                if(v.distance == -1) {
                    queue.add(v);
                    v.distance = u.distance + 1;
                    v.parent = u;
                }
            }
        }
    }

    public static void connectParentChild(ModifiableNode parent, ModifiableNode child){
        parent.addChild(child);
        child.addParent(parent);
    }

    public static void connectSiblings(ModifiableNode s1, ModifiableNode s2) {
        s1.addSibling(s2);
        s2.addSibling(s1);
    }

    private static Set<NodeBFS> buildBFSGraph(Set<Node> graph) {
        Map<Node, NodeBFS> map = new TreeMap<>();
        for(Node node : graph) {
            NodeBFS nodeBFS = new NodeBFS(node);
            map.put(node, nodeBFS);
        }
        for(NodeBFS nodeBFS : map.values()) {
            for(Node sibling : nodeBFS.node.siblings()) nodeBFS.adjacents.add(map.get(sibling));
            for(Node parent : nodeBFS.node.parents()) nodeBFS.adjacents.add(map.get(parent));
            for(Node child : nodeBFS.node.children()) nodeBFS.adjacents.add(map.get(child));
        }

        return new TreeSet<>(map.values());
    }

    private static List<Node> pathToVertex(Set<NodeBFS> graph, NodeBFS source) {
        List<Node> path = new LinkedList<>();
        while(source.distance != 0) {
            path.add(0, source.node);
            source = source.parent;
        }
        path.add(0, source.node);
        return path;
    }

    private static <T> String printCollection(Collection<T> collection) {
        StringBuilder sb = new StringBuilder().append('[');
        for(T elem : collection) {
            sb.append(elem.toString()).append(", ");
        }
        if(!collection.isEmpty()) sb.delete(sb.length() - 2, sb.length());

        return sb.append(']').toString();
    }
}

class NodeBFS implements Comparable<NodeBFS>{
    Node node;
    NodeBFS parent;
    int distance;
    List<NodeBFS> adjacents;

    public NodeBFS(Node node) {
        this.node = node;
        this.adjacents = new LinkedList<>();
    }

    @Override
    public int compareTo(NodeBFS o) {
        return this.node.compareTo(o.node);
    }
}
