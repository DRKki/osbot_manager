package gui.dialogues.input_dialog;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import bot_parameters.script.Script;

public final class ScriptDialog extends InputDialog<Script> {

    private final TextField scriptIdentifierField;
    private final TextField scriptParameters;

    public ScriptDialog() {

        setHeaderText("Add A Script");

        scriptIdentifierField = new TextField();
        scriptIdentifierField.setPromptText("Script id / name");

        scriptParameters = new TextField();
        scriptParameters.setPromptText("Parameters:");

        grid.add(new Label("Script Identifier:"), 0, 0);
        grid.add(scriptIdentifierField, 1, 0);

        grid.add(new Label("Script parameters:"), 0, 1);
        grid.add(scriptParameters, 1, 1);

        scriptIdentifierField.textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(scriptIdentifierField.getText().trim().isEmpty());
        });

        Platform.runLater(scriptIdentifierField::requestFocus);
    }

    @Override
    protected final void setValues(final Script existingItem) {
        if(existingItem == null) {
            scriptIdentifierField.setText("");
            scriptParameters.setText("");
            return;
        }
        scriptIdentifierField.setText(existingItem.getScriptIdentifier());
        scriptParameters.setText(existingItem.getParameters());
    }

    @Override
    protected final Script onAdd() {
        String sciptParametersText = scriptParameters.getText().trim();
        if(sciptParametersText.isEmpty()) sciptParametersText = "none";
        return new Script(scriptIdentifierField.getText().trim(), sciptParametersText);
    }

    @Override
    protected final Script onEdit(Script existingItem) {
        existingItem.setScriptIdentifier(scriptIdentifierField.getText());
        existingItem.setParameters(scriptParameters.getText());
        return existingItem;
    }
}
