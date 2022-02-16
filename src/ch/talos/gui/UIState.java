package ch.talos.gui;

import ch.talos.model.ModifiableNode;
import ch.talos.model.MutableGraphState;
import ch.talos.model.Node;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public interface UIState {

    void updateFocusNode(ModifiableNode node);

    void updateSelectedNode(ModifiableNode node);

    ModifiableNode getFocusNode();

    ModifiableNode getSelectedNode();

    MutableGraphState graphState();

    ObservableList<ModifiableNode> siblings();

    ObservableList<ModifiableNode> children();

    ObservableList<ModifiableNode> parents();

    ObservableList<ModifiableNode> childrenOfParents();

    ObjectProperty<ModifiableNode> focusNodeProperty();

    ObjectProperty<ModifiableNode> selectedNodeProperty();

    StringProperty selectedNodeTextProperty();

    StringProperty selectedNodeUrlProperty();
}


