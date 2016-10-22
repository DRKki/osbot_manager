package gui.tabs;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import gui.dialogues.input_dialog.InputDialog;

public class ListTab<T> extends Tab {

    private final ListView<T> listView;
    private final ButtonBar buttonBar;
    private final InputDialog<T> inputDialog;

    public ListTab(final String text, final String placeholder, final InputDialog<T> inputDialog) {

        this.inputDialog = inputDialog;

        setText(text);

        BorderPane borderPane = new BorderPane();

        listView = new ListView<>();
        listView.setPlaceholder(new Label(placeholder));
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        borderPane.setCenter(listView);

        buttonBar = new ButtonBar();
        buttonBar.setPadding(new Insets(10, 10, 10, 10));
        BorderPane.setAlignment(buttonBar, Pos.CENTER);
        borderPane.setBottom(buttonBar);

        Button addButton = new Button("Add");
        addButton.setOnAction(e -> onAdd());
        buttonBar.getButtons().add(addButton);

        Button editButton = new Button("Edit");
        editButton.setOnAction(e -> onEdit());
        buttonBar.getButtons().add(editButton);

        Button removeButton = new Button("Remove");
        removeButton.setOnAction(e -> onRemove());
        buttonBar.getButtons().add(removeButton);

        AnchorPane.setBottomAnchor(borderPane, 0.0);
        AnchorPane.setRightAnchor(borderPane, 0.0);
        AnchorPane.setLeftAnchor(borderPane, 0.0);
        AnchorPane.setTopAnchor(borderPane, 0.0);
        AnchorPane anchorPane = new AnchorPane(borderPane);

        setContent(anchorPane);
    }

    public final ListView<T> getListView() {
        return listView;
    }

    final ButtonBar getButtonBar() {
        return buttonBar;
    }

    private void onAdd() {
        inputDialog.setExistingItem(null);
        inputDialog.showAndWait().ifPresent(item -> listView.getItems().add(item));
    }

    private void onEdit() {
        final int selectedIndex = listView.getSelectionModel().getSelectedIndex();
        if(selectedIndex > -1) {
            inputDialog.setExistingItem(listView.getSelectionModel().getSelectedItem());
            inputDialog.showAndWait().ifPresent(editedItem -> listView.refresh());
        }
    }

    private void onRemove() {
        listView.getItems().removeAll(listView.getSelectionModel().getSelectedItems());
    }
}
