package ch.talos.gui;

import ch.talos.model.Node;
import com.sun.javafx.geom.BaseBounds;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

import java.util.ArrayList;
import java.util.List;

public final class GraphViewCreator {
    public static HBox graphView(UIState uiState) {
        HBox box = new HBox();

        ObservableList<? extends Node> siblingsList = uiState.siblings();
        ObservableList<? extends Node> childrenList = uiState.children();
        ObservableList<? extends Node> parentsList = uiState.parents();

        ListView siblings = new ListView(siblingsList);
        ListView children = new ListView(childrenList);
        ListView parents = new ListView(parentsList);

        Label siblingsLabel = new Label("Siblings");
        Label childrenLabel = new Label("Children");
        Label parentsLabel = new Label("Parents");

        VBox siblingsBox = new VBox(siblingsLabel, siblings);
        VBox childrenBox = new VBox(childrenLabel, children);
        VBox parentsBox = new VBox(parentsLabel, parents);

        Button nodeButton = new Button();
        nodeButton.textProperty().bind(uiState.focusNodeProperty().asString());
        Text text = new Text();
        text.textProperty().bind(uiState.focusNodeTextProperty());

        TextField urlField = new TextField(uiState.getFocusNode().url());
        urlField.setEditable(false);
        text.setWrappingWidth(400);

        box.getChildren().add(siblingsBox);
        box.getChildren().add(childrenBox);
        box.getChildren().add(parentsBox);

        Separator sep = new Separator();
        Separator sep2 = new Separator();

        VBox vbox = new VBox();
        vbox.getChildren().add(nodeButton);
        vbox.getChildren().add(sep);
        vbox.getChildren().add(text);
        vbox.getChildren().add(sep2);
        vbox.getChildren().add(urlField);

        box.getChildren().add(vbox);

        return box;
    }
}
