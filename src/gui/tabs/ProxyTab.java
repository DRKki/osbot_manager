package gui.tabs;

import bot_parameters.proxy.Proxy;
import gui.dialogues.input_dialog.ProxyDialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class ProxyTab extends TableTab<Proxy> {

    public ProxyTab() {
        super("Proxies", "No proxies found.", new ProxyDialog());

        TableColumn<Proxy, String> ipCol = new TableColumn<>("IP Address");
        ipCol.setCellValueFactory(new PropertyValueFactory<>("ipAddress"));

        TableColumn<Proxy, Integer> portCol = new TableColumn<>("Port");
        portCol.setCellValueFactory(new PropertyValueFactory<>("port"));

        TableColumn<Proxy, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<Proxy, String> passwordCol = new TableColumn<>("Password");
        passwordCol.setCellFactory(param -> new PasswordFieldCell());
        passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));

        getTableView().getColumns().addAll(ipCol, portCol, usernameCol, passwordCol);
    }
}
