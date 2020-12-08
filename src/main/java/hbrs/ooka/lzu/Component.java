package hbrs.ooka.lzu;

import hbrs.ooka.log.ConcreteLogger;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Random;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Component {

    private String name;
    private String description;
    private String author;
    private String version;
    private String pathToJar;
    private ClassLoader classLoader;
    private Class startClass;
    private Method startMethod;
    private Method stopMethod;
    private Method injectMethod;

    private String state = State.INITIALZED;

    private final HashMap<String, Thread> threads = new HashMap<>();
    private final HashMap<String, Object> instances = new HashMap<>();

    public void init() throws IOException, ClassNotFoundException {
        JarFile jarFile = new JarFile(pathToJar);
        Enumeration<JarEntry> e = jarFile.entries();
        ClassLoader cl = this.getClassLoader();

        while (e.hasMoreElements() && this.getStartClass() == null) {
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
                if(m.isAnnotationPresent(hbrs.ooka.annotation.Start.class)){
                    this.setStartClass(c);
                    this.setStartMethod(m);
                }
                if(m.isAnnotationPresent(hbrs.ooka.annotation.Stop.class)){
                    this.setStopMethod(m);
                }
                if(m.isAnnotationPresent(hbrs.ooka.annotation.Inject.class)){
                    this.setInjectMethod(m);
                }
            }
        }
    }

    public String start(){
        final String instanceId = String.valueOf(Math.abs(new Random().nextLong()));
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Object instance = startClass.getDeclaredConstructor().newInstance();
                    getInjectMethod().invoke(instance, new ConcreteLogger());
                    getStartMethod().invoke(instance);
                    instances.put(instanceId, instance);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
        threads.put(instanceId, thread);
        setState(State.RUNNING);

        return instanceId;
    }

    public void stop(){ // stop all instances
        HashMap<String, Object> instances = getInstances();
        while(instances.size()>0){
            String instanceId = (String) instances.keySet().toArray()[0];
            stop(instanceId);
            instances = getInstances();
        }
    }

    public void stop(String instanceId) {
        Object instance = instances.remove(instanceId);
        try {
            stopMethod.invoke(instance);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Thread thread = threads.remove(instanceId);
        thread.stop();
        setState(State.DEPLOYED);
    }

    public HashMap<String, Thread> getThreads() {
        return threads;
    }

    public HashMap<String, Object> getInstances() {
        return instances;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Method getStartMethod() {
        return startMethod;
    }

    public void setStartMethod(Method startMethod) {
        this.startMethod = startMethod;
    }

    public Method getStopMethod() {
        return stopMethod;
    }

    public void setStopMethod(Method stopMethod) {
        this.stopMethod = stopMethod;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Class getStartClass() {
        return startClass;
    }

    public void setStartClass(Class startClass) {
        this.startClass = startClass;
    }

    public String getPathToJar() {
        return pathToJar;
    }

    public void setPathToJar(String pathToJar) {
        this.pathToJar = pathToJar;
    }

    public Method getInjectMethod() {
        return injectMethod;
    }

    public void setInjectMethod(Method injectMethod) {
        this.injectMethod = injectMethod;
    }
}
