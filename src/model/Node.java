package model;

import java.util.Set;

/**
 * I mean I should probably
 */
public interface Node extends Comparable<Node> {
    //connections
    Set<Node> siblings();
    Set<Node> parents();
    Set<Node> children();

    //properties
    String name();
    String text();
    String url();
    String id();


    default String textRepresentation() {
        return name() + " :\n  Siblings: " + siblings() + "\n  Parents: " + parents() +
                "\n  Children: " + children() + "\n  Text: " + text() + "\n  Url: " + url() + "\n\n";
    }

    default int degree() {
        return siblings().size() + parents().size() + children().size();
    }
}

