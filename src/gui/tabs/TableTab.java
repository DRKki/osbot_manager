package gui.tabs;

import gui.dialogues.input_dialog.InputDialog;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class TableTab<T> extends Tab {

    private final TableView<T> tableView;
    private final ButtonBar buttonBar;
    private final InputDialog<T> inputDialog;

    TableTab(final String text, final String placeholder, final InputDialog<T> inputDialog) {

        this.inputDialog = inputDialog;

        setText(text);

        BorderPane borderPane = new BorderPane();

        tableView = new TableView<>();
        tableView.setStyle(".table-view .column-header .label{ -fx-alignment:CENTER; }");
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

        borderPane.setCenter(tableView);

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

    public final TableView<T> getTableView() {
        return tableView;
    }

    final ButtonBar getButtonBar() {
        return buttonBar;
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

        private final PasswordField passwordField;

        PasswordFieldCell() {
            passwordField = new PasswordField();
            passwordField.setEditable(false);
            passwordField.setStyle(
                    "-fx-background-color: -fx-control-inner-background;\n" +
                    "-fx-background-insets: 0;\n" +
                    "-fx-padding: 0 0 0 0;\n" +
                    "-fx-faint-focus-color: transparent;\n" +
                    "-fx-text-box-border: transparent;\n" +
                    "-fx-border-radius:0;\n"
            );

            this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            this.setGraphic(null);
        }

        @Override
        protected void updateItem(final String item, final boolean empty) {
            super.updateItem(item, empty);
            if(!isEmpty()){
                passwordField.setText(item);
                setGraphic(passwordField);
            } else{
                setGraphic(null);
            }
        }
    }
}
