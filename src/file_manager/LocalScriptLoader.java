package file_manager;

import bot_parameters.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class LocalScriptLoader {

    public final List<Script> getLocalScripts() {

        final List<Script> localScripts = new ArrayList<>();

        try {
            final String scriptsDirPath = Paths.get(System.getProperty("user.home"), "OSBot", "Scripts").toString();
            final File scriptsDir = new File(scriptsDirPath);

            if(!scriptsDir.isDirectory()) return localScripts;

            final File[] jarFiles = scriptsDir.listFiles((dir, name) -> name.endsWith(".jar"));

            if(jarFiles == null) return localScripts;

            for(final File file : jarFiles) {
                URL url = file.toURI().toURL();
                ClassLoader classLoader = new URLClassLoader(new URL[]{ url });
                JarFile jarFile = new JarFile(file.getAbsolutePath());
                addScriptsFromJar(jarFile, classLoader, localScripts);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return localScripts;
    }

    private void addScriptsFromJar(final JarFile jarFile, final ClassLoader classLoader, final List<Script> localScripts) throws ClassNotFoundException {
        Enumeration entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = (JarEntry) entries.nextElement();
            String name = entry.getName();
            if (name.endsWith(".class")) {
                Script script = getScriptFromClass(classLoader, name);
                if(script != null) {
                    localScripts.add(script);
                }
            }
        }
    }

    private Script getScriptFromClass(final ClassLoader classLoader, final String className) throws ClassNotFoundException {
        Class cls = classLoader.loadClass(className.replace('/', '.').replace(".class", ""));
        Annotation scriptManifest = cls.getAnnotation(ScriptManifest.class);
        if(scriptManifest != null) {
            return new Script(((ScriptManifest) scriptManifest).name(), "", true);
        }
        return null;
    }
}
