package bot_parameters.proxy;

import bot_parameters.interfaces.BotParameter;

import java.io.Serializable;

public class Proxy implements BotParameter, Serializable {

    private String ip;
    private int port;

    public Proxy(final String ip, final int port) {
        this.ip = ip;
        this.port = port;
    }

    public final String getIP() {
        return ip;
    }

    public final int getPort() {
        return port;
    }

    public final void setIP(final String ip) {
        this.ip = ip;
    }

    public final void setPort(final int port) { this.port = port; }

    @Override
    public String toParameterString() {
        return String.format("-bot_parameters.proxy %s:%d", ip, port);
    }

    @Override
    public String toString() {
        return ip + ":" + port;
    }
}
