package bot_parameters.script;

import bot_parameters.interfaces.BotParameter;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public final class Script implements BotParameter, Serializable {

    private static final long serialVersionUID = -3697946363287646016L;

    private SimpleStringProperty author;
    private SimpleStringProperty scriptIdentifier;
    private SimpleStringProperty parameters;
    private SimpleBooleanProperty isLocal;

    public Script(final String scriptIdentifier, final String parameters, final boolean isLocal) {
        this.scriptIdentifier = new SimpleStringProperty(scriptIdentifier);
        this.parameters = new SimpleStringProperty(parameters);
        this.isLocal = new SimpleBooleanProperty(isLocal);
    }

    public final String getScriptIdentifier() {
        return scriptIdentifier.get();
    }

    public final String getParameters() {
        return parameters.get();
    }

    public final void setScriptIdentifier(final String scriptIdentifier) {
        this.scriptIdentifier.set(scriptIdentifier);
    }

    public final void setParameters(final String parameters) {
        this.parameters.set(parameters);
    }

    public final boolean isLocal() { return isLocal.get(); }

    public void setIsLocal(final boolean isLocal) { this.isLocal.set(isLocal); }

    @Override
    public final String toParameterString() {
        if (isLocal.get()) return String.format("-script \"\\\"%s\\\":%s\"", scriptIdentifier.get(), parameters.get());
        else return String.format("-script %s:%s", scriptIdentifier.get(), parameters.get());
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.writeObject(scriptIdentifier.get());
        stream.writeObject(parameters.get());
        stream.writeBoolean(isLocal.get());
    }

    private void readObject(ObjectInputStream stream) throws ClassNotFoundException, IOException {
        scriptIdentifier = new SimpleStringProperty((String) stream.readObject());
        parameters = new SimpleStringProperty((String) stream.readObject());
        isLocal = new SimpleBooleanProperty(stream.readBoolean());
    }

    @Override
    public final String toString() {
        return scriptIdentifier.get();
    }
}
