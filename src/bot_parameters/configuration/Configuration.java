package bot_parameters.configuration;

import bot_parameters.account.RunescapeAccount;
import bot_parameters.interfaces.BotParameter;
import bot_parameters.proxy.Proxy;
import bot_parameters.script.Script;

import java.io.Serializable;

public final class Configuration implements BotParameter, Serializable {

    private RunescapeAccount runescapeAccount;
    private Script script;
    private Proxy proxy;
    private Integer memoryAllocation;
    private boolean collectData;
    private boolean debugMode;
    private Integer debugPort;
    private boolean lowCpuMode;
    private boolean lowResourceMode;

    public Configuration(final RunescapeAccount runescapeAccount, final Script script) {
        this.runescapeAccount = runescapeAccount;
        this.script = script;
    }

    public RunescapeAccount getRunescapeAccount() {
        return runescapeAccount;
    }

    public final Script getScript() {
        return script;
    }

    public final Proxy getProxy() {
        return proxy;
    }

    public final Integer getMemoryAllocation() {
        return memoryAllocation;
    }

    public final boolean isCollectData() {
        return collectData;
    }

    public final boolean isDebugMode() {
        return debugMode;
    }

    public final Integer getDebugPort() { return debugPort; }

    public final boolean isLowResourceMode() { return lowResourceMode; }

    public final boolean isLowCpuMode() { return lowCpuMode; }

    public final void setRunescapeAccount(final RunescapeAccount runescapeAccount) { this.runescapeAccount = runescapeAccount; }

    public final void setScript(final Script script) { this.script = script; }

    public final void setProxy(final Proxy proxy) {
        this.proxy = proxy;
    }

    public final void setMemoryAllocation(final Integer memoryAllocation) {
        this.memoryAllocation = memoryAllocation;
    }

    public final void setCollectData(final boolean collectData) {
        this.collectData = collectData;
    }

    public final void setDebugMode(final boolean debugMode) {
        this.debugMode = debugMode;
    }

    public final void setDebugPort(final Integer debugPort) {
        this.debugPort = debugPort;
    }

    public final void setLowCpuMode(final boolean lowCpuMode) { this.lowCpuMode = lowCpuMode; }

    public final void setLowResourceMode (final boolean lowResourceMode) { this.lowResourceMode = lowResourceMode; }

    @Override
    public final String toParameterString() {
        String parameterString = String.join(" ", runescapeAccount.toParameterString(), script.toParameterString());
        if (proxy != null) parameterString += " " + proxy.toParameterString();
        if (memoryAllocation != null) parameterString += " -mem " + memoryAllocation;
        if (collectData) parameterString += " -data 1";
        if (debugMode && debugPort != null) parameterString += " -debug " + debugPort;

        if (lowResourceMode && lowCpuMode) parameterString += " -allow lowresource,lowcpu";
        else if (lowResourceMode) parameterString += " -allow lowresource";
        else if (lowCpuMode) parameterString += " -allow lowcpu";

        return parameterString;
    }

    @Override
    public String toString() {
        String configText = "";
        configText += runescapeAccount.toString();
        configText += " | ";
        configText += script.toString();
        if (proxy != null) configText += " | " + proxy.toString();
        if (memoryAllocation != null) configText += " | Memory: " + memoryAllocation;
        configText += " | Collect Data: " + collectData;
        configText += " | Debug Mode: " + debugMode;
        if (debugPort != null) configText += " | Debug Port: " + debugPort;
        if (lowResourceMode) configText += " | Low Resource Mode";
        if (lowCpuMode) configText += " | Low CPU Mode";
        return configText;
    }
}
