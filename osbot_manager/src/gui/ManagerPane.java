package gui;

import bot_parameters.account.RunescapeAccount;
import bot_parameters.configuration.Configuration;
import bot_parameters.proxy.Proxy;
import bot_parameters.script.Script;
import exceptions.ClientOutOfDateException;
import exceptions.MissingWebWalkDataException;
import file_manager.PropertiesFileManager;
import file_manager.SettingsFileManager;
import gui.dialogues.error_dialog.ExceptionDialog;
import gui.dialogues.input_dialog.ConfigurationDialog;
import gui.dialogues.input_dialog.ProxyDialog;
import gui.dialogues.input_dialog.RunescapeAccountDialog;
import gui.dialogues.input_dialog.ScriptDialog;
import gui.tabs.BotSettingsTab;
import gui.tabs.ListTab;
import gui.tabs.RunTab;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import script_executor.ScriptExecutor;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class ManagerPane extends BorderPane {

    public ManagerPane() {

        BotSettingsTab botSettingsTab = new BotSettingsTab();

        ListTab<RunescapeAccount> runescapeAccountTab = new ListTab<>("Runescape Accounts", "No accounts found.", new RunescapeAccountDialog());
        ListTab<Proxy> proxyTab = new ListTab<>("Proxies", "No proxies found.", new ProxyDialog());
        ListTab<Script> scriptTab = new ListTab<>("Scripts", "No scripts found.", new ScriptDialog());
        ListTab<Configuration> runTab = new RunTab("Configurations", "No configurations found.", new ConfigurationDialog(runescapeAccountTab.getListView().getItems(), scriptTab.getListView().getItems(), proxyTab.getListView().getItems()), botSettingsTab);

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.getTabs().addAll(runTab, scriptTab, runescapeAccountTab, proxyTab, botSettingsTab);
        setCenter(tabPane);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(event -> {
            List<Serializable> objects = new ArrayList<>();
            objects.addAll(runescapeAccountTab.getListView().getItems());
            objects.addAll(proxyTab.getListView().getItems());
            objects.addAll(scriptTab.getListView().getItems());
            objects.addAll(runTab.getListView().getItems());
            SettingsFileManager.saveSettings(objects);
            PropertiesFileManager.setOSBotProperties(botSettingsTab.getBot().getOsbotPath(),
                    botSettingsTab.getOsBotAccount().getUsername(),
                    botSettingsTab.getOsBotAccount().getPassword());
        });

        Button loadButton = new Button("Load");
        loadButton.setOnAction(event -> {
            runescapeAccountTab.getListView().getItems().clear();
            proxyTab.getListView().getItems().clear();
            scriptTab.getListView().getItems().clear();
            runTab.getListView().getItems().clear();
            for (final Object object : SettingsFileManager.loadSettings()) {
                if (object instanceof RunescapeAccount) {
                    runescapeAccountTab.getListView().getItems().add((RunescapeAccount) object);
                } else if (object instanceof Proxy) {
                    proxyTab.getListView().getItems().add((Proxy) object);
                } else if (object instanceof Script) {
                    scriptTab.getListView().getItems().add((Script) object);
                } else if (object instanceof Configuration) {
                    runTab.getListView().getItems().add((Configuration) object);
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
