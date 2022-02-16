package ch.talos.model;

import java.util.Set;

public interface ModifiableNode extends Node {
    //setters
    void setName(String name);

    void setText(String text);

    void setUrl(String url);

    void setId(String id); //In principle shouldn't be changed

    void addSibling(ModifiableNode sibling);

    void addParent(ModifiableNode parent);

    void addChild(ModifiableNode children);

    @Override
    Set<ModifiableNode> siblings();
    @Override
    Set<ModifiableNode> parents();
    @Override
    Set<ModifiableNode> children();

    /**
     * The remove methods should do nothing when there's no link to remove.
     * @param sibling
     * @return
     */
    boolean removeSibling(ModifiableNode sibling);

    boolean removeParent(ModifiableNode parent);

    boolean removeChild(ModifiableNode child);
}
