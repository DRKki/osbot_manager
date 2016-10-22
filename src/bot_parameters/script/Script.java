package bot_parameters.script;

import bot_parameters.interfaces.BotParameter;

import java.io.Serializable;

public final class Script implements BotParameter, Serializable {

    private String scriptIdentifier;
    private String parameters;
    private boolean isLocal;

    public Script(final String scriptIdentifier, final String parameters, final boolean isLocal) {
        this.scriptIdentifier = scriptIdentifier;
        this.parameters = parameters;
        this.isLocal = isLocal;
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

    public final boolean isLocal() { return isLocal; }

    @Override
    public final String toParameterString() {
        return String.format("-script %s:%s", scriptIdentifier, parameters);
    }

    @Override
    public final String toString() {
        String location = isLocal ? "(Local) " : "(SDN) ";
        return location + scriptIdentifier + " : " + parameters;
    }
}
