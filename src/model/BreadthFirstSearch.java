package model;

import java.util.*;

public class BreadthFirstSearch {

    public static void BFSTest(Set<Node> graph0, String sourceName) {
        Set<NodeBFS> graph = buildBFSGraph(graph0);

        NodeBFS source = null;
        for(NodeBFS node : graph) {
            if(node.node.name().equals(sourceName)) {
                source = node;
                break;
            }
        }
        if(source == null) throw new IllegalStateException("Source not found :(");

        breadthFirstSearch(graph, source);

        List<NodeBFS> listGraph = new ArrayList<>(graph);
        listGraph.sort(Comparator.comparingInt(c -> c.distance == -1 ? Integer.MAX_VALUE : c.distance));
        for(NodeBFS nodeBFS : listGraph) {
            Node node = nodeBFS.node;
            System.out.println("Distance between " + source.node.name() +
                    " and " + node.name() + " : " + (
                    nodeBFS.distance == -1 ? "NOT REACHABLE" :
                            nodeBFS.distance +
                                    " with path " + (Objects.isNull(nodeBFS.parent) ? "itself" :
                                    HelperMethods.printCollection(pathToVertex(graph, nodeBFS))))
            ); }
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
