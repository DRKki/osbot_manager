package bot_parameters.configuration;

import bot_parameters.account.OSBotAccount;
import bot_parameters.account.RunescapeAccount;
import bot_parameters.interfaces.BotParameter;
import bot_parameters.interfaces.Copyable;
import bot_parameters.proxy.Proxy;
import bot_parameters.script.Script;
import exceptions.ClientOutOfDateException;
import exceptions.IncorrectLoginException;
import exceptions.MissingWebWalkDataException;
import gui.dialogues.error_dialog.ExceptionDialog;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.net.ServerSocket;
import java.util.*;

public final class Configuration implements BotParameter, Copyable<Configuration>, Serializable {

    private static final long serialVersionUID = 1938451332017337304L;

    private SimpleObjectProperty<RunescapeAccount> runescapeAccount;
    private SimpleListProperty<Script> scripts;
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
    private SimpleBooleanProperty noRender = new SimpleBooleanProperty();
    private SimpleObjectProperty<WorldType> worldType = new SimpleObjectProperty<>();
    private SimpleIntegerProperty world = new SimpleIntegerProperty(-1);
    private SimpleBooleanProperty randomizeWorld = new SimpleBooleanProperty();
    private SimpleBooleanProperty isRunning = new SimpleBooleanProperty();

    private int processID;

    public Configuration(final RunescapeAccount runescapeAccount, final ObservableList<Script> scripts) {
        this.runescapeAccount = new SimpleObjectProperty<>(runescapeAccount);
        this.scripts = new SimpleListProperty<>(scripts);
        this.proxy = new SimpleObjectProperty<>(new Proxy("No Proxy", -1));
    }

    public RunescapeAccount getRunescapeAccount() {
        return runescapeAccount.get();
    }

    public final ObservableList<Script> getScripts() {
        return scripts.get();
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

    public final boolean isNoRender() { return noRender.get(); }

    public final WorldType getWorldType() { return worldType.get(); }

    public final Integer getWorld() { return world.get(); }

    public boolean isRandomizeWorld() { return randomizeWorld.get(); }

    public final void setRunescapeAccount(final RunescapeAccount runescapeAccount) { this.runescapeAccount.set(runescapeAccount); }

    public final void setScripts(final ObservableList<Script> scripts) { this.scripts.set(scripts); }

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

    public final void setNoRender(final boolean noRender) { this.noRender.set(noRender); }

    public final void setWorldType(final WorldType worldType) { this.worldType.set(worldType); }

    public final void setWorld(final Integer world) { this.world.set(world); }

    public final void setRandomizeWorld(final boolean randomizeWorld) { this.randomizeWorld.set(randomizeWorld); }

    public final boolean isRunning() { return isRunning.get(); }

    public void setRunning(final boolean isRunning) { this.isRunning.set(isRunning); }

    public final void addRunListener(final ChangeListener<Boolean> listener) { isRunning.addListener(listener); }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.writeObject(getRunescapeAccount());
        stream.writeObject(new ArrayList<>(getScripts()));
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
        stream.writeBoolean(isNoRender());
    }

    private void readObject(ObjectInputStream stream) throws ClassNotFoundException, IOException {
        runescapeAccount = new SimpleObjectProperty<>((RunescapeAccount) stream.readObject());
        Object scriptObj = stream.readObject();
        if (scriptObj instanceof Script) {
            List<Script> scriptList = new ArrayList<>();
            scriptList.add((Script) scriptObj);
            scripts = new SimpleListProperty<>(FXCollections.observableArrayList(scriptList));
        } else {
            scripts = new SimpleListProperty<>(FXCollections.observableArrayList((List<Script>) scriptObj));
        }
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
        try {
            noRender = new SimpleBooleanProperty(stream.readBoolean());
        } catch (Exception e) {
            System.out.println("Config does not contain new norender option, skipping");
            noRender = new SimpleBooleanProperty();
        }
        isRunning = new SimpleBooleanProperty();
    }

    @Override
    public final String toParameterString() {
        String parameterString = runescapeAccount.get().toParameterString();
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
        if (lowResourceMode.get()) {
            allowParams.add("lowresource");
        }
        if (lowCpuMode.get()) {
            allowParams.add("lowcpu");
        }
        if (reflection.get()) {
            allowParams.add("reflection");
        }
        if (noRandoms.get()) {
            allowParams.add("norandoms");
        }
        if (noInterface.get()) {
            allowParams.add("nointerface");
        }
        if (noRender.get()) {
            allowParams.add("norender");
        }

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
                runescapeAccount.get(),
                scripts.get()
        );
        configurationCopy.setProxy(getProxy());
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
        configurationCopy.setNoRender(isNoRender());
        configurationCopy.setNoInterface(isNoInterface());
        return configurationCopy;
    }

    public void run(final String osbotPath, final OSBotAccount osBotAccount) {
        Thread runThread = new Thread(() -> {

            for (final Script script : scripts.get()) {

                List<String> command = new ArrayList<>();

                Collections.addAll(command, "java", "-jar", osbotPath);
                Collections.addAll(command, osBotAccount.toParameterString().split(" "));
                Collections.addAll(command, this.toParameterString().split(" "));
                Collections.addAll(command, script.toParameterString().split(" "));

                try {

                    final ProcessBuilder processBuilder = new ProcessBuilder(command);
                    processBuilder.redirectErrorStream(true);
                    final Process process = processBuilder.start();

                    setRunning(true);

                    List<Integer> javaPIDs = getJavaPIDs();

                    processID = -1;

                    try (final InputStream stdout = process.getInputStream();
                         final InputStreamReader inputStreamReader = new InputStreamReader(stdout);
                         final BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

                        String outputLine;
                        while ((outputLine = bufferedReader.readLine()) != null) {

                            outputLine = outputLine.trim();

                            System.out.println(outputLine);

                            if (outputLine.toLowerCase().contains("client is out of date")) {
                                Platform.runLater(() -> new ExceptionDialog(new ClientOutOfDateException()).show());
                                setRunning(false);
                                return;
                            } else if (outputLine.toLowerCase().contains("update web walking")) {
                                Platform.runLater(() -> new ExceptionDialog(new MissingWebWalkDataException()).show());
                                setRunning(false);
                                return;
                            } else if (outputLine.toLowerCase().contains("invalid username or password")) {
                                Platform.runLater(() -> new ExceptionDialog(new IncorrectLoginException()).show());
                                setRunning(false);
                                return;
                            } else if (outputLine.contains("OSBot is now ready!")) {
                                List<Integer> newJavaPIDs = getJavaPIDs();
                                newJavaPIDs.removeAll(javaPIDs);

                                if (newJavaPIDs.size() == 1) {
                                    processID = newJavaPIDs.get(0);
                                }
                            } else if (outputLine.contains("Bot exited") || (outputLine.contains("Script") && outputLine.endsWith("has exited!"))) {
                                break;
                            }
                        }
                    }

                    System.out.println("Reached");
                    if (processID != -1) {
                        System.out.println("Killing");
                        killProcess(processID);
                        processID = -1;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                setRunning(false);
            }
        });
        runThread.start();
    }

    public void stop() {
        if (!isRunning()) {
            return;
        }
        if (processID == -1) {
            return;
        }
        killProcess(processID);
    }

    private List<Integer> getJavaPIDs() {
        if (System.getProperty("os.name").startsWith("Windows")) {
            return getJavaPIDsWindows();
        } else {
            return getJavaPIDsLinux();
        }
    }

    private List<Integer> getJavaPIDsWindows()  {
        List<Integer> pids = new ArrayList<>();
        try {
            Process process = Runtime.getRuntime().exec("tasklist /FI \"IMAGENAME eq java.exe\" /NH");
            try (final InputStream stdout = process.getInputStream();
                 final InputStreamReader inputStreamReader = new InputStreamReader(stdout);
                 final BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
                String processInfo;
                while ((processInfo = bufferedReader.readLine()) != null) {
                    processInfo = processInfo.trim();
                    String[] values = processInfo.split("\\s+");
                    if (values.length >= 2) {
                        pids.add(Integer.parseInt(values[1]));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pids;
    }

    private List<Integer> getJavaPIDsLinux()  {
        List<Integer> pids = new ArrayList<>();
        try {
            Process process = Runtime.getRuntime().exec("ps aux | grep java");
            try (final InputStream stdout = process.getInputStream();
                 final InputStreamReader inputStreamReader = new InputStreamReader(stdout);
                 final BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
                String processInfo;
                while ((processInfo = bufferedReader.readLine()) != null) {
                    processInfo = processInfo.trim();
                    String[] values = processInfo.split("\\s+");
                    if (values.length > 0) {
                        pids.add(Integer.parseInt(values[0]));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pids;
    }

    private void killProcess(final int PID) {
        try {
            if (System.getProperty("os.name").startsWith("Windows")) {
                Runtime.getRuntime().exec("Taskkill /PID " + PID + " /F");
            } else {
                Runtime.getRuntime().exec("kill -9 " + PID);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
