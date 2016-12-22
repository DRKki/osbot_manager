package bot_parameters.configuration;

import bot_parameters.account.RunescapeAccount;
import bot_parameters.interfaces.BotParameter;
import bot_parameters.interfaces.Copyable;
import bot_parameters.proxy.Proxy;
import bot_parameters.script.Script;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public final class Configuration implements BotParameter, Copyable<Configuration>, Serializable {

    private static final long serialVersionUID = 1938451332017337304L;

    private SimpleObjectProperty<RunescapeAccount> runescapeAccount;
    private SimpleObjectProperty<Script> script;
    private SimpleObjectProperty<Proxy> proxy;
    private SimpleIntegerProperty memoryAllocation = new SimpleIntegerProperty(-1);
    private SimpleBooleanProperty collectData = new SimpleBooleanProperty();
    private SimpleBooleanProperty debugMode = new SimpleBooleanProperty();
    private SimpleIntegerProperty debugPort = new SimpleIntegerProperty(-1);
    private SimpleBooleanProperty lowCpuMode = new SimpleBooleanProperty();
    private SimpleBooleanProperty lowResourceMode = new SimpleBooleanProperty();
    private SimpleBooleanProperty reflection = new SimpleBooleanProperty();
    private SimpleBooleanProperty noRandoms = new SimpleBooleanProperty();
    private SimpleBooleanProperty noInterface = new SimpleBooleanProperty();
    private SimpleObjectProperty<WorldType> worldType = new SimpleObjectProperty<>();
    private SimpleIntegerProperty world = new SimpleIntegerProperty(-1);
    private SimpleBooleanProperty randomizeWorld = new SimpleBooleanProperty();
    private SimpleBooleanProperty isRunning = new SimpleBooleanProperty();

    private Process process;

    public Configuration(final RunescapeAccount runescapeAccount, final Script script) {
        this.runescapeAccount = new SimpleObjectProperty<>(runescapeAccount);
        this.script = new SimpleObjectProperty<>(script);
        this.proxy = new SimpleObjectProperty<>(new Proxy("No Proxy", -1));
    }

    public Process getProcess() { return process; }

    public void setProcess(final Process process) { this.process = process; }

    public RunescapeAccount getRunescapeAccount() {
        return runescapeAccount.get();
    }

    public final Script getScript() {
        return script.get();
    }

    public final Proxy getProxy() {
        return proxy.get();
    }

    public final Integer getMemoryAllocation() {
        return memoryAllocation.get();
    }

    public final boolean isCollectData() {
        return collectData.get();
    }

    public final boolean isDebugMode() {
        return debugMode.get();
    }

    public final Integer getDebugPort() { return debugPort.get(); }

    public final boolean isLowResourceMode() { return lowResourceMode.get(); }

    public final boolean isLowCpuMode() { return lowCpuMode.get(); }

    public final boolean isReflection() { return reflection.get(); }

    public final boolean isNoRandoms() { return noRandoms.get(); }

    public final boolean isNoInterface() { return noInterface.get(); }

    public final WorldType getWorldType() { return worldType.get(); }

    public final Integer getWorld() { return world.get(); }

    public boolean isRandomizeWorld() { return randomizeWorld.get(); }

    public final void setRunescapeAccount(final RunescapeAccount runescapeAccount) { this.runescapeAccount.set(runescapeAccount); }

    public final void setScript(final Script script) { this.script.set(script); }

    public final void setProxy(final Proxy proxy) {
        this.proxy.set(proxy);
    }

    public final void setMemoryAllocation(final Integer memoryAllocation) {
        this.memoryAllocation.set(memoryAllocation);
    }

    public final void setCollectData(final boolean collectData) {
        this.collectData.set(collectData);
    }

    public final void setDebugMode(final boolean debugMode) {
        this.debugMode.set(debugMode);
    }

    public final void setDebugPort(final Integer debugPort) {
        this.debugPort.set(debugPort);
    }

    public final void setLowCpuMode(final boolean lowCpuMode) { this.lowCpuMode.set(lowCpuMode); }

    public final void setLowResourceMode(final boolean lowResourceMode) { this.lowResourceMode.set(lowResourceMode); }

    public final void setReflection(final boolean reflection) { this.reflection.set(reflection); }

    public final void setNoRandoms(final boolean noRandoms) { this.noRandoms.set(noRandoms); }

    public final void setNoInterface(final boolean noInterface) { this.noInterface.set(noInterface); }

    public final void setWorldType(final WorldType worldType) { this.worldType.set(worldType); }

    public final void setWorld(final Integer world) { this.world.set(world); }

    public final void setRandomizeWorld(final boolean randomizeWorld) { this.randomizeWorld.set(randomizeWorld); }

    public final boolean isRunning() { return isRunning.get(); }

    public void setRunning(final boolean isRunning) { this.isRunning.set(isRunning); }

    public final void addRunListener(final ChangeListener<Boolean> listener) { isRunning.addListener(listener); }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.writeObject(getRunescapeAccount());
        stream.writeObject(getScript());
        stream.writeObject(getProxy());
        stream.writeInt(getMemoryAllocation());
        stream.writeBoolean(isCollectData());
        stream.writeBoolean(isDebugMode());
        stream.writeInt(getDebugPort());
        stream.writeBoolean(isLowCpuMode());
        stream.writeBoolean(isLowResourceMode());
        stream.writeObject(getWorldType());
        stream.writeInt(getWorld());
        stream.writeBoolean(isRandomizeWorld());
        stream.writeBoolean(isReflection());
        stream.writeBoolean(isNoRandoms());
        stream.writeBoolean(isNoInterface());
    }

    private void readObject(ObjectInputStream stream) throws ClassNotFoundException, IOException {
        runescapeAccount = new SimpleObjectProperty<>((RunescapeAccount) stream.readObject());
        script = new SimpleObjectProperty<>((Script) stream.readObject());
        proxy = new SimpleObjectProperty<>((Proxy) stream.readObject());
        memoryAllocation = new SimpleIntegerProperty(stream.readInt());
        collectData = new SimpleBooleanProperty(stream.readBoolean());
        debugMode = new SimpleBooleanProperty(stream.readBoolean());
        debugPort = new SimpleIntegerProperty(stream.readInt());
        lowCpuMode = new SimpleBooleanProperty(stream.readBoolean());
        lowResourceMode = new SimpleBooleanProperty(stream.readBoolean());
        worldType = new SimpleObjectProperty<>((WorldType) stream.readObject());
        world = new SimpleIntegerProperty(stream.readInt());
        randomizeWorld = new SimpleBooleanProperty(stream.readBoolean());
        try {
            reflection = new SimpleBooleanProperty(stream.readBoolean());
            noRandoms = new SimpleBooleanProperty(stream.readBoolean());
        } catch (Exception e) {
            System.out.println("Config does not contain new allow options, skipping");
            reflection = new SimpleBooleanProperty();
            noRandoms = new SimpleBooleanProperty();
        }
        try {
            noInterface = new SimpleBooleanProperty(stream.readBoolean());
        } catch (Exception e) {
            System.out.println("Config does not contain new nointerface option, skipping");
            noInterface = new SimpleBooleanProperty();
        }
        isRunning = new SimpleBooleanProperty();
    }

    @Override
    public final String toParameterString() {
        String parameterString = String.join(" ", runescapeAccount.get().toParameterString(), script.get().toParameterString());
        if (proxy != null) parameterString += " " + proxy.get().toParameterString();
        if (memoryAllocation.get() != -1) parameterString += " -mem " + memoryAllocation.get();
        if (collectData.get()) parameterString += " -data 1";

        if (debugMode.get() && debugPort.get() != -1) {
            parameterString += " -debug " + debugPort.get();
        } else {
            Optional<Integer> availablePort = getAvailablePort();
            if (availablePort.isPresent()) {
                parameterString += " -debug " + availablePort.get();
            }
        }

        List<String> allowParams = new ArrayList<>();
        if (lowResourceMode.get()) allowParams.add("lowresource");
        if (lowCpuMode.get()) allowParams.add("lowcpu");
        if (reflection.get()) allowParams.add("reflection");
        if (noRandoms.get()) allowParams.add("norandoms");
        if (noInterface.get()) allowParams.add("nointerface");

        if (!allowParams.isEmpty()) {
            parameterString += " -allow " + String.join(",", allowParams);
        }

        int worldVal;
        if (randomizeWorld.get()) worldVal = worldType.get().worlds[new Random().nextInt(worldType.get().worlds.length)];
        else worldVal = world.get();

        if (worldVal != -1) parameterString += " -world " + worldVal;

        return parameterString;
    }

    private Optional<Integer> getAvailablePort() {
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            return Optional.of(serverSocket.getLocalPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Configuration createCopy() {
        Configuration configurationCopy = new Configuration(
                runescapeAccount.get().createCopy(),
                script.get().createCopy()
        );
        configurationCopy.setProxy(getProxy().createCopy());
        configurationCopy.setMemoryAllocation(getMemoryAllocation());
        configurationCopy.setCollectData(isCollectData());
        configurationCopy.setDebugMode(isDebugMode());
        configurationCopy.setDebugPort(getDebugPort());
        configurationCopy.setLowCpuMode(isLowCpuMode());
        configurationCopy.setLowResourceMode(isLowResourceMode());
        configurationCopy.setWorldType(getWorldType());
        configurationCopy.setWorld(getWorld());
        configurationCopy.setRandomizeWorld(isRandomizeWorld());
        configurationCopy.setReflection(isReflection());
        configurationCopy.setNoRandoms(isNoRandoms());
        return configurationCopy;
    }
}
