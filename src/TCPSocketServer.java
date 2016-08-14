import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class TCPSocketServer {
    private ServerSocket serverSocket = null;
    private Socket socket = null;
    private ObjectInputStream inStream = null;
    private OutputStream ouStream = null;

    private TCPSocketServer() {

    }

    public void communicate() {

        try {
            serverSocket = new ServerSocket(4445);
            while (true) {
                socket = serverSocket.accept();
                System.out.println("Connected");

                inStream = new ObjectInputStream(socket.getInputStream());

                TransferObject transferObject = (TransferObject) inStream.readObject();

                System.out.println("Object received = " + transferObject);
                writeFile(transferObject.getFiles());

                if (transferObject.getMessage().equals("stop")) {
                    break;
                }
            }
            socket.close();
        } catch (SocketException se) {
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException cn) {
            cn.printStackTrace();
        }
    }

    private void writeFile(ArrayList<MyFile> files){
        try {
            for (MyFile file : files) {

                File newFile = new File("D:\\test\\result\\" + file.getFileName());
                // if file doesnt exists, then create it
                if (!newFile.exists()) {
                    newFile.createNewFile();
                }

                FileOutputStream out = new FileOutputStream(newFile);
                out.write(file.getByteArray());
                System.out.println("Done");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new TCPSocketServer().communicate();
    }
}