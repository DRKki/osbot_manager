package bot_parameters.configuration;

import bot_parameters.account.RunescapeAccount;
import bot_parameters.interfaces.BotParameter;
import bot_parameters.proxy.Proxy;
import bot_parameters.script.Script;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.io.Serializable;
import java.util.Random;

public final class Configuration implements BotParameter, Serializable {

    private SimpleObjectProperty<RunescapeAccount> runescapeAccount;
    private SimpleObjectProperty<Script> script;
    private SimpleObjectProperty<Proxy> proxy;
    private final SimpleIntegerProperty memoryAllocation = new SimpleIntegerProperty(-1);
    private final SimpleBooleanProperty collectData = new SimpleBooleanProperty();
    private final SimpleBooleanProperty debugMode = new SimpleBooleanProperty();
    private final SimpleIntegerProperty debugPort = new SimpleIntegerProperty(-1);
    private final SimpleBooleanProperty lowCpuMode = new SimpleBooleanProperty();
    private final SimpleBooleanProperty lowResourceMode = new SimpleBooleanProperty();
    private final SimpleObjectProperty<WorldType> worldType = new SimpleObjectProperty<>();
    private final SimpleIntegerProperty world = new SimpleIntegerProperty(-1);
    private final SimpleBooleanProperty randomizeWorld = new SimpleBooleanProperty();

    public Configuration(final RunescapeAccount runescapeAccount, final Script script) {
        this.runescapeAccount = new SimpleObjectProperty<>(runescapeAccount);
        this.script = new SimpleObjectProperty<>(script);
        this.proxy = new SimpleObjectProperty<>(new Proxy("No Proxy", -1));
    }

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

    public final void setLowResourceMode (final boolean lowResourceMode) { this.lowResourceMode.set(lowResourceMode); }

    public final void setWorldType(final WorldType worldType) { this.worldType.set(worldType); }

    public final void setWorld(final Integer world) { this.world.set(world); }

    public final void setRandomizeWorld(final boolean randomizeWorld) { this.randomizeWorld.set(randomizeWorld); }

    @Override
    public final String toParameterString() {
        String parameterString = String.join(" ", runescapeAccount.get().toParameterString(), script.get().toParameterString());
        if (proxy != null) parameterString += " " + proxy.get().toParameterString();
        if (memoryAllocation.get() != -1) parameterString += " -mem " + memoryAllocation.get();
        if (collectData.get()) parameterString += " -data 1";
        if (debugMode.get() && debugPort.get() != -1) parameterString += " -debug " + debugPort.get();

        if (lowResourceMode.get() && lowCpuMode.get()) parameterString += " -allow lowresource,lowcpu";
        else if (lowResourceMode.get()) parameterString += " -allow lowresource";
        else if (lowCpuMode.get()) parameterString += " -allow lowcpu";

        int worldVal;
        if (randomizeWorld.get()) worldVal = worldType.get().worlds[new Random().nextInt(worldType.get().worlds.length)];
        else worldVal = world.get();

        if (worldVal != -1) parameterString += " -world " + worldVal;

        return parameterString;
    }
}
