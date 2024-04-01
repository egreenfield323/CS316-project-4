package Client;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {

    public static void main(String[] args) {
        // expect two command-line args from user
        if (args.length != 2) {
            System.out.println("Syntax: Client.Client <serverIP> <serverPort>");
            return;
        }

        Scanner keyboard = new Scanner(System.in);
        String command;
        int numThreads = 8;
        ExecutorService es = Executors.newFixedThreadPool(numThreads);

        System.out.println("""
                Input a command
                1) L - list all files
                2) D - remove a file
                3) R - rename a file
                4) G - download a file
                5) U - upload a file""");

        do {
            command = keyboard.nextLine().toUpperCase();
            switch(command){
                case "D", "G", "U":
                    System.out.println("Please enter the name of the file: ");
                    command += keyboard.nextLine();
                    break;
                case("R"):
                    System.out.println("Please enter the name of the file you wish to rename: ");
                    command += keyboard.nextLine();
                    System.out.println("Now please enter the new name for that file: ");
                    command += ":" + keyboard.nextLine();
                    break;
            }

            if (!command.equalsIgnoreCase("q")) {
                ClientTask ct = new ClientTask(command, args, keyboard);
                es.submit(ct);
            }
        } while (!command.equalsIgnoreCase("q"));
        System.out.println("Exiting program...");
        es.shutdown();
    }
}
