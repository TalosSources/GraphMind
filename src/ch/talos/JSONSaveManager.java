package ch.talos;

import ch.talos.model.ModifiableNode;
import ch.talos.model.Node;
import ch.talos.model.SimpleNode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.*;

public class JSONSaveManager {

    public static Set<ModifiableNode> generateGraphFromJSON(String directory) {

        Map<String, ModifiableNode> map = new TreeMap<>();

            //First we use the thought file to create all the nodes
        BufferedReader sourceReader = fileBufferedReader(directory + "thoughts.json");
        try {
            while(sourceReader.ready()) {
                String jsonString = sourceReader.readLine();
                JSONObject object = new JSONObject(jsonString);

                try {
                    object.getString("ForgottenDateTime"); //Filters out forgotten thoughts
                    continue;
                } catch (JSONException e) { }
                try {
                    if(object.getInt("ACType") == 1) continue; //Filters out private thoughts
                } catch (JSONException e) { } //If it has no private type value at all, we consider it not private

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
                    //System.out.println("That one's null : " + object.getString("SourceId"));
                    continue;
                }

                //Type 1 : notes; Type 2 : ?; Type 3 : URL; Type 4 : html notes;
                switch(object.getInt("Type")) {
                    case 1 :
                        String name = object.getString("Name");
                        if(name.equals("Notes.md") || name.equals("Notes.txt")) {
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


        return new TreeSet<>(map.values());
    }

    public static boolean saveJSONFromGraph(Set<? extends Node> graph, String directory) {
        File dir = new File(directory);
        if(!dir.isDirectory()) return false;
        HelperMethods.deleteDirectory(dir);

        try {
            BufferedWriter thoughtsWriter = fileBufferedWriter(directory + "thoughts.json");
            BufferedWriter linksWriter = fileBufferedWriter(directory + "links.json");
            BufferedWriter attachmentsWriter = fileBufferedWriter(directory + "attachments.json");

            Set<Node> visited = new TreeSet<>();

            for(Node u : graph) {
                //create the thought
                visited.add(u);
                JSONObject thought = new JSONObject();
                thought.put("Name", u.name());
                thought.put("Id", u.id());
                thoughtsWriter.write(thought.toString() + "\n");

                //add the necessary links
                for(Node v : u.children()) {
                    writeLink(linksWriter, 1, u, v);
                }
                for(Node v : u.siblings()) {
                    if(visited.contains(v)) continue;
                    writeLink(linksWriter, 3, u, v);
                }

                //add the attachments
                if(!Objects.isNull(u.text())) {
                    JSONObject text = new JSONObject();
                    text.put("SourceId", u.id());
                    text.put("Type", 1);
                    text.put("Name", "Notes.txt");

                    File thoughtDir = new File(directory + u.id());
                    thoughtDir.mkdirs();

                    attachmentsWriter.write(text.toString() + "\n");
                    HelperMethods.writeStringToFile(thoughtDir.getAbsolutePath() + "\\Notes.txt", u.text());
                }

                if(!Objects.isNull(u.url())) {
                    JSONObject url = new JSONObject();
                    url.put("SourceId", u.id());
                    url.put("Type", 3);
                    url.put("Location", u.url());
                    attachmentsWriter.write(url.toString() + "\n");
                }
            }

            thoughtsWriter.flush();
            thoughtsWriter.close();
            linksWriter.flush();
            linksWriter.close();
            attachmentsWriter.flush();
            attachmentsWriter.close();
        } catch (IOException ioe) {
            return false;
        }

        return true; //return values indicates whether everything went well
    }

    public static void writeLink(BufferedWriter out, int relation, Node u, Node v) throws IOException {
        JSONObject link = new JSONObject();
        link.put("ThoughtIdA", u.id());
        link.put("ThoughtIdB", v.id());
        link.put("Relation", relation);
        out.write(link.toString() + "\n");
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

    public static BufferedWriter fileBufferedWriter(String directory) {
        File sourceFile = new File(directory);

        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(sourceFile);
        } catch (IOException e) {
            System.out.println("An error happened during writing :( ");
        }

        return new BufferedWriter(fileWriter);
    }
}
