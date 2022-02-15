package java.model;

import java.util.Set;

public interface UnmodifiableNode extends Node {
    Node withName(String name);

    Node withText(String text);

    Node withUrl(String url);

    /**
     * Returns a new node, similar to this, but with sibling added to the siblings
     *
     * @param sibling the node to be added in the siblings
     * @return a new node, with that new sibling
     */
    Node withSibling(Node sibling);

    /**
     * Returns a new node, similar to this, but with the siblings replaced with the parameter siblings
     *
     * @param siblings the new siblings
     * @return a new node with those siblings instead of the old ones.
     */
    Node withSiblings(Set<Node> siblings);

    /**
     * Returns a new node, similar to this, but with the parent replaced with the parameter parent
     *
     * @param parent the new parent
     * @return a new node with that parent instead of the old one.
     */
    Node withParent(Node parent);

    /**
     * Returns a new node, similar to this, but with the parents replaced with the parameter parents
     *
     * @param parents the new parents
     * @return a new node with those parents instead of the old ones.
     */
    Node withParents(Set<Node> parents);

    Node withChild(Node child);

    Node withChildren(Set<Node> children);
}
