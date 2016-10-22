package bot_parameters.account;

import bot_parameters.interfaces.BotParameter;

public final class OSBotAccount extends Account implements BotParameter {

    @Override
    public final String toParameterString() {
        return String.format("-login %s:%s", getUsername(), getPassword());
    }
}
