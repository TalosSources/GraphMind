package model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

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
}
