import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class TCPSocketClient {
    private Socket socket = null;
    private ObjectInputStream inputStream = null;
    private ObjectOutputStream outputStream = null;
    private boolean isConnected = false;

    private TCPSocketClient() {
    }

    private void communicate() {

        while (!isConnected) {
            try {
                socket = new Socket("localHost", 4445);
                System.out.println("Connected");
                isConnected = true;
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                TransferObject transferObject = new TransferObject(readFiles(),"stop");
                System.out.println("Object to be written = " + transferObject);
                outputStream.writeObject(transferObject);

            } catch (SocketException se) {
                se.printStackTrace();
// System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private ArrayList<MyFile> readFiles() {
        ArrayList<MyFile> listFiles = new ArrayList<MyFile>();

        File folder = new File("D:\\test");
        File[] arrFiles = folder.listFiles();
        FileInputStream fileInputStream = null;

        if (arrFiles != null) {
            for (File arrFile : arrFiles) {

                if (arrFile.isFile()) {
                    byte[] bFile = new byte[(int) arrFile.length()];

                    //convert file into array of bytes
                    try {
                        fileInputStream = new FileInputStream(arrFile);
                        fileInputStream.read(bFile);
                        fileInputStream.close();
                        for (byte aBFile : bFile) {
                            System.out.print((char) aBFile);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    listFiles.add(new MyFile(arrFile.getName(), bFile));
                }
            }
        }

        return listFiles;
    }

    public static void main(String[] args) {
        new TCPSocketClient().communicate();
    }
}