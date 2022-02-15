package java.gui;

import java.model.MutableGraphState;
import java.model.Node;

public interface UIState {

    void setObservedNode(Node node);

    Node getObservedNode();

    MutableGraphState graphState();

}


