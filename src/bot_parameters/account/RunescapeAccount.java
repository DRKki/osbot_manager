package bot_parameters.account;

import bot_parameters.interfaces.BotParameter;
import javafx.beans.property.SimpleIntegerProperty;

public final class RunescapeAccount extends Account implements BotParameter {

    private SimpleIntegerProperty pin;

    public RunescapeAccount(final String username, final String password, final int pin) {
        super(username, password);
        this.pin = new SimpleIntegerProperty(pin);
    }

    public final int getPin() {
        return pin.get();
    }

    public final void setPin(final int pin) {
        this.pin = new SimpleIntegerProperty(pin);
    }

    @Override
    public final String toParameterString() {
        return String.format("-bot %s:%s:%d", getUsername(), getPassword(), pin.get());
    }
}
