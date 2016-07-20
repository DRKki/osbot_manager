package bot_parameters.script;

import bot_parameters.interfaces.BotParameter;

import java.io.Serializable;

public final class Script implements BotParameter, Serializable {

    private String scriptIdentifier;
    private String parameters;

    public Script(final String scriptIdentifier, final String parameters) {
        this.scriptIdentifier = scriptIdentifier;
        this.parameters = parameters;
    }

    public final String getScriptIdentifier() {
        return scriptIdentifier;
    }

    public final String getParameters() {
        return parameters;
    }

    public final void setScriptIdentifier(final String scriptIdentifier) {
        this.scriptIdentifier = scriptIdentifier;
    }

    public final void setParameters(final String parameters) {
        this.parameters = parameters;
    }

    @Override
    public final String toParameterString() {
        return String.format("-script %s:%s", scriptIdentifier, parameters);
    }

    @Override
    public final String toString() {
        return scriptIdentifier + ":" + parameters;
    }
}
