package model;
import java.io.*;
import java.util.*;
import org.json.*;

import static model.BreadthFirstSearch.BFSTest;

public class Main {

    public static void main(String[] args) throws IOException {
        File sourceFile = new File("C:\\CoolProjectsToAdvanceOneDay\\GraphMind\\TheBrainSave\\thoughts.json");
        FileReader fr = new FileReader(sourceFile);
        BufferedReader br = new BufferedReader(fr);

        String jsonString = br.readLine().substring(1);
        JSONObject object = new JSONObject(jsonString);
        System.out.println(object.get("Name"));
        while(br.ready()) {
            jsonString = br.readLine();
            object = new JSONObject(jsonString);
            System.out.println(object.get("Name"));
        }
    }

    public static void simpleGraphTest() {
        Set<Node> graph = simpleGraph();

        for (Node node : graph) System.out.print(node.textRepresentation());
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

    public static void connectParentChild(ModifiableNode parent, ModifiableNode child) {
        parent.addChild(child);
        child.addParent(parent);
    }

    public static void connectSiblings(ModifiableNode s1, ModifiableNode s2) {
        s1.addSibling(s2);
        s2.addSibling(s1);
    }
}

