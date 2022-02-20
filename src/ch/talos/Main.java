package ch.talos;

import ch.talos.gui.VisualRepresentation.DrawGraph;
import ch.talos.gui.GraphViewCreator;
import ch.talos.gui.SimpleUIState;
import ch.talos.gui.UIState;
import ch.talos.model.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import ch.talos.analytics.BreadthFirstSearch;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static javafx.application.Platform.isFxApplicationThread;

public class Main extends Application {

    public static void main(String[] args){
        launch(args);
        //simpleTests();
//        uglyDrawing();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Stage mainWindow = new Stage(StageStyle.DECORATED);
        mainWindow.initModality(Modality.WINDOW_MODAL);

        String ownJsonDirectory = HelperMethods.contentOfFile("ownSaveLocation");

        UIState uiState = new SimpleUIState(ownJsonDirectory, null);

        Pane mainPane = GraphViewCreator.graphView(uiState, mainWindow);
        mainPane.setPrefSize(1600, 900);
        Scene scene = new Scene(mainPane);   //setups the scene with the above
        mainWindow.setScene(scene);

        mainWindow.setTitle("GraphMind");
        mainWindow.show();
//        mainWindow.setMaximized(true);
    }

    public static void simpleTests() {
        String externalJsonDirectory = HelperMethods.contentOfFile("externalSaveLocation");
        String ownJsonDirectory = HelperMethods.contentOfFile("ownSaveLocation");

        /** //Those lines fetch the external save and saves it with the simpler format.
         Set<ModifiableNode> graph = ch.talos.JSONSaveManager.generateGraphFromJSON(externalJsonDirectory);
         System.out.println(ch.talos.JSONSaveManager.saveJSONFromGraph(graph, ownJsonDirectory));

         /**/
        Set<ModifiableNode> graph = JSONSaveManager.generateGraphFromJSON(ownJsonDirectory);

        /**
        writeTextRepresentation(graph);

        /**/
        BreadthFirstSearch bfs = new BreadthFirstSearch(graph);
        bfs.BFSTest("Moi");

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

