package Client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class ClientTask implements Runnable {

    private final String[] args;
    private final int serverPort;
    private final String command;

    public ClientTask(String c, String[] args, Scanner k) {
        this.command = c;
        this.args = args;
        this.serverPort = Integer.parseInt(args[1]);
    }

    public void run() {
        String commandChar = command.substring(0, 1);
        ByteBuffer commandBuffer;
        SocketChannel channel;
        String fileName;
        try {

            switch (commandChar) {
                case "L":
                    commandBuffer = ByteBuffer.wrap(command.getBytes());
                    channel = SocketChannel.open();
                    sendRequest(channel, args, serverPort, commandBuffer);

                    System.out.println(new String(displayReply(channel)));
                    channel.close();
                    break;
                case "D":
                    commandBuffer = ByteBuffer.wrap(command.getBytes());
                    channel = SocketChannel.open();
                    sendRequest(channel, args, serverPort, commandBuffer);

                    System.out.println(new String(displayReply(channel)));
                    channel.close();
                    break;
                case "R":
                    commandBuffer = ByteBuffer.wrap(command.getBytes());
                    channel = SocketChannel.open();
                    sendRequest(channel, args, serverPort, commandBuffer);

                    System.out.println(new String(displayReply(channel)));
                    channel.close();
                    break;
                case "G":
                    fileName = command.substring(1);
                    commandBuffer = ByteBuffer.wrap(command.getBytes());
                    channel = SocketChannel.open();
                    sendRequest(channel, args, serverPort, commandBuffer);

                    if (new String(displayReply(channel)).equals("F")) {
                        System.out.println("Could not find that file.");
                    } else {
                        channel.write(ByteBuffer.wrap("R".getBytes()));
                        FileOutputStream newFileStream = new FileOutputStream("src/files/" + fileName, true);
                        FileChannel fc = newFileStream.getChannel();

                        ByteBuffer content = ByteBuffer.allocate(1024);

                        while (channel.read(content) >= 0) {
                            content.flip();
                            fc.write(content);
                            content.clear();
                        }

                        newFileStream.close();
                        System.out.println("File successfully downloaded!");
                    }

                    channel.close();
                    break;
                case "U":
                    fileName = command.substring(1);
                    File f = new File("src/files/" + fileName);
                    if (f.exists()) {
                        commandBuffer = ByteBuffer.wrap(command.getBytes());
                        channel = SocketChannel.open();
                        sendRequest(channel, args, serverPort, commandBuffer);

                        if (new String(displayReply(channel)).equals("S")) {

                            FileInputStream fs = new FileInputStream(f);
                            FileChannel fc = fs.getChannel();
                            int bufferSize = 1024;
                            if (bufferSize > fc.size()) {
                                bufferSize = (int) fc.size();
                            }

                            ByteBuffer fileContent = ByteBuffer.allocate(bufferSize);
                            while (fc.read(fileContent) >= 0) {
                                channel.write(fileContent.flip());
                                fileContent.clear();
                            }
                            channel.shutdownOutput();
                            channel.close();
                            System.out.println("File successfully uploaded");
                        }
                    } else {
                        System.out.println("File not in current directory");
                    }
                    break;
                default:
                    if (!command.equals("0")) {
                        System.out.println("Invalid command");
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendRequest(SocketChannel channel, String[] args, int serverPort, ByteBuffer queryBuffer) throws IOException {
        channel.connect(new InetSocketAddress(args[0], serverPort));
        channel.write(queryBuffer);
    }

    private static byte[] displayReply(SocketChannel channel) throws IOException {
        ByteBuffer replyBuffer = ByteBuffer.allocate(1024);
        int bytesRead = channel.read(replyBuffer);

        replyBuffer.flip();
        byte[] replyArray = new byte[bytesRead];
        replyBuffer.get(replyArray);

        return replyArray;
    }
}
