package gui.tabs;

import bot_parameters.account.RunescapeAccount;
import bot_parameters.configuration.Configuration;
import bot_parameters.configuration.WorldType;
import bot_parameters.proxy.Proxy;
import bot_parameters.script.Script;
import gui.dialogues.error_dialog.ExceptionDialog;
import gui.dialogues.input_dialog.ConfigurationDialog;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import script_executor.ScriptExecutor;

import java.util.List;
import java.util.Optional;

public class ConfigurationTab extends TableTab<Configuration> {

    private final BotSettingsTab botSettingsTab;
    private final Button startButton, startAllButton;

    public ConfigurationTab(final ObservableList<RunescapeAccount> runescapeAccounts, final ObservableList<Script> scripts, final ObservableList<Proxy> proxies,  final BotSettingsTab botSettingsTab) {
        super("Configurations", "No configurations found.", new ConfigurationDialog(runescapeAccounts, scripts, proxies));

        this.botSettingsTab = botSettingsTab;

        startButton = new Button("Start");
        startButton.setMnemonicParsing(false);
        toolBar.getItems().add(startButton);

        startAllButton = new Button("Start All");
        startAllButton.setMnemonicParsing(false);
        toolBar.getItems().add(startAllButton);

        startButton.setOnAction(e -> {
            startButton.setDisable(true);
            startAllButton.setDisable(true);
            start();
        });

        startAllButton.setOnAction(e -> {
            startAllButton.setDisable(true);
            startButton.setDisable(true);
            startAll();})
        ;

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

        getTableView().setRowFactory(param -> {

            TableRow<Configuration> row = new TableRow<>();

            row.itemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    String rowStyle = row.getStyle();
                    newValue.addRunListener((observable1, oldValue1, newValue1) -> {
                        if (newValue.isRunning()) {
                            row.setStyle(rowStyle + "-fx-background-color: #49E20E;");
                        } else {
                            row.setStyle(rowStyle);
                        }
                    });
                }
            });
            return row;
        });

        Thread processChecker = new Thread(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while(true) {
                    getTableView().getItems()
                                  .stream()
                                  .filter(Configuration::isRunning)
                                  .filter(configuration -> !configuration.getProcess().isAlive())
                                  .forEach(configuration -> {
                                      configuration.setRunning(false);
                                      configuration.setProcess(null);
                    });
                    Thread.sleep(1000);
                }
            }
        });
        processChecker.setDaemon(true);
        processChecker.start();
    }

    private void start() {
        runConfigurations(getTableView().getSelectionModel().getSelectedItems());
    }

    private void startAll() {
        runConfigurations(getTableView().getItems());
    }

    private void runConfigurations(final List<Configuration> configurations) {
        int delay = 0;
        if (configurations.size() > 1) {
            Optional<Integer> userDelayVal = getDelayFromUser();
            if (userDelayVal.isPresent()){
                delay = userDelayVal.get();
            }
        }
        Thread executorThread = new Thread(new ConfigurationExecutor(configurations, delay, () -> {
            startAllButton.setDisable(false);
            startButton.setDisable(false);
        }));
        executorThread.setDaemon(true);
        executorThread.start();
    }

    private Optional<Integer> getDelayFromUser() {
        TextInputDialog delayDialog = new TextInputDialog("5");
        delayDialog.setTitle("Explv's OSBot Manager");
        delayDialog.setHeaderText("Set delay between bot starts");
        delayDialog.setContentText("Enter delay (s):");

        Optional<String> delayText = delayDialog.showAndWait();

        if (delayText.isPresent()) {
            return Optional.of(Integer.parseInt(delayText.get()));
        }
        return Optional.empty();
    }

    private class ConfigurationExecutor extends Task<Void> {

        private final List<Configuration> configurations;
        private final int delay;
        private final ConfigurationExecuteCallback callback;

        ConfigurationExecutor(final List<Configuration> configurations, final int delay, final ConfigurationExecuteCallback callback) {
            this.configurations = configurations;
            this.delay = delay;
            this.callback = callback;
        }

        @Override
        protected Void call() throws Exception {
            for (final Configuration configuration : configurations) {
                try {
                    ScriptExecutor.execute(botSettingsTab.getBot().getOsbotPath(), botSettingsTab.getOsBotAccount(), configuration).ifPresent(process -> {
                        configuration.setProcess(process);
                        configuration.setRunning(true);
                    });
                } catch (Exception e) {
                    new ExceptionDialog(e).showAndWait();
                }
                Thread.sleep(delay);
            }
            callback.onComplete();
            return null;
        }
    }

    private interface ConfigurationExecuteCallback {
        void onComplete();
    }
}
