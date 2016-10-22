package file_manager;

import java.io.File;
import java.io.FilenameFilter;
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

    public final List<String> getLocalScriptNames() {

        final List<String> localScriptNames = new ArrayList<>();

        try {
            final String scriptsDirPath = Paths.get(System.getProperty("user.home"), "OSBot", "Scripts").toString();
            final File scriptsDir = new File(scriptsDirPath);

            if(!scriptsDir.isDirectory()) return localScriptNames;

            final File[] jarFiles = scriptsDir.listFiles((dir, name) -> name.endsWith(".jar"));

            if(jarFiles == null) return localScriptNames;

            for(final File file : jarFiles) {

                URL url = file.toURI().toURL();
                ClassLoader classLoader = new URLClassLoader(new URL[]{url, new URL("file://osbot.jar")});

                JarFile jarFile = new JarFile(file.getAbsolutePath());
                Enumeration entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = (JarEntry) entries.nextElement();
                    String name = entry.getName();
                    if (name.contains(".class")) {
                        Class cls = classLoader.loadClass(name.replace('/', '.').replace(".class", ""));
                        for (Annotation annotation : cls.getAnnotations()) {
                            String annotationStr = annotation.toString();
                            if (annotationStr.contains("ScriptManifest")) {
                                localScriptNames.add(getScriptNameFromAnnotation(annotationStr));
                                break;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return localScriptNames;
    }

    private String getScriptNameFromAnnotation(final String annotation) {
        for (String component : annotation.split(", ")) {
            if (component.startsWith("name=")) {
                return component.substring(5, component.length());
            }
        }
        return "";
    }
}
