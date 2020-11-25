package hbrs.ooka;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class LZUTest {
    private static LZU lzu;

    @BeforeAll
    public static void beforeAll() throws Exception {
        lzu = new LZU();
    }

    private void deploy(String name, String pathToJar) throws IOException, ClassNotFoundException {
        lzu.deployComponent(name, pathToJar);
    }

    @Test
    public void deployComponent() throws IOException, ClassNotFoundException {
        String name = "Buchungssystem";
        String pathToJar = "C:\\Users\\alexa\\Documents\\WORKSPACE\\OOKA_Buchungssystem\\target\\OOKA_Buchungssystem-1.0-SNAPSHOT.jar";
        deploy(name, pathToJar);
        assertEquals(name, lzu.getComponents().get(name).getName());
    }

    @Test
    public void deployStartComponent() throws InvocationTargetException, IllegalAccessException, IOException, ClassNotFoundException {
        String name = "Buchungssystem";
        String pathToJar = "C:\\Users\\alexa\\Documents\\WORKSPACE\\OOKA_Buchungssystem\\target\\OOKA_Buchungssystem-1.0-SNAPSHOT.jar";
        deploy(name, pathToJar);
        lzu.startComponent(name);
        assertEquals(State.RUNNING, lzu.getComponents().get(name).getState());
    }

    @Test
    public void deployStartStopComponent() throws InvocationTargetException, IllegalAccessException, IOException, ClassNotFoundException {
        String name = "Buchungssystem";
        String pathToJar = "C:\\Users\\alexa\\Documents\\WORKSPACE\\OOKA_Buchungssystem\\target\\OOKA_Buchungssystem-1.0-SNAPSHOT.jar";
        deploy(name, pathToJar);
        lzu.startComponent(name);
        lzu.stopComponent(name);
        assertEquals(State.STOPPED, lzu.getComponents().get(name).getState());
    }

    @Test
    public void deployStartStopTwoComponents() throws IOException, ClassNotFoundException {
        String name1 = "Buchungssystem";
        String pathToJar1 = "C:\\Users\\alexa\\Documents\\WORKSPACE\\OOKA_Buchungssystem\\target\\OOKA_Buchungssystem-1.0-SNAPSHOT.jar";
        deploy(name1, pathToJar1);

        String name2= "Dummy Component";
        String pathToJar2 = "C:\\Users\\alexa\\Documents\\WORKSPACE\\OOKA_DummyComponent\\target\\OOKA_DummyComponent-1.0-SNAPSHOT.jar";
        deploy(name2, pathToJar2);

        lzu.startComponent(name1);
        assertEquals(State.RUNNING, lzu.getComponents().get(name1).getState());
        lzu.startComponent(name2);
        assertEquals(State.RUNNING, lzu.getComponents().get(name2).getState());

        lzu.stopComponent(name1);
        assertEquals(State.STOPPED, lzu.getComponents().get(name1).getState());
        lzu.stopComponent(name2);
        assertEquals(State.STOPPED, lzu.getComponents().get(name2).getState());
    }
}
