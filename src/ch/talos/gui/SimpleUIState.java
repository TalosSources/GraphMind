package ch.talos.gui;

import ch.talos.model.MutableGraphState;
import ch.talos.model.Node;

public final class SimpleUIState implements UIState {
    private final MutableGraphState graphState;

    private Node focusNode;
    private Node selectedNode;

    public SimpleUIState(MutableGraphState gs, Node focusNode) {
        this.graphState = gs;
        this.focusNode = focusNode;
        this.selectedNode = focusNode;
    }

    @Override
    public void setFocusNode(Node node) {
        this.focusNode = node;
    }

    @Override
    public void setSelectedNode(Node node) {
        this.selectedNode = node;
    }

    @Override
    public Node getFocusNode() {
        return focusNode;
    }

    @Override
    public Node getSelectedNode() {
        return selectedNode;
    }

    @Override
    public MutableGraphState graphState() {
        return graphState;
    }
}
