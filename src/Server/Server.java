package Server;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Server {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Please specify server port.");
            return;
        }

        int port = Integer.parseInt(args[0]);
        int numThreads = 8;

        ExecutorService es = Executors.newFixedThreadPool(numThreads);
        NetworkListener networkListenerTask = new NetworkListener(es, port);
        Future<?> f = es.submit(networkListenerTask);

        String command;
        do {
            Scanner in = new Scanner(System.in);
            command = in.nextLine();
        } while (!command.equalsIgnoreCase("Q"));
        f.cancel(true);
        es.shutdown();
    }
}