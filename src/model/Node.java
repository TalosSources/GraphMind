package model;

import java.util.Set;

/**
 * I mean I should probably
 */
interface Node extends Comparable<Node> {
    //connections
    Set<Node> siblings();
    Set<Node> parents();
    Set<Node> children();

    //properties
    String name();
    String text();
    String url();


    default public String print() {
        return name() + " :\n  Siblings: " + siblings() + "\n  Parents: " + parents() +
                "\n  Children: " + children() + "\n  Text: " + text() + "\n  Url: " + url() + "\n\n";
    }
}

interface ModifiableNode extends Node {
    //setters
    void setName(String name);
    void setText(String text);
    void setUrl(String url);

    void addSibling(Node sibling);
    void addParent(Node parent);
    void addChild(Node children);

    boolean removeSibling(Node sibling);
    boolean removeParent(Node parent);
    boolean removeChild(Node child);
}

interface UnmodifiableNode extends Node {
    Node withName(String name);
    Node withText(String text);
    Node withUrl(String url);

    /**
     * Returns a new node, similar to this, but with sibling added to the siblings
     * @param sibling the node to be added in the siblings
     * @return a new node, with that new sibling
     */
    Node withSibling(Node sibling);

    /**
     * Returns a new node, similar to this, but with the siblings replaced with the parameter siblings
     * @param siblings the new siblings
     * @return a new node with those siblings instead of the old ones.
     */
    Node withSiblings(Set<Node> siblings);

    /**
     * same
     * @param parent
     * @return
     */
    Node withParent(Node parent);

    Node withParents(Set<Node> parents);

    Node withChild(Node child);

    Node withChildren(Set<Node> children);
}
