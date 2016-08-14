import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class TCPSocketClient {
    private Socket socket = null;
    private ObjectOutputStream outputStream = null;
    private boolean isConnected = false;
    private Utils utils = new Utils();

    TCPSocketClient() {
    }

    public static void main(String[] args) {
        new TCPSocketClient().communicate();
    }

    void communicate() {
        ArrayList<File> files = readFiles();
        while (!isConnected) {
            try {
                System.out.println(utils.getCurrentDateTime() + " Підключення до сервера " + "IP - localHost:4445");
                socket = new Socket("localHost", 4445);
                System.out.println(utils.getCurrentDateTime() + " Підключення встановлено\n");
                isConnected = true;
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                TransferObject transferObject = new TransferObject(files, "Don`t stop");
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

    private void communicate2(File file) {
        Socket writeSocket = null;
        try {
            System.out.println(utils.getCurrentDateTime() + " Підключення до сервера " + "IP - localHost:4446");
            writeSocket = new Socket("localhost", 4446);
            System.out.println(utils.getCurrentDateTime() + " Підключення встановлено\n");
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(writeSocket.getOutputStream()));
            int n = 0;
            byte[] buf = new byte[1024];

            String fileName = file.getName();


            //write file names
            dos.writeUTF(fileName);
            //write file length
            dos.writeLong(file.length());

            //create new fileinputstream for each file
            FileInputStream fis = new FileInputStream(file);
            int count = 0;
            System.out.println(utils.getCurrentDateTime() + " Передача файлу " + fileName + " розпочато");
            //write file to dos
            // /*102 323 900*/
            int logCounter = utils.getLogCounter(file.length());
            while ((n = fis.read(buf)) != -1) {
                dos.write(buf, 0, n);
                count += n;
                if (count % (1024 * (1024 * logCounter)) == 0) {
                    System.out.println("Передано: " + utils.readableFileSize(count));
                }
            }
            System.out.println(utils.getCurrentDateTime() + " Передача файлу " + fileName + " закінчено");
            System.out.println("Всього передано: " + utils.readableFileSize(count));
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
                    System.out.println(utils.getCurrentDateTime() + " Знайдено файл " + arrFile.getName() + " " + utils.readableFileSize(arrFile.length()));
                    listFiles.add(arrFile);
                }
            }
        }
        System.out.println();
        return listFiles;
    }

}