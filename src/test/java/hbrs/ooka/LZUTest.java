package hbrs.ooka;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

public class LZUTest {
    private static LZU lzu;

    @BeforeClass
    public static void beforeClass() throws Exception {
        lzu = new LZU();
    }

    @Test
    public void deployComponent() throws IOException, ClassNotFoundException {
        String name = "Buchungssystem";
        String pathToJar = "C:\\Users\\alexa\\Documents\\WORKSPACE\\OOKA_Buchungssystem\\target\\OOKA_Buchungssystem-1.0-SNAPSHOT.jar";
        lzu.deploy(name, pathToJar);
    }
}
