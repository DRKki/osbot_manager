package bot_parameters.account;

import bot_parameters.interfaces.BotParameter;

public final class RunescapeAccount extends Account implements BotParameter {

    private int pin;

    public RunescapeAccount(final String username, final String password, final int pin) {
        super(username, password);
        this.pin = pin;
    }

    public final int getPin() {
        return pin;
    }

    public final void setPin(final int pin) {
        this.pin = pin;
    }

    @Override
    public final String toParameterString() {
        return String.format("-bot %s:%s:%d", getUsername(), getPassword(), pin);
    }
}
