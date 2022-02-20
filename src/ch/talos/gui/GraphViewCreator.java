package ch.talos.gui;

import ch.talos.HelperMethods;
import ch.talos.analytics.KeySearch;
import ch.talos.gui.VisualRepresentation.DrawGraph;
import ch.talos.model.LinkType;
import ch.talos.model.ModifiableNode;
import ch.talos.model.SimpleNode;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.*;
import javafx.stage.Window;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class GraphViewCreator {

    /**
     * In theory, that method should take as arguments some eventHandlers, like in tCHu, that would be used when the
     * user perform actions
     * @param uiState
     * @return
     */
    public static Pane graphView(UIState uiState, Window mainWindow) {
        Pane mainGraphPane = graphPane(uiState);

        TextField focusedNodeName = new TextField();
        focusedNodeName.textProperty().bindBidirectional(uiState.focusNodeNameProperty());
        focusedNodeName.fontProperty().set(Font.font("Helvetica", 30));
        focusedNodeName.onMouseClickedProperty().set(e ->
                uiState.updateSelectedNode(uiState.getFocusNode()));

        Text selectedNodeName = new Text();
        selectedNodeName.textProperty().bind(uiState.selectedNodeNameProperty());
        selectedNodeName.fontProperty().set(Font.font("Helvetica", 20));

        TextArea nodeText = new TextArea();
        nodeText.textProperty().bindBidirectional(uiState.selectedNodeTextProperty());
        nodeText.setWrapText(true);
        nodeText.setPrefWidth(600);
        nodeText.setPrefHeight(500);

        nodeText.setEditable(true);
        //nodeText.setWrappingWidth(400);

        TextField urlField = new TextField();
        urlField.textProperty().bindBidirectional(uiState.selectedNodeUrlProperty());
        Button urlButton = new Button("Go to that URL");
        urlButton.setOnAction(e -> {
            String url = uiState.selectedNodeUrlProperty().get();
            if(url != null){
                URI oURL = null;
                try {
                    oURL = new URI(url);
                } catch (URISyntaxException ex) { ex.printStackTrace(); }
                Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.browse(oURL);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        Button drawingButton = new Button("Graph Visualisation");
        drawingButton.setOnAction(event -> {
            if(Objects.isNull(uiState.getFocusNode())) return;
            new Thread(() -> DrawGraph.performDrawing(uiState.graphState(), uiState.getFocusNode())).start();
        });

        VBox textAndURL = new VBox(selectedNodeName, new Separator(), nodeText, new Separator(), urlField, urlButton);

        VBox childrenButtonsTest = new VBox();
        uiState.children().addListener((ListChangeListener<? super ModifiableNode>) event -> {
            List<Button> buttonsList = new ArrayList<>();
            for(ModifiableNode child : uiState.children()) {
                Button button = new Button(child.name());
                button.setOnAction(event2 -> uiState.updateFocusNode(child));
                buttonsList.add(button);
            }
            childrenButtonsTest.getChildren().setAll(buttonsList);
        });

        Button addNodeButton = new Button("Create a new Node");
        addNodeButton.setOnAction(e -> addNodePopup(uiState));

        Button deleteNode = new Button("Delete active node");
        deleteNode.setOnAction(e -> {
            if(Objects.isNull(uiState.getFocusNode())) return;
            ModifiableNode newFocusNode = newFocusNode(uiState.getFocusNode());
            uiState.graphState().removeNode(uiState.getFocusNode());
            uiState.updateFocusNode(newFocusNode);
        });
        VBox buttons = new VBox(drawingButton, addNodeButton, deleteNode);
        buttons.setSpacing(8);

        Button fileChooserButton = new Button("Select save directory");
        DirectoryChooser directoryChooser = new DirectoryChooser();
        TextField saveDirectory = new TextField();
        saveDirectory.setEditable(false);
        saveDirectory.setPrefWidth(400);
        saveDirectory.textProperty().bind(uiState.saveLocation());
        fileChooserButton.setOnAction(e -> {
            File savePlace = directoryChooser.showDialog(mainWindow);
            uiState.saveLocation().set(savePlace.getAbsolutePath());
        });
        Button loadButton = new Button("Load graph");
        loadButton.setOnAction(e -> uiState.loadSave(null));
        Button writeButton = new Button("Save graph");
        writeButton.setOnAction(e -> uiState.writeSave());
        VBox saveBox = new VBox(fileChooserButton, saveDirectory, new Separator(), loadButton, writeButton);

        HBox drawButtonAndSearchbar = new HBox(buttons, new Separator(), searchBox(uiState), saveBox);

        VBox listAndName = new VBox(focusedNodeName, new Separator(), mainGraphPane,
                new Separator(), drawButtonAndSearchbar);

        return new HBox(listAndName, textAndURL);
    }

    private static ModifiableNode newFocusNode(ModifiableNode old) {
        if(!old.parents().isEmpty()) return HelperMethods.anyElement(old.parents());
        if(!old.children().isEmpty()) return HelperMethods.anyElement(old.children());
        if(!old.siblings().isEmpty()) return HelperMethods.anyElement(old.siblings());
        return null;
    }

    private static Pane graphPane(UIState uiState) {
        ListView<ModifiableNode> siblings = new ListView<>(uiState.siblings());
        ListView<ModifiableNode> children = new ListView<>(uiState.children());
        ListView<ModifiableNode> parents = new ListView<>(uiState.parents());
        ListView<ModifiableNode> childrenOfParents = new ListView<>(uiState.childrenOfParents());

        addListenerList(siblings, uiState); //View changes when the selected item changes
        addListenerList(children, uiState);
        addListenerList(parents, uiState);
        addListenerList(childrenOfParents, uiState);

        VBox siblingsBox = new VBox(new Label("Siblings"), siblings);
        VBox childrenBox = new VBox(new Label("Children"), children);
        VBox parentsBox = new VBox(new Label("Parents"), parents);
        VBox childrenOfParentsBox = new VBox(new Label("ChildrenOfParents"), childrenOfParents);

        return new HBox(parentsBox, childrenBox, siblingsBox, childrenOfParentsBox);
    }

    private static Pane searchBox(UIState uiState) {
        TextField searchField = new TextField();
        searchField.editableProperty().set(true);

        ObservableList<ModifiableNode> results = FXCollections.observableArrayList();
        ListView<ModifiableNode> resultsView = new ListView<>(results);
        addListenerList(resultsView, uiState);
        //resultsView.onMouseClickedProperty().set(buttonEvent(resultsView, uiState));

        searchField.textProperty().addListener(event -> {
            results.setAll(KeySearch.search(uiState.graphState().getGraph(), searchField.textProperty().get()));
        });

        //Button addChild = new Button("Add child");
        Button addChild = addLinkButton("Add child", LinkType.CHILD, uiState, resultsView);
        Button addParent = addLinkButton("Add parent", LinkType.PARENT, uiState, resultsView);
        Button addSibling = addLinkButton("Add sibling", LinkType.SIBLING, uiState, resultsView);
        Button disconnect = new Button("Disconnect");
        disconnect.setOnAction(e ->
                {
                    uiState.graphState().disconnectNodes(
                        resultsView.getSelectionModel().getSelectedItem(), uiState.getFocusNode());
                    uiState.refreshLinks();
                });
        HBox linkButtons = new HBox(addChild, addParent, addSibling, disconnect);
        linkButtons.setSpacing(5);

        VBox searchBox = new VBox(searchField, resultsView, linkButtons);
        searchBox.setSpacing(8);

        return searchBox;
    }

    private static void addLink(ModifiableNode selectedItem, UIState uiState, LinkType type) {
        if(Objects.isNull(selectedItem) || Objects.isNull(uiState.getFocusNode())) return;
        System.out.println("adding " + type + selectedItem + " to " + uiState.getFocusNode());
        switch(type) {
            case CHILD: uiState.graphState().connectParentChild(uiState.getFocusNode(), selectedItem);
                break;
            case PARENT: uiState.graphState().connectParentChild(selectedItem, uiState.getFocusNode());
                break;
            case SIBLING: uiState.graphState().connectSiblings(uiState.getFocusNode(), selectedItem);
        }
        uiState.refreshLinks();
    }

    private static Button addLinkButton(String text, LinkType type,
                                        UIState uiState, ListView<ModifiableNode> lv) {
        Button button = new Button(text);

        button.setOnAction(event -> addLink(lv.getSelectionModel().getSelectedItem(), uiState, type));

        return button;
    }

    private static EventHandler<? super MouseEvent> buttonEvent(ListView<ModifiableNode> siblings,
                                                                UIState uiState) {
        return event -> {
            ModifiableNode selectedNode = siblings.getSelectionModel().getSelectedItem();
            if(selectedNode != null) {
                if(event.getClickCount() == 2)
                    uiState.updateFocusNode(selectedNode);
                else
                    uiState.updateSelectedNode(selectedNode);
            }
        };
    }

    private static void addListenerList(ListView<ModifiableNode> lw, UIState uiState) {
        lw.getSelectionModel().selectedItemProperty().addListener((observableValue, modifiableNode, t1) -> {
            ModifiableNode newNode = lw.getSelectionModel().getSelectedItem();
            if(!Objects.isNull(newNode)) uiState.updateSelectedNode(newNode);
        });
        lw.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            ModifiableNode newNode = lw.getSelectionModel().getSelectedItem();
            if(t1 && !Objects.isNull(newNode)) uiState.updateSelectedNode(newNode);
        });
        lw.onMouseClickedProperty().set(buttonEvent(lw, uiState));
        lw.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode().equals(KeyCode.ENTER))
                uiState.updateFocusNode(uiState.getSelectedNode());
        });
    }

    private static void addNodePopup(UIState uiState) {
        Stage popup = new Stage(StageStyle.UTILITY);
        popup.initModality(Modality.WINDOW_MODAL);

        TextField nameField = new TextField();
        nameField.editableProperty().set(true);

        Button createButton = new Button("Create");
        createButton.disableProperty().bind(nameField.textProperty().isEqualTo(""));
        createButton.setOnAction(event -> {
            ModifiableNode newNode = new SimpleNode(nameField.getText());
            //TODO : find a way to generate new ID's (that must be unique)
            HelperMethods.setUniqueId(newNode, uiState.graphState().getGraph());
            uiState.graphState().addNode(newNode);
            popup.close();
        });

        VBox searchBox = new VBox(nameField, createButton);

        Scene littleScene = new Scene(searchBox);
        popup.setScene(littleScene);
        popup.show();
    }

    public static void printAllSelected(ListView<ModifiableNode> l1,
                                        ListView<ModifiableNode> l2,
                                        ListView<ModifiableNode> l3) {
        System.out.println(
                "L1 : " + l1.getSelectionModel().getSelectedItem() + ", " +
                "L2 : " + l2.getSelectionModel().getSelectedItem() + ", " +
                "L3 : " + l3.getSelectionModel().getSelectedItem() + ", "
        );
        System.out.println("L1 : " + l1.isPressed() + ", " + "L2 : " + l2.isPressed() + ", L3 : " + l3.isPressed());
    }

        /*private static void searchPopupWindow(UIState uiState) {
        Stage searchWindow = new Stage(StageStyle.UTILITY);
        searchWindow.initModality(Modality.WINDOW_MODAL);

        TextField searchField = new TextField();
        searchField.editableProperty().set(true);

        ObservableList<ModifiableNode> results = FXCollections.observableArrayList();
        ListView<ModifiableNode> resultsView = new ListView<>(results);
        resultsView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        searchField.textProperty().addListener(event -> {
            results.setAll(KeySearch.search(uiState.graphState().getGraph(), searchField.textProperty().get()));
        });

        Button chooseButton = new Button("Select");
        chooseButton.setOnAction(event ->
                addLink(resultsView.getSelectionModel().getSelectedItem(), uiState));

        VBox searchBox = new VBox(searchField, resultsView, chooseButton);

        Scene littleScene = new Scene(searchBox);
        searchWindow.setScene(littleScene);
        searchWindow.show();
    }*/
}
