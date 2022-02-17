package ch.talos.gui;

import ch.talos.analytics.KeySearch;
import ch.talos.gui.VisualRepresentation.DrawGraph;
import ch.talos.model.ModifiableNode;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public final class GraphViewCreator {

    /**
     * In theory, that method should take as arguments some eventHandlers, like in tCHu, that would be used when the
     * user perform actions
     * @param uiState
     * @return
     */
    public static Pane graphView(UIState uiState) {
        ListView<ModifiableNode> siblings = new ListView<>(uiState.siblings());
        ListView<ModifiableNode> children = new ListView<>(uiState.children());
        ListView<ModifiableNode> parents = new ListView<>(uiState.parents());
        ListView<ModifiableNode> childrenOfParents = new ListView<>(uiState.childrenOfParents());

        siblings.onMouseClickedProperty().set(buttonEvent(siblings, uiState));
        children.onMouseClickedProperty().set(buttonEvent(children, uiState));
        parents.onMouseClickedProperty().set(buttonEvent(parents, uiState));
        childrenOfParents.onMouseClickedProperty().set(buttonEvent(childrenOfParents, uiState));
//        siblings.onMouseClickedProperty().set(e -> selected = siblings.getSelectionModel().getSelectedItem());
//        siblings.onMouseClickedProperty().set(e -> printAllSelected(siblings, children, parents));


        Label siblingsLabel = new Label("Siblings");
        Label childrenLabel = new Label("Children");
        Label parentsLabel = new Label("Parents");
        Label childrenOfParentsLabel = new Label("ChildrenOfParents");

        VBox siblingsBox = new VBox(siblingsLabel, siblings);
        VBox childrenBox = new VBox(childrenLabel, children);
        VBox parentsBox = new VBox(parentsLabel, parents);
        VBox childrenOfParentsBox = new VBox(childrenOfParentsLabel, childrenOfParents);

        Text focusedNodeName = new Text();
        focusedNodeName.textProperty().bind(uiState.focusNodeProperty().asString());
        focusedNodeName.fontProperty().set(Font.font("Helvetica", 30));

        Text selectedNodeName = new Text();
        selectedNodeName.textProperty().bind(uiState.selectedNodeProperty().asString());
        selectedNodeName.fontProperty().set(Font.font("Helvetica", 20));

        Text text = new Text();
        text.textProperty().bind(uiState.selectedNodeTextProperty());
        text.setWrappingWidth(400);

        TextField urlField = new TextField();
        urlField.textProperty().bind(uiState.selectedNodeUrlProperty());
        urlField.setEditable(false);
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
        drawingButton.setOnAction(event -> new Thread(() ->
                DrawGraph.performDrawing(uiState.graphState(), uiState.getFocusNode())).start());

        HBox listsBox = new HBox();
        listsBox.getChildren().add(parentsBox);
        listsBox.getChildren().add(childrenBox);
        listsBox.getChildren().add(siblingsBox);
        listsBox.getChildren().add(childrenOfParentsBox);

        Separator sep = new Separator();
        Separator sep2 = new Separator();

        VBox vbox = new VBox();
        vbox.getChildren().add(selectedNodeName);
        vbox.getChildren().add(sep);
        vbox.getChildren().add(text);
        vbox.getChildren().add(sep2);
        vbox.getChildren().add(urlField);
        vbox.getChildren().add(urlButton);

        Button b1 = new Button("B1"); Button b2 = new Button("B2");
        b2.translateYProperty().set(150);
        Line line = new Line();
        line.startXProperty().bind(Bindings.add(b1.translateXProperty(), 10));
        line.endXProperty().bind(Bindings.add(b2.translateXProperty(), 10));
        line.startYProperty().bind(Bindings.add(b1.translateYProperty(), 10));
        line.endYProperty().bind(Bindings.add(b2.translateYProperty(), 10));
        Group group = new Group(b1, b2, line);

        VBox listAndName = new VBox(focusedNodeName, new Separator(), listsBox, new Separator(), drawingButton);

        return new HBox(listAndName, vbox, searchBox(uiState), group);
    }

    private static Pane searchBox(UIState uiState) {
        TextField searchField = new TextField();
        searchField.editableProperty().set(true);

        ObservableList<ModifiableNode> results = FXCollections.observableArrayList();
        ListView<ModifiableNode> resultsView = new ListView<>(results);
        resultsView.onMouseClickedProperty().set(buttonEvent(resultsView, uiState));

        searchField.textProperty().addListener(event -> {
            results.setAll(KeySearch.search(uiState.graphState().getGraph(), searchField.textProperty().get()));
        });

        return new VBox(searchField, resultsView);
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
}
