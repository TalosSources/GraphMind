package ch.talos;

import ch.talos.model.ModifiableNode;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Random;
import java.util.Set;

public final class HelperMethods {
    final public static Random RNG = new Random();

    final static private char[] HEX_ALPHABET =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    final static private int[] format = {8, 4, 4, 4, 12};

    public static <T> String printCollection(Collection<T> collection) {
        StringBuilder sb = new StringBuilder().append('[');
        for(T elem : collection) {
            sb.append(elem.toString()).append(", ");
        }
        if(!collection.isEmpty()) sb.delete(sb.length() - 2, sb.length());

        return sb.append(']').toString();
    }

    public static String contentOfFile(String path) {
        String content = null;
        try {
            content = Files.readString(Path.of(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content;
    }

    public static boolean writeStringToFile(String address, String text) {
        Path path = Path.of(address);
        try {
            Files.writeString(path, text);
        } catch (IOException e) {
            System.out.println("Writing error for address " + address);
            return false;
        }

        return true;
    }

    public static void deleteDirectory(File directory) {
        for(File garbage : directory.listFiles()) {
            if(garbage.isDirectory()) {
                deleteDirectory(garbage);
            }
            garbage.delete();
        }
    }

    public static <T> T anyElement(Set<T> set) {
        for(T elem : set) return elem;
        throw new IllegalArgumentException("bon bof la");
    }


    /**
     * The id format : HHHHHHHH-HHHH-HHHH-HHHH-HHHHHHHHHHHH (where H is an hex figure)
     * @param node
     * @param graph
     */
    public static void setUniqueId(ModifiableNode node, Set<ModifiableNode> graph) {
        do {
            String generated = generateString(HEX_ALPHABET, format, '-');
            node.setId(generated);
        } while(graph.contains(node));
        System.out.println("Generated id : " + node.id());
    }

    public static String generateString(char[] alphabet, int[] format, char separator) {
        StringBuilder sb = new StringBuilder();

        for(int l : format) {
            for(int i = 0; i < l; ++i) sb.append(alphabet[RNG.nextInt(alphabet.length)]);
            sb.append(separator);
        }
        if(sb.length() > 0) sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }
}
