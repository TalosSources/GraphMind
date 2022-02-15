package ch.talos.model;

import java.util.List;
import java.util.Set;

public class SimpleGraphState implements MutableGraphState{
    private final Set<ModifiableNode> graph;

    public SimpleGraphState(Set<ModifiableNode> graph) {
        this.graph = graph;
    }

    @Override
    public Set<ModifiableNode> getGraph() {
        return graph;
    }

    @Override
    public List<Node> searchNode(String key) {
        //TODO : Binary tree? something where the nodes are sorted alphabetically
        return null;
    }

    @Override
    public void connectParentChild(ModifiableNode parent, ModifiableNode child) {
        parent.addChild(child);
        child.addParent(parent);
    }

    @Override
    public void connectSiblings(ModifiableNode s1, ModifiableNode s2) {
        s1.addSibling(s2);
        s2.addSibling(s1);
    }

    @Override
    public void disconnectNodes(ModifiableNode node1, ModifiableNode node2) {
        node1.removeChild(node2);
        node1.removeParent(node2);
        node1.removeSibling(node2);
        node2.removeChild(node1);
        node2.removeParent(node1);
        node2.removeSibling(node1);
    }
}