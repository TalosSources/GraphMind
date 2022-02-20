package ch.talos.gui;

import ch.talos.JSONSaveManager;
import ch.talos.model.ModifiableNode;
import ch.talos.model.MutableGraphState;
import ch.talos.model.Node;
import ch.talos.model.SimpleGraphState;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

public final class SimpleUIState implements UIState {
    private MutableGraphState graphState;

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

    public SimpleUIState(String saveLocation, String startId) {
        this.focusNodeProperty = new SimpleObjectProperty<>();
        this.selectedNodeProperty = new SimpleObjectProperty<>();

        this.focusNodeNameProperty = new SimpleStringProperty();
        this.selectedNodeNameProperty = new SimpleStringProperty();
        this.selectedNodeTextProperty = new SimpleStringProperty();
        this.selectedNodeUrlProperty = new SimpleStringProperty();

        this.saveLocation = new SimpleStringProperty();

        siblings = FXCollections.observableArrayList();
        children = FXCollections.observableArrayList();
        parents = FXCollections.observableArrayList();
        childrenOfParents = FXCollections.observableArrayList();

        this.saveLocation.set(saveLocation);

        loadSave(startId);

        focusNodeNameProperty.addListener((ov, o, n) -> {
             if(!Objects.isNull(getFocusNode())) getFocusNode().setName(focusNodeNameProperty.get());
        });
        selectedNodeTextProperty.addListener((ov, o, n) -> {
            if(!Objects.isNull(getFocusNode())) getSelectedNode().setText(selectedNodeTextProperty.get());
        });
        selectedNodeUrlProperty.addListener((ov, o, n) -> {
            if(!Objects.isNull(getFocusNode())) getSelectedNode().setUrl(selectedNodeUrlProperty.get());
        });

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

    @Override
    public StringProperty saveLocation() {
        return saveLocation;
    }

    public void loadSave(String startId) {
        Set<ModifiableNode> graph = JSONSaveManager.generateGraphFromJSON(saveLocation.get());
        this.graphState = new SimpleGraphState(graph);

        ModifiableNode startNode = null;
        if(!Objects.isNull(startId))
            for(ModifiableNode node : this.graphState.getGraph())
                if(startId.equals(node.id())) startNode = node;
        focusNodeProperty.set(startNode);
        updateFocusNode(getFocusNode());
    }

    public void writeSave() {
        System.out.println("Writing to " + saveLocation.get());
        JSONSaveManager.saveJSONFromGraph(graphState.getGraph(), saveLocation().get());
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
