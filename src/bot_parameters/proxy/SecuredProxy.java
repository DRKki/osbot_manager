package bot_parameters.proxy;

public class SecuredProxy extends Proxy {

    private String username, password;

    public SecuredProxy(final String ip, final int port, final String username, final String password) {
        super(ip, port);
        this.username = username;
        this.password = password;
    }

    public final String getUsername() {
        return username;
    }

    public final String getPassword() {
        return password;
    }

    public final void setUsername(final String username) {
        this.username = username;
    }

    public final void setPassword(final String password) {
        this.password = password;
    }

    @Override
    public final String toParameterString() {
        return String.format("-bot_parameters.proxy %s:%d:%s:%s", getIpAddress(), getPort(), username, password);
    }

    @Override
    public final String toString() {
        return getIpAddress() + ":" + getPort() + ":" + username;
    }
}
