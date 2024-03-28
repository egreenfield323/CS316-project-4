import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class ClientUploadTask implements Runnable {

    private final FileChannel fc;

    public ClientUploadTask(File f) throws FileNotFoundException {
        FileInputStream fs = new FileInputStream(f);
        this.fc = fs.getChannel();
    }

    public void run() {
        try {
            SocketChannel channel = SocketChannel.open();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

