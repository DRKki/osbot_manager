package gui.dialogues.input_dialog;

import bot_parameters.account.RunescapeAccount;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public final class RunescapeAccountDialog extends InputDialog<RunescapeAccount> {

    private final TextField username;
    private final PasswordField password, bankPin;

    public RunescapeAccountDialog() {

        setHeaderText("Add A Runescape Account");

        username = new TextField();
        username.setPromptText("Username");

        password = new PasswordField();
        password.setPromptText("Password");

        bankPin = new PasswordField();
        bankPin.setPromptText("(Optional) Bank Pin");
        bankPin.setTextFormatter(new TextFormatter<>(change -> change.getText().matches("\\d*") ? change : null));

        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);

        grid.add(new Label("Password:"), 0, 1);
        grid.add(password, 1, 1);

        grid.add(new Label("Bank Pin:"), 0, 2);
        grid.add(bankPin, 1, 2);

        ChangeListener<String> changeListener = (observable, oldValue, newValue) -> {
            okButton.setDisable(username.getText().trim().isEmpty() || password.getText().trim().isEmpty());
        };

        username.textProperty().addListener(changeListener);
        password.textProperty().addListener(changeListener);

        Platform.runLater(username::requestFocus);
    }

    @Override
    protected final void setValues(final RunescapeAccount existingItem) {
        if(existingItem == null) {
            username.setText("");
            password.setText("");
            bankPin.setText("");
            return;
        }
        username.setText(existingItem.getUsername());
        password.setText(existingItem.getPassword());
        bankPin.setText(String.valueOf(existingItem.getPin()));
    }

    @Override
    public final RunescapeAccount onAdd() {
        String bankPinText = bankPin.getText();
        if (bankPinText.isEmpty()) bankPinText = "1234";
        return new RunescapeAccount(username.getText(), password.getText(), Integer.parseInt(bankPinText));
    }

    @Override
    protected final RunescapeAccount onEdit(final RunescapeAccount existingItem) {
        existingItem.setUsername(username.getText());
        existingItem.setPassword(password.getText());
        existingItem.setPin(Integer.parseInt(bankPin.getText()));
        return existingItem;
    }
}
