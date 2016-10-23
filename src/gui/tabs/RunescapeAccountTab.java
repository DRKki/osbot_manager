package gui.tabs;

import bot_parameters.account.RunescapeAccount;
import gui.dialogues.input_dialog.RunescapeAccountDialog;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class RunescapeAccountTab extends TableTab<RunescapeAccount> {

    public RunescapeAccountTab() {
        super("Runescape Accounts", "No accounts found.", new RunescapeAccountDialog());

        TableColumn<RunescapeAccount, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<RunescapeAccount, String> passwordCol = new TableColumn<>("Password");
        passwordCol.setCellFactory(param -> new PasswordFieldCell());
        passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));

        TableColumn<RunescapeAccount, Integer> pinCol = new TableColumn<>("Bank Pin");
        pinCol.setCellValueFactory(new PropertyValueFactory<>("pin"));

        getTableView().getColumns().addAll(usernameCol, passwordCol, pinCol);
    }
}
