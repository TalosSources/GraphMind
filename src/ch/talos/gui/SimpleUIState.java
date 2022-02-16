package ch.talos.gui;

import ch.talos.model.ModifiableNode;
import ch.talos.model.MutableGraphState;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public final class SimpleUIState implements UIState {
    private final MutableGraphState graphState;

    private final ObjectProperty<ModifiableNode> focusNodeProperty;
    private final ObjectProperty<ModifiableNode> selectedNodeProperty;

    private final StringProperty focusNodeTextProperty;

    private final ObservableList<ModifiableNode> siblings;
    private final ObservableList<ModifiableNode> children;
    private final ObservableList<ModifiableNode> parents;

    public SimpleUIState(MutableGraphState gs, ModifiableNode focusNode) {
        this.focusNodeProperty = new SimpleObjectProperty<>();
        this.focusNodeProperty.set(focusNode);
        this.selectedNodeProperty = new SimpleObjectProperty<>();
        this.selectedNodeProperty.set(focusNode);

        this.focusNodeTextProperty = new SimpleStringProperty();
        this.focusNodeTextProperty.set(focusNode.text());

        this.graphState = gs;

        siblings = FXCollections.observableArrayList(new ArrayList<>(focusNode.siblings()));
        children = FXCollections.observableArrayList(new ArrayList<>(focusNode.children()));
        parents = FXCollections.observableArrayList(new ArrayList<>(focusNode.parents()));
    }

    @Override
    public void updateFocusNode(ModifiableNode node) {
        focusNodeProperty.set(node);
        siblings.setAll(node.siblings());
        children.setAll(node.children());
        parents.setAll(node.parents());
        focusNodeTextProperty.set(node.text());
    }

    @Override
    public void updateSelectedNode(ModifiableNode node) {
        selectedNodeProperty.set(node);
    }

    @Override
    public ModifiableNode getFocusNode() {
        return focusNodeProperty.get();
    }

    @Override
    public ModifiableNode getSelectedNode() {
        return selectedNodeProperty.get();
    }

    @Override
    public MutableGraphState graphState() {
        return graphState;
    }

    @Override
    public ObservableList<ModifiableNode> siblings() {
        return siblings;
    }

    @Override
    public ObservableList<ModifiableNode> children() {
        return children;
    }

    @Override
    public ObservableList<ModifiableNode> parents() {
        return parents;
    }

    @Override
    public ObjectProperty<ModifiableNode> focusNodeProperty() {
        return focusNodeProperty;
    }

    @Override
    public ObjectProperty<ModifiableNode> selectedNodeProperty() {
        return selectedNodeProperty;
    }

    @Override
    public StringProperty focusNodeTextProperty() {
        return focusNodeTextProperty;
    }
}
