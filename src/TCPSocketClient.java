import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class TCPSocketClient {
    private Socket socket = null;
    private ObjectInputStream inputStream = null;
    private ObjectOutputStream outputStream = null;
    private boolean isConnected = false;

    TCPSocketClient() {
    }

    public static void main(String[] args) {
        new TCPSocketClient().communicate();
    }

    void communicate() {
        ArrayList<File> files = readFiles();
        while (!isConnected) {
            try {
                socket = new Socket("localHost", 4445);
                System.out.println("Connected");
                isConnected = true;
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                TransferObject transferObject = new TransferObject(files, "Don`t stop");
                System.out.println("Object to be written = " + transferObject);
                outputStream.writeObject(transferObject);
            } catch (SocketException se) {
                se.printStackTrace();
// System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (File file : files) {
            communicate2(file);
        }
    }

    void communicate2(File file) {
        Socket writeSocket = null;
        try {
            writeSocket = new Socket("localhost", 4446);
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(writeSocket.getOutputStream()));
            int n = 0;
            byte[] buf = new byte[4092];

            String fileName = file.getName();


            //write file names
            dos.writeUTF(fileName);
            dos.writeLong(file.length());

            //create new fileinputstream for each file
            FileInputStream fis = new FileInputStream(file);
            int count = 0;
            //write file to dos
            System.out.println(fileName);
            System.out.println("file.length() " + file.length());
            while ((n = fis.read(buf)) != -1) {
                dos.write(buf, 0, n);
                count += n;
                if (count % (file.length()/10) == 0) {
                    System.out.println("Передано: " + readableFileSize(count));
                }
            }
            System.out.println("Всього передано: " + readableFileSize(count));
            System.out.println("Або: " + count + " байт\n");

            dos.flush();
            dos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private ArrayList<File> readFiles() {
        ArrayList<File> listFiles = new ArrayList<File>();

        File folder = new File("D:\\test");
        File[] arrFiles = folder.listFiles();
        if (arrFiles != null) {
            for (File arrFile : arrFiles) {
                if (arrFile.isFile()) {
                    listFiles.add(arrFile);
                }
            }
        }

        return listFiles;
    }

    public static String readableFileSize(long size) {
        if(size <= 0) return "0";
        final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }


}