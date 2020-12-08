package hbrs.ooka.lzu;

import hbrs.ooka.log.Logger;
import hbrs.ooka.log.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class LZU {

    private final HashMap<String, Component> components = new HashMap<>();

    public void start(){
        // what should be done here?
    }

    public void stop(){
        Iterator<Map.Entry<String, Component>> itComponents = components.entrySet().iterator();
        while(itComponents.hasNext()){
            Map.Entry<String, Component> eComponent = itComponents.next();
            Component c = eComponent.getValue();
            stopComponent(c.getName());
        }
    }

    public void deployComponent(String name, String pathToJar) throws IOException, ClassNotFoundException {
        System.out.println("Deploy Component " + name + " with Jar at path: " + pathToJar);

        Component component = new Component();
        component.setName(name);
        component.setPathToJar(pathToJar);

        URL[] urls = {new URL("jar:file:" + pathToJar + "!/")};
        URLClassLoader cl = URLClassLoader.newInstance(urls);
        component.setClassLoader(cl);

        component.init();
        if(component.getStartMethod() == null || component.getStopMethod() == null || component.getInjectMethod() == null){
            System.out.println("Start-/Stop-/Logger-Inject-Method missing in component!");
            return;
        }

        components.put(name, component);
        persistCurrentConfiguration();
        System.out.println("Deployment of " + name + " done");
    }

    public void removeComponent(String name){
        System.out.println("Remove Component " + name );
        stopComponent(name);
        components.remove(name);
        persistCurrentConfiguration();
        System.out.println("Removal of " + name + " done");
    }

    public String startComponent(String name) {
        System.out.println("Start component " + name);
        Component component = components.get(name);
        String instanceId = component.start();
        System.out.println("Created instance with instanceId " + instanceId);
        return instanceId;
    }

    public void stopComponent(String name) {
        System.out.println("Stop all instances of component " + name);
        Component component = components.get(name);
        component.stop();
    }

    public void stopComponent(String name, String instanceId) {
        System.out.println("Stop instance " + instanceId + " of component " + name);
        Component component = components.get(name);
        component.stop(instanceId);
    }

    public void persistCurrentConfiguration(){
        String filename = "lzu_configuration.txt";
        File file = new File(filename);
        try {
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.flush();
            int count = 1;
            for(Map.Entry<String, Component> e : components.entrySet()){
                writer.append(count++ + ": " + e.getKey() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, Component> getComponents() {
        return components;
    }

}
