package bot_parameters.account;

import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;

public class Account implements Serializable {

    private SimpleStringProperty username, password;

    public Account(final String username, final String password) {
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
    }

    public final String getUsername() {
        return username.get();
    }

    public final void setUsername(String username) {
        this.username.set(username);
    }

    public final String getPassword() {
        return password.get();
    }

    public final void setPassword(String password) {
        this.password.set(password);
    }

    @Override
    public final String toString() {
        return getUsername();
    }
}
