package ch.talos.gui;

import ch.talos.model.ModifiableNode;
import ch.talos.model.MutableGraphState;
import ch.talos.model.Node;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class SimpleUIState implements UIState {
    private final MutableGraphState graphState;

    private final ObjectProperty<ModifiableNode> focusNodeProperty;
    private final ObjectProperty<ModifiableNode> selectedNodeProperty;

    private final StringProperty selectedNodeTextProperty;
    private final StringProperty selectedNodeUrlProperty;

    private final ObservableList<ModifiableNode> siblings;
    private final ObservableList<ModifiableNode> children;
    private final ObservableList<ModifiableNode> parents;
    private final ObservableList<ModifiableNode> childrenOfParents;

    public SimpleUIState(MutableGraphState gs, ModifiableNode focusNode) {
        this.focusNodeProperty = new SimpleObjectProperty<>();
        //this.focusNodeProperty.set(focusNode);
        this.selectedNodeProperty = new SimpleObjectProperty<>();
        //this.selectedNodeProperty.set(focusNode);

        this.selectedNodeTextProperty = new SimpleStringProperty();
//        this.selectedNodeTextProperty.set(focusNode.text());
        this.selectedNodeUrlProperty = new SimpleStringProperty();
//        this.selectedNodeUrlProperty.set(focusNode.url());

        this.graphState = gs;

        siblings = FXCollections.observableArrayList();
        children = FXCollections.observableArrayList();
        parents = FXCollections.observableArrayList();
        childrenOfParents = FXCollections.observableArrayList();

        updateFocusNode(focusNode);
    }

    @Override
    public void updateFocusNode(ModifiableNode node) {
        focusNodeProperty.set(node);
        siblings.setAll(node.siblings());
        children.setAll(node.children());
        parents.setAll(node.parents());
        childrenOfParents.setAll(childrenOfParents(node));
        sortAlphabetically(siblings);
        sortAlphabetically(children);
        sortAlphabetically(parents);
        sortAlphabetically(childrenOfParents);
        updateSelectedNode(node);
    }

    @Override
    public void updateSelectedNode(ModifiableNode node) {
        selectedNodeProperty.set(node);
        selectedNodeTextProperty.set(node.text());
        selectedNodeUrlProperty.set(node.url());
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
    public StringProperty selectedNodeTextProperty() {
        return selectedNodeTextProperty;
    }

    @Override
    public StringProperty selectedNodeUrlProperty() {
        return selectedNodeUrlProperty;
    }

    @Override
    public ObservableList<ModifiableNode> childrenOfParents() {
        return childrenOfParents;
    }

    private static List<ModifiableNode> childrenOfParents(ModifiableNode node) {
        List<ModifiableNode> cOP = new ArrayList<>();
        for(ModifiableNode parent : node.parents())
            for(ModifiableNode childOfParent : parent.children())
                if(!childOfParent.equals(node) && !node.siblings().contains(childOfParent) &&
                !node.children().contains(childOfParent) && !node.parents().contains(childOfParent)
                && !cOP.contains(childOfParent))
                    cOP.add(childOfParent);

        return cOP;
    }

    private void sortAlphabetically(ObservableList<ModifiableNode> list) {
        list.sort(Comparator.comparing(Node::name));
    }
}
