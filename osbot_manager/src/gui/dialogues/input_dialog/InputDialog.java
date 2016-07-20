package gui.dialogues.input_dialog;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.GridPane;

public abstract class InputDialog<T> extends Dialog<T> {

    final GridPane grid;
    final Node okButton;
    private T existingItem;

    InputDialog() {
        setTitle("Explv's OSBot Manager");
        grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        getDialogPane().setContent(grid);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        okButton = getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);
        setResultConverter(buttonType -> {
            if(buttonType != ButtonType.OK) return null;
            if(existingItem != null) return onEdit(existingItem);
            return onAdd();
        });
    }

    public final void setExistingItem(final T existingItem) {
        this.existingItem = existingItem;
        setValues(existingItem);
    }

    protected abstract void setValues(final T existingItem);

    protected abstract T onAdd();

    protected abstract T onEdit(final T existingItem);
}
