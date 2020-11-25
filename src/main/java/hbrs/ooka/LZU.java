package hbrs.ooka;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class LZU {

    private HashMap<String, Component> components = new HashMap<>();

    public void start(){

    }

    public void stop(){

    }

    public void deployComponent(String name, String pathToJar) throws IOException, ClassNotFoundException {
        System.out.println("Deploy Component " + name + " with Jar at path: " + pathToJar);

        Component component = new Component();
        component.setName(name);

        JarFile jarFile = new JarFile(pathToJar);
        Enumeration<JarEntry> e = jarFile.entries();

        URL[] urls = {new URL("jar:file:" + pathToJar + "!/")};
        URLClassLoader cl = URLClassLoader.newInstance(urls);

        while (e.hasMoreElements() && component.getStartClass() == null) {
            JarEntry je = e.nextElement();
            if (je.isDirectory() || !je.getName().endsWith(".class")) {
                continue;
            }
            // -6 because of .class
            String className = je.getName().substring(0, je.getName().length() - 6);
            className = className.replace('/', '.');

            Class c = cl.loadClass(className);
            Method[] methods = c.getMethods();

            for (Method m : methods) {
                String annotations = Arrays.toString(m.getDeclaredAnnotations());
                if (annotations.contains("@hbrs.ooka.annotation.Start()")) {
                    component.setStartClass(c);
                    component.setStartMethod(m);
                }
                if (annotations.contains("@hbrs.ooka.annotation.Stop()")) {
                    component.setStopMethod(m);
                }
            }
        }

        components.put(name, component);
        System.out.println("Deployment of " + name + " done");
    }

    public void startComponent(String name) {
        System.out.println("Start component " + name);
        Component component = components.get(name);
        component.start();
    }

    public void stopComponent(String name) {
        System.out.println("Stop component " + name);
        Component component = components.get(name);
        component.stop();
    }

    public HashMap<String, Component> getComponents() {
        return components;
    }
}
