package gui;

import bot_parameters.account.RunescapeAccount;
import bot_parameters.configuration.Configuration;
import bot_parameters.proxy.Proxy;
import bot_parameters.script.Script;
import file_manager.PropertiesFileManager;
import file_manager.SettingsFileManager;
import gui.dialogues.input_dialog.ConfigurationDialog;
import gui.dialogues.input_dialog.ProxyDialog;
import gui.dialogues.input_dialog.RunescapeAccountDialog;
import gui.dialogues.input_dialog.ScriptDialog;
import gui.tabs.*;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ManagerPane extends BorderPane {

    public ManagerPane() {

        BotSettingsTab botSettingsTab = new BotSettingsTab();

        TableTab<RunescapeAccount> runescapeAccountTab = new RunescapeAccountTab();
        TableTab<Script> scriptTab = new ScriptTab();
        TableTab<Proxy> proxyTab = new ProxyTab();
        TableTab<Configuration> runTab = new ConfigurationTab(runescapeAccountTab.getTableView().getItems(), scriptTab.getTableView().getItems(), proxyTab.getTableView().getItems(), botSettingsTab);

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.getTabs().addAll(runTab, scriptTab, runescapeAccountTab, proxyTab, botSettingsTab);
        setCenter(tabPane);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(event -> {
            List<Serializable> objects = new ArrayList<>();
            objects.addAll(runescapeAccountTab.getTableView().getItems());
            objects.addAll(proxyTab.getTableView().getItems());
            objects.addAll(scriptTab.getTableView().getItems());
            objects.addAll(runTab.getTableView().getItems());
            SettingsFileManager.saveSettings(objects);
            PropertiesFileManager.setOSBotProperties(botSettingsTab.getBot().getOsbotPath(),
                    botSettingsTab.getOsBotAccount().getUsername(),
                    botSettingsTab.getOsBotAccount().getPassword());
        });

        Button loadButton = new Button("Load");
        loadButton.setOnAction(event -> {
            runescapeAccountTab.getTableView().getItems().clear();
            proxyTab.getTableView().getItems().clear();
            scriptTab.getTableView().getItems().clear();
            runTab.getTableView().getItems().clear();
            for (final Object object : SettingsFileManager.loadSettings()) {
                if (object instanceof RunescapeAccount) {
                    runescapeAccountTab.getTableView().getItems().add((RunescapeAccount) object);
                } else if (object instanceof Proxy) {
                    proxyTab.getTableView().getItems().add((Proxy) object);
                } else if (object instanceof Script) {
                    scriptTab.getTableView().getItems().add((Script) object);
                } else if (object instanceof Configuration) {
                    runTab.getTableView().getItems().add((Configuration) object);
                }
            }
        });

        ToolBar toolBar = new ToolBar();

        toolBar.getItems().add(saveButton);
        toolBar.getItems().add(loadButton);

        setTop(toolBar);

        PropertiesFileManager.getOSBotProperties().ifPresent(properties -> {
            if(properties.containsKey("path")) {
                botSettingsTab.setOsbotPath(properties.getProperty("path"));
            }
            if(properties.containsKey("username")) {
                botSettingsTab.setOsbotUsername(properties.getProperty("username"));
            }
            if(properties.containsKey("password")) {
                botSettingsTab.setOsbotPassword(properties.getProperty("password"));
            }
        });
    }
}
