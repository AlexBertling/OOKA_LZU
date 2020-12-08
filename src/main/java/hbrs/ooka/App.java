package hbrs.ooka;

import hbrs.ooka.lzu.LZU;

import java.io.IOException;
import java.util.Scanner;

public class App 
{
    private static final Scanner scanner = new Scanner(System.in);

    private static final LZU lzu = new LZU();

    public static void main( String[] args )
    {
        System.out.println( "####### LZU für Software-Komponenten #######" );

        lzu.start();

        boolean exit = false;
        while (!exit){

            String command = scanner.nextLine();

            if(command.contains("deploy")){
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

            } else if(command.contains("remove")){
                String[] s = command.split(" ");
                if(s.length < 2){
                    System.out.println("Please provide a component name!");
                } else {
                    String name = s[1];
                    lzu.removeComponent(name);
                }

            } else if(command.contains("start")){
                String[] s = command.split(" ");
                if(s.length < 2){
                    System.out.println("Please provide a component name!");
                } else {
                    String name = s[1];
                    lzu.startComponent(name);
                }
            } else if(command.contains("stop")){
                String[] s = command.split(" ");
                if(s.length < 3){
                    System.out.println("Please provide a component name and an instanceId!");
                } else {
                    String name = s[1];
                    String instanceId = s[2];
                    if(instanceId.equals("all")){
                        lzu.stopComponent(name);
                    } else {
                        lzu.stopComponent(name, instanceId);
                    }
                }
            } else if(command.contains("exit")){
                lzu.stop();
                exit = true;
            }

            else {
                System.out.println("Command not found...");
            }
        }

    }
}
