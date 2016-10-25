package file_manager;

import bot_parameters.script.Script;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class LocalScriptLoader {

    private final Pattern nameRe = Pattern.compile("name=([^,)]+)");
    private final Pattern authorRe = Pattern.compile("author=(.+)");

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
                Script script = getScriptFromJar(jarFile, classLoader);
                if(script != null) localScripts.add(script);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return localScripts;
    }

    private Script getScriptFromJar(final JarFile jarFile, final ClassLoader classLoader) throws ClassNotFoundException {
        Enumeration entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = (JarEntry) entries.nextElement();
            String name = entry.getName();
            if (name.endsWith(".class")) {
                Script script = getScriptFromClass(classLoader, name);
                if(script != null) {
                    return script;
                }
            }
        }
        return null;
    }

    private Script getScriptFromClass(final ClassLoader classLoader, final String className) throws ClassNotFoundException {
        Class cls = classLoader.loadClass(className.replace('/', '.').replace(".class", ""));
        for (Annotation annotation : cls.getAnnotations()) {
            String annotationStr = annotation.toString();
            if (annotationStr.contains("ScriptManifest")) {
                return getScriptFromAnnotation(annotationStr);
            }
        }
        return null;
    }

    private Script getScriptFromAnnotation(final String annotation) {
        String name = "", author = "";

        Matcher nameMatcher = nameRe.matcher(annotation);
        if (nameMatcher.find()) {
            name = nameMatcher.group(1);
        }

        if(name.equals("")) return null;
        return new Script(name, "none", true);
    }
}
