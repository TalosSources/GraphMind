package ch.talos.gui;

import ch.talos.model.MutableGraphState;
import ch.talos.model.Node;

public interface UIState {

    void setFocusNode(Node node);

    void setSelectedNode(Node node);

    Node getFocusNode();

    Node getSelectedNode();

    MutableGraphState graphState();

}


