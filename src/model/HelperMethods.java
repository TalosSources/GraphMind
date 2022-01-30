package model;

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
}
