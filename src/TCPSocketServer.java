import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class TCPSocketServer {
    private ServerSocket serverSocket = null;
    private Socket socket = null;
    private InputStream inStream = null;
    private OutputStream ouStream = null;

    private TCPSocketServer() {

    }

    private void communicate() {
        try {
            serverSocket = new ServerSocket(4545);
            while (true) {
                socket = serverSocket.accept();
                inStream = socket.getInputStream();
                ouStream = socket.getOutputStream();
                byte[] readBuffer = new byte[1024];
                int numBytes = inStream.read(readBuffer);
                byte[] tempBuffer = new byte[numBytes];
                System.arraycopy(readBuffer, 0, tempBuffer, 0, numBytes);
                String message = new String(tempBuffer, "UTF-8");
                System.out.println("message from client = " + message);
                String reply = "Thank you !!.This is from server";
                byte[] replyBytes = reply.getBytes();
                ouStream.write(replyBytes);
                System.out.println("reply has written to socket");
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
        new TCPSocketServer().communicate();
    }
}