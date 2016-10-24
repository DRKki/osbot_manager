package bot_parameters.bot;

import bot_parameters.interfaces.BotParameter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public final class Bot implements BotParameter {

    private String osbotPath;

    public final String getOsbotPath() {
        return osbotPath;
    }

    public final void setOsbotPath(final String osbotPath) {
        this.osbotPath = osbotPath;
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.writeObject(getOsbotPath());
    }

    private void readObject(ObjectInputStream stream) throws ClassNotFoundException, IOException {
        setOsbotPath((String) stream.readObject());
    }

    @Override
    public String toParameterString() {
        return "java -jar " + osbotPath;
    }
}
