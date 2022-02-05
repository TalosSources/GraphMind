package model;

import org.json.JSONObject;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class JSONGraphCreator {
    public static Set<Node> generateGraph(String directory) {

        Map<String, ModifiableNode> map = new TreeMap<>();
        Desktop desktop = java.awt.Desktop.getDesktop();

            //First we use the thought file to create all the nodes
        BufferedReader sourceReader = fileBufferedReader(directory + "thoughts.json");
        try {
            while(sourceReader.ready()) {
                String jsonString = sourceReader.readLine();
                JSONObject object = new JSONObject(jsonString);
                try {
                    object.getString("ForgottenDateTime");
                    //System.out.println("Forgotten : " + object.getString("Name"));
                    continue;
                } catch (Exception e) {
                    if(object.getInt("ACType") == 1) continue;
                }
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
                if(Objects.isNull(nodeA) || Objects.isNull(nodeB)) continue; //link with a forgotten thought

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

            //After that we fill in the attachments
        BufferedReader attachmentsReader = fileBufferedReader(directory + "attachments.json");
        try {
            while(attachmentsReader.ready()) {
                String jsonString = attachmentsReader.readLine();
                JSONObject object = new JSONObject(jsonString);

                ModifiableNode node = map.get(object.getString("SourceId"));

                if(Objects.isNull(node)) {
                    System.out.println("That one's null : " + object.getString("SourceId"));
                    continue;
                }

                //Type 1 : notes; Type 2 : ?; Type 3 : URL; Type 4 : html notes;
                switch(object.getInt("Type")) {
                    case 1 :
                        String name = object.getString("Name");
                        if(name.equals("Notes.md")) {
                            String text = HelperMethods.contentOfFile(directory + node.id() + "\\" + name);
                            node.setText(text);
                        }
                        break;
                    case 3 :
                        String url = object.getString("Location");
                        node.setUrl(url);

                       /********************************
                        URI oURL = null;
                        try {
                            oURL = new URI(url);
                        } catch (URISyntaxException e) { e.printStackTrace(); }
                        desktop.browse(oURL);
                        /********************************/

                        break;
                    default :
                        //System.out.println("weird node : " + node.name());
                        break;
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
            System.out.println("Source file not found :(. File was " + directory);
        }

        return new BufferedReader(fileReader);
    }
}
