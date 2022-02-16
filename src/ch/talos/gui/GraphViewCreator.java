package ch.talos.gui;

import ch.talos.model.ModifiableNode;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public final class GraphViewCreator {
    public static int switched = 10;
    public static HBox graphView(UIState uiState) {
        HBox box = new HBox();

        ListView<ModifiableNode> siblings = new ListView(uiState.siblings());
        ListView children = new ListView(uiState.children());
        ListView parents = new ListView(uiState.parents());


        Label siblingsLabel = new Label("Siblings");
        Label childrenLabel = new Label("Children");
        Label parentsLabel = new Label("Parents");

        VBox siblingsBox = new VBox(siblingsLabel, siblings);
        VBox childrenBox = new VBox(childrenLabel, children);
        VBox parentsBox = new VBox(parentsLabel, parents);

        Button changeButton = new Button("Go to selected node");
        changeButton.onActionProperty().set(
                e -> uiState.updateFocusNode(siblings.getSelectionModel().getSelectedItem()));

        Button nodeButton = new Button();
        nodeButton.textProperty().bind(uiState.focusNodeProperty().asString());
        Text text = new Text();
        text.textProperty().bind(uiState.focusNodeTextProperty());
        HBox buttons = new HBox(nodeButton, changeButton);

        TextField urlField = new TextField(uiState.getFocusNode().url());
        urlField.setEditable(false);
        text.setWrappingWidth(400);

        box.getChildren().add(siblingsBox);
        box.getChildren().add(childrenBox);
        box.getChildren().add(parentsBox);

        Separator sep = new Separator();
        Separator sep2 = new Separator();

        VBox vbox = new VBox();
        vbox.getChildren().add(buttons);
        vbox.getChildren().add(sep);
        vbox.getChildren().add(text);
        vbox.getChildren().add(sep2);
        vbox.getChildren().add(urlField);

        box.getChildren().add(vbox);

        return box;
    }
}
