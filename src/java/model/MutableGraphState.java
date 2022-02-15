package java.model;

import java.util.List;
import java.util.Set;

public interface MutableGraphState {
    Set<ModifiableNode> getGraph();

    List<Node> searchNode(String key);

    void connectParentChild(ModifiableNode parent, ModifiableNode child);

    void connectSiblings(ModifiableNode s1, ModifiableNode s2);

    /**
     * This method assumes there's only one type of connection possible between 2 nodes.
     * Therefore we need to check when connecting that the 2 nodes aren't already connected in any other way
     * @param node1
     * @param node2
     */
    void disconnectNodes(ModifiableNode node1, ModifiableNode node2);
}
