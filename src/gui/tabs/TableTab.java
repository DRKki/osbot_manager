package gui.tabs;

import gui.dialogues.input_dialog.InputDialog;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;

public class TableTab<T> extends Tab {

    private final TableView<T> tableView;
    final ToolBar toolBar;
    final ContextMenu contextMenu;
    private final InputDialog<T> inputDialog;

    TableTab(final String text, final String placeholder, final InputDialog<T> inputDialog) {

        this.inputDialog = inputDialog;

        setText(text);

        BorderPane borderPane = new BorderPane();

        tableView = new TableView<>();
        tableView.setPlaceholder(new Label(placeholder));
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.DELETE) {
                tableView.getItems().remove(tableView.getSelectionModel().getSelectedIndex());
            }
        });
        tableView.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2) {
                onEdit();
            }
        });

        tableView.widthProperty().addListener((source, oldWidth, newWidth) -> {
            Pane header = (Pane) tableView.lookup("TableHeaderRow");
            if (getTableView().getItems().isEmpty()) {
                header.setVisible(false);
            }
        });

        borderPane.setCenter(tableView);

        toolBar = new ToolBar();
        toolBar.setPadding(new Insets(10, 10, 10, 10));

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        toolBar.getItems().add(spacer);

        Button addButton = new Button("Add");
        addButton.setOnAction(e -> onAdd());
        toolBar.getItems().add(addButton);

        Button editButton = new Button("Edit");
        editButton.setOnAction(e -> onEdit());
        toolBar.getItems().add(editButton);

        Button removeButton = new Button("Remove");
        removeButton.setOnAction(e -> onRemove());
        toolBar.getItems().add(removeButton);

        borderPane.setBottom(toolBar);

        AnchorPane.setBottomAnchor(borderPane, 0.0);
        AnchorPane.setRightAnchor(borderPane, 0.0);
        AnchorPane.setLeftAnchor(borderPane, 0.0);
        AnchorPane.setTopAnchor(borderPane, 0.0);
        AnchorPane anchorPane = new AnchorPane(borderPane);

        setContent(anchorPane);

        getTableView().getItems().addListener(new ListChangeListener<T>() {
            @Override
            public void onChanged(Change<? extends T> c) {
                if (getTableView().getItems().isEmpty()) {
                    getTableView().lookup("TableHeaderRow").setVisible(false);
                } else {
                    getTableView().lookup("TableHeaderRow").setVisible(true);
                }
            }
        });

        contextMenu = new ContextMenu();

        MenuItem addOption = new MenuItem("Add");
        addOption.setOnAction(e -> onAdd());
        contextMenu.getItems().add(addOption);

        MenuItem editOption = new MenuItem("Edit");
        editOption.setOnAction(e -> onEdit());
        contextMenu.getItems().add(editOption);

        MenuItem removeOption = new MenuItem("Remove");
        removeOption.setOnAction(e -> onRemove());
        contextMenu.getItems().add(removeOption);

        getTableView().contextMenuProperty().set(contextMenu);
    }

    public final TableView<T> getTableView() {
        return tableView;
    }

    private void onAdd() {
        inputDialog.setExistingItem(null);
        inputDialog.showAndWait().ifPresent(item -> tableView.getItems().add(item));
    }

    private void onEdit() {
        final int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
        if(selectedIndex > -1) {
            inputDialog.setExistingItem(tableView.getSelectionModel().getSelectedItem());
            inputDialog.showAndWait().ifPresent(editedItem -> tableView.refresh());
        }
    }

    private void onRemove() {
        tableView.getItems().removeAll(tableView.getSelectionModel().getSelectedItems());
    }

    final class PasswordFieldCell extends TableCell<T, String> {

        @Override
        protected void updateItem(final String item, final boolean empty) {
            super.updateItem(item, empty);
            if(item != null && !item.isEmpty()){
                setText(new String(new char[item.length()]).replace("\0", "*"));
            } else{
                setText(null);
            }
        }
    }
}
