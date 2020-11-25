package hbrs.ooka;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Component implements Runnable {

    private String name;
    private String description;
    private String author;
    private String version;
    private Class startClass;
    private Method startMethod;
    private Method stopMethod;

    private String state;

    private Thread thread;

    @Override
    public void run() {
        Method startMethod = this.getStartMethod();
        try {
            startMethod.invoke(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void start(){
        thread = new Thread(this);
        thread.start();
        setState(State.RUNNING);
    }

    public void stop(){
        thread.interrupt();
        setState(State.STOPPED);
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
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
