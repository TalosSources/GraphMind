package model.Analytics;

import model.HelperMethods;
import model.Node;

import java.util.*;

public class BreadthFirstSearch {
    final private Set<NodeBFS> graph;

    public BreadthFirstSearch(Set<Node> graph) {
        this.graph = buildBFSGraph(graph);
    }

    public void BFSTest(String sourceName) {
        NodeBFS source = initBFS(sourceName);

        breadthFirstSearch(source);

        printBFSResult(source);
    }

    public void printBFSResult(NodeBFS source) {
        List<NodeBFS> listGraph = new ArrayList<>(graph);
        listGraph.sort(Comparator.comparingInt(c -> c.distance == -1 ? Integer.MAX_VALUE : c.distance));
        for(NodeBFS nodeBFS : listGraph) {
            Node node = nodeBFS.node;
            System.out.println("Distance between " + source.node.name() +
                    " and " + node.name() + " : " + (
                    nodeBFS.distance == -1 ? "NOT REACHABLE" :
                            nodeBFS.distance +
                                    " with path " + (Objects.isNull(nodeBFS.parent) ? "itself" :
                                    HelperMethods.printCollection(pathToVertex(nodeBFS))))
            ); }
    }

    public NodeBFS initBFS(String sourceName) {
        NodeBFS source = null;
        for(NodeBFS node : graph) {
            if(node.node.name().equals(sourceName)) {
                source = node;
                break;
            }
        }
        if(source == null) throw new IllegalStateException("Source not found :(");
        return source;
    }

    private void breadthFirstSearch(NodeBFS source) {
        for(NodeBFS c : graph) {
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

    private static List<Node> pathToVertex(NodeBFS source) {
        List<Node> path = new LinkedList<>();
        while(source.distance != 0) {
            path.add(0, source.node);
            source = source.parent;
        }
        path.add(0, source.node);
        return path;
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
