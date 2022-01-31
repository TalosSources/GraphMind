package model;

public interface ModifiableNode extends Node {
    //setters
    void setName(String name);

    void setText(String text);

    void setUrl(String url);

    void setId(String id); //In principle shouldn't be changed

    void addSibling(Node sibling);

    void addParent(Node parent);

    void addChild(Node children);

    boolean removeSibling(Node sibling);

    boolean removeParent(Node parent);

    boolean removeChild(Node child);
}
