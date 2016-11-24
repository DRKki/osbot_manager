package bot_parameters.account;

import bot_parameters.interfaces.BotParameter;
import bot_parameters.interfaces.Copyable;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public final class RunescapeAccount extends Account implements BotParameter, Copyable<RunescapeAccount> {

    private static final long serialVersionUID = 4730705293691635049L;

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

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.writeObject(getUsername());
        stream.writeObject(getPassword());
        stream.writeInt(getPin());
    }

    private void readObject(ObjectInputStream stream) throws ClassNotFoundException, IOException {
        username = new SimpleStringProperty((String) stream.readObject());
        password = new SimpleStringProperty((String) stream.readObject());
        pin = new SimpleIntegerProperty(stream.readInt());
    }

    @Override
    public final String toParameterString() {
        return String.format("-bot %s:%s:%d", getUsername(), getPassword(), pin.get());
    }

    @Override
    public RunescapeAccount createCopy() {
        return new RunescapeAccount(getUsername(), getPassword(), getPin());
    }
}
