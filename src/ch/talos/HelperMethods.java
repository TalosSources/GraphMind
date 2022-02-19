package ch.talos;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Set;

public class HelperMethods {
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
        throw new IllegalArgumentException();
    }
}
