package bot_parameters.proxy;

import bot_parameters.interfaces.BotParameter;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;

public class Proxy implements BotParameter, Serializable {

    private SimpleStringProperty ipAddress;
    private SimpleIntegerProperty port;

    public Proxy(final String ipAddress, final int port) {
        this.ipAddress = new SimpleStringProperty(ipAddress);
        this.port = new SimpleIntegerProperty(port);
    }

    public final String getIpAddress() {
        return ipAddress.get();
    }

    public final int getPort() {
        return port.get();
    }

    public final void setIP(final String ip) {
        this.ipAddress.set(ip);
    }

    public final void setPort(final int port) { this.port.set(port); }

    @Override
    public String toParameterString() {
        return String.format("-bot_parameters.proxy %s:%d", ipAddress.get(), port.get());
    }

    @Override
    public String toString() {
        return ipAddress.get() + ":" + port.get();
    }
}
