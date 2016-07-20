package bot_parameters.account;

import java.io.Serializable;

class Account implements Serializable {

    private String username, password;

    Account() {}

    Account(final String username, final String password) {
        this.username = username;
        this.password = password;
    }

    public final String getUsername() {
        return username;
    }

    public final void setUsername(String username) {
        this.username = username;
    }

    public final String getPassword() {
        return password;
    }

    public final void setPassword(String password) {
        this.password = password;
    }

    @Override
    public final String toString() {
        return "Username: " + getUsername();
    }
}
