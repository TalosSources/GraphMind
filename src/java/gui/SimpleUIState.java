package java.gui;

import java.model.MutableGraphState;
import java.model.Node;

public class SimpleUIState implements UIState {
    private final MutableGraphState graphState;

    private Node observedNode;

    public SimpleUIState(MutableGraphState gs) {
        this.graphState = gs;
    }

    @Override
    public void setObservedNode(Node node) {
        this.observedNode = node;
    }

    @Override
    public Node getObservedNode() {
        return observedNode;
    }

    @Override
    public MutableGraphState graphState() {
        return graphState;
    }
}
