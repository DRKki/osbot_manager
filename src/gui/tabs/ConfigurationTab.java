package gui.tabs;

import bot_parameters.account.RunescapeAccount;
import bot_parameters.configuration.Configuration;
import bot_parameters.configuration.WorldType;
import bot_parameters.proxy.Proxy;
import bot_parameters.script.Script;
import gui.dialogues.error_dialog.ExceptionDialog;
import gui.dialogues.input_dialog.ConfigurationDialog;
import gui.dialogues.input_dialog.ProxyDialog;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import script_executor.ScriptExecutor;

public class ConfigurationTab extends TableTab<Configuration> {

    private final BotSettingsTab botSettingsTab;

    public ConfigurationTab(final ObservableList<RunescapeAccount> runescapeAccounts, final ObservableList<Script> scripts, final ObservableList<Proxy> proxies,  final BotSettingsTab botSettingsTab) {
        super("Configurations", "No configurations found.", new ConfigurationDialog(runescapeAccounts, scripts, proxies));

        this.botSettingsTab = botSettingsTab;

        Button startButton = new Button("Start");
        startButton.setMnemonicParsing(false);
        startButton.setOnAction(e -> start());
        getButtonBar().getButtons().add(startButton);

        Button startAllButton = new Button("Start All");
        startAllButton.setMnemonicParsing(false);
        startAllButton.setOnAction(e -> startAll());
        getButtonBar().getButtons().add(startAllButton);

        TableColumn<Configuration, Script> scriptCol = new TableColumn<>("Script");
        scriptCol.setCellValueFactory(new PropertyValueFactory<>("script"));

        TableColumn<Configuration, RunescapeAccount> accountCol = new TableColumn<>("Account");
        accountCol.setCellValueFactory(new PropertyValueFactory<>("runescapeAccount"));

        TableColumn<Configuration, WorldType> worldTypeCol = new TableColumn<>("World Type");
        worldTypeCol.setCellValueFactory(new PropertyValueFactory<>("worldType"));

        TableColumn<Configuration, Boolean> randomWorldCol = new TableColumn<>("Random World");
        randomWorldCol.setCellValueFactory(new PropertyValueFactory<>("randomizeWorld"));

        TableColumn<Configuration, Integer> worldCol = new TableColumn<>("World");
        worldCol.setCellValueFactory(new PropertyValueFactory<>("world"));

        TableColumn<Configuration, Proxy> proxyCol = new TableColumn<>("Proxy");
        proxyCol.setCellValueFactory(new PropertyValueFactory<>("proxy"));

        TableColumn<Configuration, Integer> memoryCol = new TableColumn<>("Memory");
        memoryCol.setCellValueFactory(new PropertyValueFactory<>("memoryAllocation"));

        TableColumn<Configuration, Boolean> collectDataCol = new TableColumn<>("Collect Data");
        collectDataCol.setCellValueFactory(new PropertyValueFactory<>("collectData"));

        TableColumn<Configuration, Boolean> debugModeCol = new TableColumn<>("Debug Mode");
        debugModeCol.setCellValueFactory(new PropertyValueFactory<>("debugMode"));

        TableColumn<Configuration, Integer> debugPortCol = new TableColumn<>("Debug Port");
        debugPortCol.setCellValueFactory(new PropertyValueFactory<>("debugPort"));

        TableColumn<Configuration, Boolean> lowCpuCol = new TableColumn<>("Low CPU");
        lowCpuCol.setCellValueFactory(new PropertyValueFactory<>("lowCpuMode"));

        TableColumn<Configuration, Boolean> lowResCol = new TableColumn<>("Low Resource");
        lowResCol.setCellValueFactory(new PropertyValueFactory<>("lowResourceMode"));

        TableColumn<Configuration, Boolean> reflectionCol = new TableColumn<>("Reflection");
        reflectionCol.setCellValueFactory(new PropertyValueFactory<>("reflection"));

        TableColumn<Configuration, Boolean> noRandomsCol = new TableColumn<>("No Randoms");
        noRandomsCol.setCellValueFactory(new PropertyValueFactory<>("noRandoms"));

        getTableView().getColumns().addAll(scriptCol, accountCol, worldTypeCol, randomWorldCol, worldCol, proxyCol, memoryCol, collectDataCol, debugModeCol, debugPortCol, lowCpuCol, lowResCol, reflectionCol, noRandomsCol);
    }

    private void start() {
        getTableView().getSelectionModel().getSelectedItems().forEach(this::onStart);
    }

    private void startAll() {
        getTableView().getItems().forEach(this::onStart);
    }

    private void onStart(final Configuration item) {
        try {
            ScriptExecutor.execute(botSettingsTab.getBot().getOsbotPath(), botSettingsTab.getOsBotAccount(), item);
        } catch (Exception e) {
            new ExceptionDialog(e).showAndWait();
        }
    }
}
