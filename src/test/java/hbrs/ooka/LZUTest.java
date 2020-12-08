package hbrs.ooka;

import hbrs.ooka.lzu.Component;
import hbrs.ooka.lzu.LZU;
import hbrs.ooka.lzu.State;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class LZUTest {
    private static LZU lzu;

    private static HashMap<String, String> components = new HashMap<>();
    private static final String KEY_BUCHUNG = "Buchungssystem";
    private static final String KEY_DUMMY = "DummyComponent";

    @BeforeAll
    public static void beforeAll() throws Exception {
        lzu = new LZU();
        components.put(KEY_BUCHUNG, "C:\\Users\\alexa\\Documents\\WORKSPACE\\OOKA_Buchungssystem\\target\\OOKA_Buchungssystem-1.0-SNAPSHOT.jar");
        components.put(KEY_DUMMY, "C:\\Users\\alexa\\Documents\\WORKSPACE\\OOKA_DummyComponent\\target\\OOKA_DummyComponent-1.0-SNAPSHOT.jar");
    }

    private void deploy(String name) throws IOException, ClassNotFoundException {
        lzu.deployComponent(name, components.get(name));
    }

    private String start(String name){
        return lzu.startComponent(name);
    }

    private void stop(String name, String instanceId){
        lzu.stopComponent(name, instanceId);
    }

    private String deployStart(String name) throws IOException, ClassNotFoundException {
        deploy(name);
        return start(name);
    }

    @Test
    public void deployComponent() throws IOException, ClassNotFoundException {
        String name = KEY_DUMMY;
        deploy(name);
        assertEquals(name, lzu.getComponents().get(name).getName());
    }

    @Test
    public void deployStartComponent() throws InvocationTargetException, IllegalAccessException, IOException, ClassNotFoundException, InterruptedException {
        String name = KEY_DUMMY;
        deploy(name);
        start(name);
        Thread.sleep(2000);
        assertEquals(State.RUNNING, lzu.getComponents().get(name).getState());
    }

    @Test
    public void deployStartStopComponent() throws InvocationTargetException, IllegalAccessException, IOException, ClassNotFoundException, InterruptedException {
        String name = KEY_DUMMY;
        String instanceId = deployStart(name);
        Thread.sleep(2000);
        stop(name, instanceId);
        Thread.sleep(1000);
        assertEquals(State.DEPLOYED, lzu.getComponents().get(name).getState());
    }

    @Test
    public void deployStartStopTwoComponents() throws IOException, ClassNotFoundException, InterruptedException {
        String name = KEY_DUMMY;
        deploy(name);
        String instanceId1 = start(name);
        String instanceId2 = start(name);
        Thread.sleep(2000);
        assertEquals(State.RUNNING, lzu.getComponents().get(name).getState());

        stop(name, instanceId1);
        stop(name, instanceId2);
        Thread.sleep(1000);
        assertEquals(State.DEPLOYED, lzu.getComponents().get(name).getState());
    }

    @Test
    public void deployStartTwoComponentsStopLZU() throws IOException, ClassNotFoundException, InterruptedException {
        String name = KEY_DUMMY;
        deploy(name);
        String instanceId1 = start(name);
        String instanceId2 = start(name);
        Thread.sleep(2000);
        assertEquals(State.RUNNING, lzu.getComponents().get(name).getState());

        lzu.stop();
        Thread.sleep(1000);
        assertEquals(State.DEPLOYED, lzu.getComponents().get(name).getState());
    }
}
