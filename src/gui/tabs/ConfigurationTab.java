package gui.tabs;

import bot_parameters.account.RunescapeAccount;
import bot_parameters.configuration.Configuration;
import bot_parameters.configuration.WorldType;
import bot_parameters.proxy.Proxy;
import bot_parameters.script.Script;
import gui.ToolbarButton;
import gui.dialogues.error_dialog.ExceptionDialog;
import gui.dialogues.input_dialog.ConfigurationDialog;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public class ConfigurationTab extends TableTab<Configuration> {

    private final BotSettingsTab botSettingsTab;
    private final Button startButton, stopButton;

    public ConfigurationTab(final ObservableList<RunescapeAccount> runescapeAccounts, final ObservableList<Script> scripts, final ObservableList<Proxy> proxies,  final BotSettingsTab botSettingsTab) {
        super("Configurations", "No configurations found.", new ConfigurationDialog(runescapeAccounts, scripts, proxies));

        this.botSettingsTab = botSettingsTab;

        toolBar.getChildren().add(new Separator(Orientation.VERTICAL));

        startButton = new ToolbarButton("Start", "start_icon.png", "start_icon_blue.png");
        toolBar.getChildren().add(startButton);

        stopButton = new ToolbarButton("Stop", "stop_icon.png", "stop_icon_blue.png");
        toolBar.getChildren().add(stopButton);

        startButton.setOnAction(e -> {
            startButton.setDisable(true);
            start();
        });

        stopButton.setOnAction(e -> stop());

        TableColumn<Configuration, ObservableList<Script>> scriptCol = new TableColumn<>("Scripts");
        scriptCol.setCellValueFactory(new PropertyValueFactory<>("scripts"));

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

        TableColumn<Configuration, Boolean> noInterfaceCol = new TableColumn<>("No Interface");
        noInterfaceCol.setCellValueFactory(new PropertyValueFactory<>("noInterface"));

        TableColumn<Configuration, Boolean> noRenderCol = new TableColumn<>("No Render");
        noRenderCol.setCellValueFactory(new PropertyValueFactory<>("noRender"));

        getTableView().getColumns().addAll(scriptCol, accountCol, worldTypeCol, randomWorldCol, worldCol, proxyCol, memoryCol, collectDataCol, debugModeCol, debugPortCol, lowCpuCol, lowResCol, reflectionCol, noRandomsCol, noInterfaceCol, noRenderCol);

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

        MenuItem startOption = new MenuItem("Start");
        startOption.setOnAction(e -> start());
        contextMenu.getItems().add(startOption);

        MenuItem stopOption = new MenuItem("Stop");
        stopOption.setOnAction(e -> stop());
        contextMenu.getItems().add(stopOption);

        MenuItem viewLogOption = new MenuItem("Show log");
        viewLogOption.setOnAction(e -> showLog());
        contextMenu.getItems().add(viewLogOption);
    }

    private void start() {
        runConfigurations(getTableView().getSelectionModel().getSelectedItems());
    }

    private void stop() { getTableView().getSelectionModel().getSelectedItems().forEach(Configuration::stop); }

    private void showLog() {
        Configuration configuration = getTableView().getSelectionModel().getSelectedItem();
        Dialog dialog = new Dialog();
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.setResizable(true);
        dialog.initModality(Modality.NONE);

        TextArea textArea = new TextArea();
        ScrollPane scrollPane = new ScrollPane(textArea);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        AnchorPane.setBottomAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(scrollPane, 0.0);
        AnchorPane.setLeftAnchor(scrollPane, 0.0);
        AnchorPane.setTopAnchor(scrollPane, 0.0);
        AnchorPane anchorPane = new AnchorPane(scrollPane);

        dialog.getDialogPane().setContent(anchorPane);

        dialog.show();

        Thread readFileThread = new Thread(() -> {
            try (BufferedReader br = new BufferedReader(new FileReader(new File(configuration.getLogFileName()).toString()))) {
                String line = null;
                while (dialog.isShowing() && (((line = br.readLine()) != null) || configuration.isRunning())) {
                    if (line != null) {
                        final String updateLine = line;
                        Platform.runLater(() -> textArea.appendText(updateLine + System.lineSeparator()));
                    } else {
                        Thread.sleep(500);
                    }
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        readFileThread.start();
    }

    private void runConfigurations(final List<Configuration> configurations) {
        final int delay = configurations.size() > 1 ? getDelayFromUser() : 0;

        new Thread(() -> {
            for (final Configuration configuration : configurations) {
                try {
                    configuration.run(botSettingsTab.getBot().getOsbotPath(), botSettingsTab.getOsBotAccount());
                    Thread.sleep(delay * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    Platform.runLater(() -> new ExceptionDialog(e).show());
                }
            }
            Platform.runLater(() -> {
                startButton.setDisable(false);
            });
        }).start();
    }

    private Integer getDelayFromUser() {
        TextInputDialog delayDialog = new TextInputDialog("5");
        delayDialog.setTitle("Explv's OSBot Manager");
        delayDialog.setHeaderText("Set delay between bot starts");
        delayDialog.setContentText("Enter delay (s):");

        Optional<String> delayText = delayDialog.showAndWait();

        if (delayText.isPresent()) {
            return Integer.parseInt(delayText.get());
        }
        return 0;
    }
}
