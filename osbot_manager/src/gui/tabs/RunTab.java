package gui.tabs;

import bot_parameters.configuration.Configuration;
import exceptions.ClientOutOfDateException;
import exceptions.IncorrectLoginException;
import exceptions.MissingWebWalkDataException;
import gui.dialogues.error_dialog.ExceptionDialog;
import javafx.scene.control.Button;
import gui.dialogues.input_dialog.InputDialog;
import script_executor.ScriptExecutor;

public class RunTab extends ListTab<Configuration> {

    private final BotSettingsTab botSettingsTab;

    public RunTab(final String text, final String placeholder, final InputDialog<Configuration> addDialog, final BotSettingsTab botSettingsTab) {

        super(text, placeholder, addDialog);

        this.botSettingsTab = botSettingsTab;

        Button startButton = new Button("Start");
        startButton.setMnemonicParsing(false);
        startButton.setOnAction(e -> start());
        getButtonBar().getButtons().add(startButton);

        Button startAllButton = new Button("Start All");
        startAllButton.setMnemonicParsing(false);
        startAllButton.setOnAction(e -> startAll());
        getButtonBar().getButtons().add(startAllButton);
    }

    private void start() {
        getListView().getSelectionModel().getSelectedItems().forEach(this::onStart);
    }

    private void startAll() {
        getListView().getItems().forEach(this::onStart);
    }

    private void onStart(final Configuration item) {
        try {
            ScriptExecutor.execute(botSettingsTab.getBot().getOsbotPath(), botSettingsTab.getOsBotAccount(), item);
        } catch (Exception e) {
            new ExceptionDialog(e).showAndWait();
        }
    }
}
