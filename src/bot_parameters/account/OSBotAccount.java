package bot_parameters.account;

import bot_parameters.interfaces.BotParameter;

public final class OSBotAccount extends Account implements BotParameter {

    private static final long serialVersionUID = 3206668253057580659L;

    public OSBotAccount(final String username, final String password) {
        super(username, password);
    }

    @Override
    public final String toParameterString() {
        return String.format("-login %s:%s", getUsername(), getPassword());
    }
}
