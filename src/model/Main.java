package model;
import model.Analytics.BreadthFirstSearch;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args){
        String externalJsonDirectory = HelperMethods.contentOfFile("externalSaveLocation");
        String ownJsonDirectory = HelperMethods.contentOfFile("ownSaveLocation");

        /**/
        Set<Node> graph = JSONSaveManager.generateGraphFromJSON(externalJsonDirectory);

        /**/
        System.out.println(
               JSONSaveManager.saveJSONFromGraph(
                     graph, ownJsonDirectory));

        /**
        writeTextRepresentation(graph);

        /**
        BreadthFirstSearch bfs = new BreadthFirstSearch(graph);
        bfs.BFSTest("GraphMind");

        /**

        for(Node n : graph)
            if(n.url() != null && n.text() != null) System.out.println(n);

        /**

        List<Node> listGraph = new ArrayList<>(graph);
        listGraph.sort(Comparator.comparingDouble(n -> -n.degree()));
        for(Node node : listGraph) {
            System.out.println(node.name() + " : " + node.degree());
        } /**/
    }

    public static void simpleGraphTest() {
        Set<Node> graph = simpleGraph();

        for (Node node : graph) System.out.print(node.textRepresentation());
    }

    public static Set<Node> simpleGraph() {
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

    public static void connectParentChild(ModifiableNode parent, ModifiableNode child) {
        parent.addChild(child);
        child.addParent(parent);
    }

    public static void connectSiblings(ModifiableNode s1, ModifiableNode s2) {
        s1.addSibling(s2);
        s2.addSibling(s1);
    }

    public static void writeTextRepresentation(Set<Node> graph) {
        try {
            FileWriter fw = new FileWriter(new File("brain.txt"));
            for(Node node : graph) {
                fw.write(node.textRepresentation());
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}

