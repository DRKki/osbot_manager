package gui.dialogues.input_dialog;

import bot_parameters.proxy.Proxy;
import bot_parameters.proxy.SecuredProxy;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public final class ProxyDialog extends InputDialog<Proxy> {

    private final TextField ip, port, username;
    private final PasswordField password;

    public ProxyDialog() {

        setHeaderText("Add A Proxy");

        ip = new TextField();
        ip.setPromptText("IP Address");

        port = new TextField();
        port.setPromptText("Port Number");
        port.setTextFormatter(new TextFormatter<>(change -> change.getText().matches("\\d*") ? change : null));

        username = new TextField();
        username.setPromptText("(Optional) Username");

        password = new PasswordField();
        password.setPromptText("(Optional) Password");

        grid.add(new Label("IP:"), 0, 0);
        grid.add(ip, 1, 0);

        grid.add(new Label("Port:"), 0, 1);
        grid.add(port, 1, 1);

        grid.add(new Label("Username:"), 0, 2);
        grid.add(username, 1, 2);

        grid.add(new Label("Password:"), 0, 3);
        grid.add(password, 1, 3);

        ChangeListener<String> listener = (observable, oldValue, newValue) -> {
            okButton.setDisable(ip.getText().trim().isEmpty() || port.getText().trim().isEmpty());
        };
        ip.textProperty().addListener(listener);
        port.textProperty().addListener(listener);

        Platform.runLater(ip::requestFocus);
    }

    @Override
    protected final void setValues(final Proxy existingItem) {
        if(existingItem == null) {
            ip.setText("");
            port.setText("");
            username.setText("");
            password.setText("");
            return;
        }
        ip.setText(existingItem.getIpAddress());
        port.setText(String.valueOf(existingItem.getPort()));
        if(existingItem instanceof SecuredProxy) {
            SecuredProxy securedProxy = (SecuredProxy) existingItem;
            username.setText(securedProxy.getUsername());
            password.setText(securedProxy.getPassword());
        }
    }

    @Override
    public final Proxy onAdd() {
        if (!username.getText().trim().isEmpty()) {
            return new SecuredProxy(ip.getText(), Integer.parseInt(port.getText()), username.getText(), password.getText());
        }
        return new Proxy(ip.getText(), Integer.parseInt(port.getText()));
    }

    @Override
    protected final Proxy onEdit(final Proxy existingItem) {
        existingItem.setIP(ip.getText());
        existingItem.setPort(Integer.parseInt(ip.getText()));
        if(existingItem instanceof SecuredProxy) {
            if(!username.getText().trim().isEmpty()) {
                SecuredProxy securedProxy = (SecuredProxy) existingItem;
                securedProxy.setUsername(username.getText());
                securedProxy.setPassword(password.getText());
            }
        } else if(!username.getText().trim().isEmpty()) {
            return new SecuredProxy(ip.getText(), Integer.parseInt(port.getText()), username.getText(), password.getText());
        }
        return existingItem;
    }
}
