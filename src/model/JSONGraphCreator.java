package model;

import org.json.JSONObject;

import java.io.*;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class JSONGraphCreator {
    public static Set<Node> generateGraph(String directory) {

        Map<String, ModifiableNode> map = new TreeMap<>();

            //First we use the thought file to create all the nodes
        BufferedReader sourceReader = fileBufferedReader(directory + "thoughts.json");
        try {
            while(sourceReader.ready()) {
                String jsonString = sourceReader.readLine();
                JSONObject object = new JSONObject(jsonString);
                ModifiableNode node = new SimpleNode(object.getString("Name"));
                node.setId(object.getString("Id"));
                map.put(node.id(), node);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

            //Then we use the links file to link them
        BufferedReader linksReader = fileBufferedReader(directory + "links.json");
        try {
            while(linksReader.ready()) {
                String jsonString = linksReader.readLine();
                JSONObject object = new JSONObject(jsonString);

                ModifiableNode nodeA = map.get(object.getString("ThoughtIdA"));
                ModifiableNode nodeB = map.get(object.getString("ThoughtIdB"));
                int relationType = object.getInt("Relation");

                if(relationType == 1) {
                    //parent/child
                    nodeA.addChild(nodeB);
                    nodeB.addParent(nodeA);
                } else {
                    //siblings
                    nodeA.addSibling(nodeB);
                    nodeB.addSibling(nodeA);
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }


        return new TreeSet(map.values());
    }

    public static BufferedReader fileBufferedReader(String directory) {
        File sourceFile = new File(directory);

        FileReader fileReader = null;
        try {
            fileReader = new FileReader(sourceFile);
        } catch (FileNotFoundException e) {
            System.out.println("Source file not found :(");
        }

        return new BufferedReader(fileReader);
    }
}
