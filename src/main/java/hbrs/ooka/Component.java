package hbrs.ooka;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Random;

public class Component {

    private String name;
    private String description;
    private String author;
    private String version;
    private Class startClass;
    private Method startMethod;
    private Method stopMethod;

    private String state = State.INITIALZED;

    private final HashMap<String, Thread> threads = new HashMap<>();
    private final HashMap<String, Object> instances = new HashMap<>();

    public String start(){
        final String instanceId = String.valueOf(Math.abs(new Random().nextLong()));
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Object instance = startClass.getDeclaredConstructor().newInstance();
                    startMethod.invoke(instance);
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

}
