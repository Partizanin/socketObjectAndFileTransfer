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

//        File oldFilePath = new File("D:\\ARMVZ_SL\\Obmen\\Export\\mc091113.152");
//        oldFilePath.delete();


    }
/*D:\ARMVZ_SL\Obmen\Export\mc091113.152
* D:\ARMVZ_SL\Obmen\Export\mc091113.152*/
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
                socket.close();
                outputStream.close();

            } catch (SocketException se) {
                se.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (File file : files) {
            communicate2(file);
        }

        transferFileToArchive(files);
    }

    private void transferFileToArchive(ArrayList<File> files) {
        /*D:\ARMVZ_SL\Obmen\Export\Archiv*/

        for (File file : files) {

            InputStream inStream = null;
            OutputStream outStream = null;
            File oldFilePath = null;

            try {

                oldFilePath = new File(file.getPath());
                File newFilePath = new File("D:\\ARMVZ_SL\\Obmen\\Export\\Archiv\\" + file.getName());

                inStream = new FileInputStream(oldFilePath);
                outStream = new FileOutputStream(newFilePath);

                byte[] buffer = new byte[1024];

                int length;
                //copy the file content in bytes
                while ((length = inStream.read(buffer)) > 0) {
                    outStream.write(buffer, 0, length);
                }

                System.out.println("File " + file.getName() + " is copied successful!");

                inStream.close();
                outStream.close();
                file.delete();

                //delete the original file

            } catch (IOException e) {
                e.printStackTrace();
            }

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
            fis.close();
            writeSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*mput C:\Armvz_sl\obmen\export\mc09*
    mput C:\Armvz_sl\obmen\export\dc09*
    mput C:\Armvz_sl\obmen\export\d*.dbf
    mput C:\Armvz_sl\obmen\export\0*.dbf

    cd /incoming/PEREDPLATA
    mput C:\Armvz_sl\obmen\export\1*.dbf
    mput C:\Armvz_sl\obmen\export\5*.dbf
    mput C:\Armvz_sl\obmen\export\SP*.dbf*/

    private ArrayList<File> fileFilter(File[] allFiles) {
        ArrayList<File> files = new ArrayList<File>();

        for (File allFile : allFiles) {

            String name = allFile.getName();

            if (name.toLowerCase().matches(".*" + "mc09" + ".*")) {
                System.out.println(utils.getCurrentDateTime() +
                        " Знайдено файл " + allFile.getName() + " " + utils.readableFileSize(allFile.length()));
                files.add(allFile);
            } else if (name.toLowerCase().matches(".*" + "dc09" + ".*")) {
                System.out.println(utils.getCurrentDateTime() +
                        " Знайдено файл " + allFile.getName() + " " + utils.readableFileSize(allFile.length()));
                files.add(allFile);
            } else if (name.toLowerCase().matches(".*" + "d*.dbf" + ".*")) {
                System.out.println(utils.getCurrentDateTime() +
                        " Знайдено файл " + allFile.getName() + " " + utils.readableFileSize(allFile.length()));
                files.add(allFile);
            } else if (name.toLowerCase().matches(".*" + "0*.dbf" + ".*")) {
                System.out.println(utils.getCurrentDateTime() +
                        " Знайдено файл " + allFile.getName() + " " + utils.readableFileSize(allFile.length()));
                files.add(allFile);
            } else if (name.toLowerCase().matches(".*" + "1*.dbf" + ".*")) {
                System.out.println(utils.getCurrentDateTime() +
                        " Знайдено файл " + allFile.getName() + " " + utils.readableFileSize(allFile.length()));
                files.add(allFile);
            } else if (name.toLowerCase().matches(".*" + "5*.dbf" + ".*")) {
                System.out.println(utils.getCurrentDateTime() +
                        " Знайдено файл " + allFile.getName() + " " + utils.readableFileSize(allFile.length()));
                files.add(allFile);
            } else if (name.toLowerCase().matches(".*" + "SP*.dbf" + ".*")) {
                System.out.println(utils.getCurrentDateTime() +
                        " Знайдено файл " + allFile.getName() + " " + utils.readableFileSize(allFile.length()));
                files.add(allFile);
            }

        }
        System.out.println();
        return files;
    }

    private ArrayList<File> readFiles() {
        ArrayList<File> listFiles = new ArrayList<File>();

        File folder = new File("D:\\ARMVZ_SL\\Obmen\\Export");
        File[] arrFiles = folder.listFiles();
        if (arrFiles != null) {
            listFiles = fileFilter(arrFiles);
        }
        return listFiles;
    }

}