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

    @Test
    public void deployComponent() throws IOException, ClassNotFoundException {
        String name = "Buchungssystem";
        String pathToJar = "C:\\Users\\alexa\\Documents\\WORKSPACE\\OOKA_Buchungssystem\\target\\OOKA_Buchungssystem-1.0-SNAPSHOT.jar";
        lzu.deployComponent(name, pathToJar);
        assertEquals(name, lzu.getComponents().get(name).getName());
    }

    @Test
    public void startComponent() throws InvocationTargetException, IllegalAccessException, IOException, ClassNotFoundException {
        String name = "Buchungssystem";
        deployComponent();
        lzu.startComponent(name);
        assertEquals(State.RUNNING, lzu.getComponents().get(name).getState());
    }

    @Test
    public void stopComponent() throws InvocationTargetException, IllegalAccessException, IOException, ClassNotFoundException {
        String name = "Buchungssystem";
        startComponent();
        lzu.stopComponent(name);
        assertEquals(State.STOPPED, lzu.getComponents().get(name).getState());
    }
}
