package ch.talos;

import ch.talos.gui.GraphViewCreator;
import ch.talos.gui.SimpleUIState;
import ch.talos.gui.UIState;
import ch.talos.model.*;
import javafx.application.Application;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import ch.talos.analytics.BreadthFirstSearch;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main extends Application {

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Stage selectionWindow = new Stage(StageStyle.UTILITY);
        selectionWindow.initModality(Modality.WINDOW_MODAL);

        String ownJsonDirectory = HelperMethods.contentOfFile("ownSaveLocation");
        Set<ModifiableNode> graph = JSONSaveManager.generateGraphFromJSON(ownJsonDirectory);
        Node observedNode = null;
        for(Node node : graph) if(node.name().equals("Temps")) {
            observedNode = node;
            break;
        }
        MutableGraphState graphState = new SimpleGraphState(graph);

        UIState uiState = new SimpleUIState(graphState, observedNode);

        HBox box = GraphViewCreator.graphView(uiState);

        Scene scene = new Scene(box);   //setups the scene with the above
        selectionWindow.setScene(scene);

        selectionWindow.show();

    }

    public static void simpleTests() {
        String externalJsonDirectory = HelperMethods.contentOfFile("externalSaveLocation");
        String ownJsonDirectory = HelperMethods.contentOfFile("ownSaveLocation");

        /**/ //Those lines fetch the external save and saves it with the simpler format.
         Set<ModifiableNode> graph = ch.talos.JSONSaveManager.generateGraphFromJSON(externalJsonDirectory);
         System.out.println(ch.talos.JSONSaveManager.saveJSONFromGraph(graph, ownJsonDirectory));

         /**
        Set<ModifiableNode> graph = JSONSaveManager.generateGraphFromJSON(ownJsonDirectory);

        /**
        writeTextRepresentation(graph);

        /**
        BreadthFirstSearch bfs = new BreadthFirstSearch(graph);
        bfs.BFSTest("EPFL");

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

    public static void writeTextRepresentation(Set<? extends Node> graph) {
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

