package hbrs.ooka;

import java.io.IOException;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App 
{
    private static final Scanner scanner = new Scanner(System.in);

    private static final LZU lzu = new LZU();

    public static void main( String[] args )
    {
        System.out.println( "####### LZU für Software-Komponenten #######" );

        boolean quit = false;
        while (!quit){

            System.out.print("Command: ");
            String command = scanner.nextLine();

            if(command.equals("q")){
                quit = true;
            } else if(command.contains("deploy")){
                String[] s = command.split(" ");
                if(s.length < 3){
                    System.out.println("Please provide a component name and the path to the jar file!");
                } else {
                    String name = s[1];
                    String pathToJar = s[2];
                    try {
                        lzu.deployComponent(name, pathToJar);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }

            } else {
                System.out.println("Command not found...");
            }
        }

    }
}
