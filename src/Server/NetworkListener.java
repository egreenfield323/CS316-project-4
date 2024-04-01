package Server;

import java.net.InetSocketAddress;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;

public class NetworkListener implements Runnable {
    public final ExecutorService es;
    private final int port;

    public NetworkListener(ExecutorService es, int port) {
        this.es = es;
        this.port = port;
    }

    public void run() {
        try {
            ServerSocketChannel welcomeChannel = ServerSocketChannel.open();
            welcomeChannel.socket().bind(new InetSocketAddress(this.port));

            while (true) {
                SocketChannel serveChannel = welcomeChannel.accept();
                ServerAcceptTask task = new ServerAcceptTask(serveChannel);
                es.submit(task);
            }

        } catch (ClosedByInterruptException e) {
            System.out.println("listening task has been forcefully terminated");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
