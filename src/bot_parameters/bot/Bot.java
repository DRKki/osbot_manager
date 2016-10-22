package bot_parameters.bot;

import bot_parameters.interfaces.BotParameter;

public final class Bot implements BotParameter {

    private String osbotPath;

    public final String getOsbotPath() {
        return osbotPath;
    }

    public final void setOsbotPath(final String osbotPath) {
        this.osbotPath = osbotPath;
    }

    @Override
    public String toParameterString() {
        return "java -jar " + osbotPath;
    }
}
