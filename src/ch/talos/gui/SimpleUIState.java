package ch.talos.gui;

import ch.talos.JSONSaveManager;
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
import java.util.Objects;

public final class SimpleUIState implements UIState {
    private final MutableGraphState graphState;

    private final ObjectProperty<ModifiableNode> focusNodeProperty;
    private final ObjectProperty<ModifiableNode> selectedNodeProperty;

    private final StringProperty focusNodeNameProperty;
    private final StringProperty selectedNodeNameProperty;
    private final StringProperty selectedNodeTextProperty;
    private final StringProperty selectedNodeUrlProperty;

    private final ObservableList<ModifiableNode> siblings;
    private final ObservableList<ModifiableNode> children;
    private final ObservableList<ModifiableNode> parents;
    private final ObservableList<ModifiableNode> childrenOfParents;

    private final StringProperty saveLocation;

    public SimpleUIState(MutableGraphState gs, ModifiableNode focusNode, String saveLocation) {
        this.focusNodeProperty = new SimpleObjectProperty<>();
        this.selectedNodeProperty = new SimpleObjectProperty<>();

        this.focusNodeNameProperty = new SimpleStringProperty();
        this.selectedNodeNameProperty = new SimpleStringProperty();
        this.selectedNodeTextProperty = new SimpleStringProperty();
        this.selectedNodeUrlProperty = new SimpleStringProperty();

        this.saveLocation = new SimpleStringProperty();

        this.graphState = gs;

        siblings = FXCollections.observableArrayList();
        children = FXCollections.observableArrayList();
        parents = FXCollections.observableArrayList();
        childrenOfParents = FXCollections.observableArrayList();

        updateFocusNode(focusNode);

        this.saveLocation.set(saveLocation);
        //JSONSaveManager.generateGraphFromJSON(this.saveLocation.get());

        focusNodeNameProperty.addListener((ov, o, n) ->
                getFocusNode().setName(focusNodeNameProperty.get()));
        selectedNodeTextProperty.addListener((ov, o, n) ->
                getSelectedNode().setText(selectedNodeTextProperty.get()));
        selectedNodeUrlProperty.addListener((ov, o, n) ->
                getSelectedNode().setUrl(selectedNodeUrlProperty.get()));
    }

    @Override
    public void updateFocusNode(ModifiableNode node) {
        focusNodeProperty.set(node);
        refreshLinks();
        updateSelectedNode(node);
        focusNodeNameProperty.set(Objects.isNull(node) ?
                "Select a node or create one :)" :
                node.name());
    }

    @Override
    public void updateSelectedNode(ModifiableNode node) {
        boolean n = Objects.isNull(node);
        selectedNodeProperty.set(node);
        selectedNodeNameProperty.set(n ? "" : node.name());
        selectedNodeTextProperty.set(n ? "" : node.text());
        selectedNodeUrlProperty.set(n ? "" : node.url());
    }

    @Override
    public void refreshLinks() {
        if(Objects.isNull(getFocusNode())) {
            siblings.removeAll();
            children.removeAll();
            parents.removeAll();
            childrenOfParents.removeAll();
            return;
        }
        siblings.setAll(getFocusNode().siblings());
        children.setAll(getFocusNode().children());
        parents.setAll(getFocusNode().parents());
        childrenOfParents.setAll(childrenOfParents(getFocusNode()));
        sortAlphabetically(siblings);
        sortAlphabetically(children);
        sortAlphabetically(parents);
        sortAlphabetically(childrenOfParents);
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
    public StringProperty focusNodeNameProperty() {
        return focusNodeNameProperty;
    }

    @Override
    public StringProperty selectedNodeNameProperty() {
        return selectedNodeNameProperty;
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
