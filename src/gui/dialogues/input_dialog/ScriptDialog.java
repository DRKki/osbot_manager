package gui.dialogues.input_dialog;

import bot_parameters.script.Script;
import bot_parameters.script.ScriptType;
import file_manager.LocalScriptLoader;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public final class ScriptDialog extends InputDialog<Script> {

    private final TextField scriptIdentifierField;
    private final TextField scriptParameters;
    private final ChoiceBox<ScriptType> scriptTypeSelector;
    private final ChoiceBox<Script> localScripts;
    private final Text invalidScriptIDMessage;

    public ScriptDialog() {

        setHeaderText("Add A Script");

        scriptTypeSelector = new ChoiceBox<>(FXCollections.observableArrayList(ScriptType.values()));
        scriptTypeSelector.getSelectionModel().select(ScriptType.LOCAL);

        localScripts = new ChoiceBox<>(FXCollections.observableArrayList(new LocalScriptLoader().getLocalScripts()));
        localScripts.getSelectionModel().select(0);

        scriptIdentifierField = new TextField();
        scriptIdentifierField.setPromptText("Script ID");
        invalidScriptIDMessage = new Text("SDN script id must only consist of numbers");
        invalidScriptIDMessage.setFill(Color.RED);

        scriptParameters = new TextField();
        scriptParameters.setPromptText("Parameters:");

        grid.add(scriptTypeSelector, 0, 0);
        grid.add(localScripts, 1, 0);

        if(!localScripts.getItems().isEmpty()) okButton.setDisable(false);

        scriptTypeSelector.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue == newValue) return;

            if(newValue == ScriptType.SDN) {
                showSDNScriptField();
                if (!validateSDNScriptID()) okButton.setDisable(true);
            } else {
                showLocalScriptSelector();
            }
        });

        grid.add(new Label("Script parameters:"), 0, 1);
        grid.add(scriptParameters, 1, 1);

        scriptIdentifierField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!validateSDNScriptID()) {
                scriptIdentifierField.setStyle("-fx-text-fill: red;");
                okButton.setDisable(true);
            } else {
                scriptIdentifierField.setStyle("-fx-text-fill: black;");
                okButton.setDisable(false);
            }
        });

        Platform.runLater(localScripts::requestFocus);
    }

    private void showSDNScriptField() {
        grid.getChildren().remove(localScripts);
        grid.add(scriptIdentifierField, 1, 0);
        okButton.setDisable(scriptIdentifierField.getText().trim().isEmpty());
    }

    private void showLocalScriptSelector() {
        grid.getChildren().remove(scriptIdentifierField);
        grid.add(localScripts, 1, 0);
        okButton.setDisable(false);
    }

    private boolean validateSDNScriptID() {
        return scriptIdentifierField.getText().trim().matches("\\d+");
    }

    @Override
    protected final void setValues(final Script existingItem) {
        if(existingItem == null) {
            scriptIdentifierField.setText("");
            scriptParameters.setText("");
            okButton.setDisable(true);
            return;
        }
        if(existingItem.isLocal()) {
            if(scriptTypeSelector.getValue() != ScriptType.LOCAL) {
                scriptTypeSelector.setValue(ScriptType.LOCAL);
            }
            for(final Script localScript : localScripts.getItems()) {
                if(localScript.getScriptIdentifier().equals(existingItem.getScriptIdentifier())) {
                    localScripts.getSelectionModel().select(localScript);
                    break;
                }
            }
            okButton.setDisable(false);
        } else {
            if(scriptTypeSelector.getValue() != ScriptType.SDN) {
                scriptTypeSelector.setValue(ScriptType.SDN);
            }
            scriptIdentifierField.setText(existingItem.getScriptIdentifier().trim());
            if (!validateSDNScriptID()) okButton.setDisable(true);
        }
        scriptParameters.setText(existingItem.getParameters().trim());
    }

    @Override
    protected final Script onAdd() {
        String scriptParametersText = scriptParameters.getText().trim();
        if(scriptParametersText.isEmpty()) scriptParametersText = "none";
        if(scriptTypeSelector.getValue() == ScriptType.LOCAL) {
            return new Script(localScripts.getValue().getScriptIdentifier(), scriptParametersText, true);
        }
        return new Script(scriptIdentifierField.getText().trim(), scriptParametersText, false);
    }

    @Override
    protected final Script onEdit(Script existingItem) {
        if(scriptTypeSelector.getValue() == ScriptType.LOCAL) {
            existingItem.setScriptIdentifier(localScripts.getValue().getScriptIdentifier());
            existingItem.setIsLocal(true);
        } else {
            existingItem.setScriptIdentifier(scriptIdentifierField.getText());
            existingItem.setIsLocal(false);
        }
        String scriptParametersText = scriptParameters.getText().trim();
        if(scriptParametersText.isEmpty()) scriptParametersText = "none";
        existingItem.setParameters(scriptParametersText);
        return existingItem;
    }
}
