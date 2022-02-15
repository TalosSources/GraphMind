package java.model;

public interface ModifiableNode extends Node {
    //setters
    void setName(String name);

    void setText(String text);

    void setUrl(String url);

    void setId(String id); //In principle shouldn't be changed

    void addSibling(Node sibling);

    void addParent(Node parent);

    void addChild(Node children);

    /**
     * The remove methods should do nothing when there's no link to remove.
     * @param sibling
     * @return
     */
    boolean removeSibling(Node sibling);

    boolean removeParent(Node parent);

    boolean removeChild(Node child);
}
