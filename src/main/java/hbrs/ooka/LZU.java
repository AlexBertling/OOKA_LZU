package hbrs.ooka;

import java.io.IOException;
import java.lang.annotation.Annotation;
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

    public void deploy(String name, String pathToJar) throws IOException, ClassNotFoundException {
        System.out.println("Deploy Component " + name + " with Jar at path: " + pathToJar);

        Component component = new Component();
        component.setName(name);

        JarFile jarFile = new JarFile(pathToJar);
        Enumeration<JarEntry> e = jarFile.entries();

        URL[] urls = { new URL("jar:file:" + pathToJar+"!/") };
        URLClassLoader cl = URLClassLoader.newInstance(urls);

        while (e.hasMoreElements() && component.getStartClass() == null) {
            JarEntry je = e.nextElement();
            if(je.isDirectory() || !je.getName().endsWith(".class")){
                continue;
            }
            // -6 because of .class
            String className = je.getName().substring(0,je.getName().length()-6);
            className = className.replace('/', '.');

            Class c = cl.loadClass(className);
            Method[] methods = c.getDeclaredMethods();

            System.out.println(c.getName());
            for(Method m : methods){
                String annotations = Arrays.toString(m.getDeclaredAnnotations());
                if(annotations.contains("@hbrs.ooka.annotation.Start()")){
                    component.setStartClass(c);
                    component.setStartMethod(m);
                }
                if(annotations.contains("@hbrs.ooka.annotation.Start()")){
                    component.setStopMethod(m);
                }
            }
        }

        components.put(name, component);
        /* Code zum Starten der Komponente
        Class c = component.getStartClass();
        Method mStart = component.getStartMethod();
        Method mStop = component.getStopMethod();
        if(c != null && mStart != null && mStop != null) {
            System.out.println(c.toString());
            try {
                mStart.invoke(null);
            } catch (IllegalAccessException illegalAccessException) {
                illegalAccessException.printStackTrace();
            } catch (InvocationTargetException invocationTargetException) {
                invocationTargetException.printStackTrace();
            }
        }*/
    }


}
