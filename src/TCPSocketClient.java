import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

public class TCPSocketClient {

    private TCPSocketClient() {
    }

    private void communicate() {
        try {
            Socket socket = new Socket("localhost", 4545);
            InputStream inStream = socket.getInputStream();
            OutputStream ouStream = socket.getOutputStream();
            while (socket.isConnected()) {
                String messageString = "Hello ...This is from client";
                ouStream.write(messageString.getBytes());
                System.out.println("Message has written to socket");
                byte[] byteBuffer = new byte[1024];
                int numBytes = inStream.read(byteBuffer);
                byte[] tempBuffer = new byte[numBytes];
                System.arraycopy(byteBuffer, 0, tempBuffer, 0, numBytes);
                String message = new String(tempBuffer, "UTF-8");
                System.out.println("Message from server = " + message);
                Thread.sleep(2000);
                socket.close();
            }
        } catch (SocketException se) {
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new TCPSocketClient().communicate();
    }
}