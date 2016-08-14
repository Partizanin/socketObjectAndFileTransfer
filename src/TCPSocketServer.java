import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class TCPSocketServer {
    private ServerSocket serverSocket = null;
    private ServerSocket readServerSocket = null;
    private Socket socket = null;
    private Socket readSocket = null;
    private ObjectInputStream inStream = null;
    private InputStream readInStream = null;
    private OutputStream readOuStream;
    private TransferObject transferObject;
    private Utils utils = new Utils();

    TCPSocketServer() {
        communicate2();

    }

    public static void main(String[] args) {
        new TCPSocketServer().communicate();

    }

    private void communicate2() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                int fileCounter = -1;
                try {
                    readServerSocket = new ServerSocket(4446);
                    while (true) {
                        fileCounter++;
                        readSocket = readServerSocket.accept();
                        readInStream = readSocket.getInputStream();
                        DataInputStream dataInputStream = new DataInputStream(readInStream);

                        String fileName = null;
                        fileName = dataInputStream.readUTF();
                        String filePath = "D:\\test\\result\\";
                        filePath += fileName;
                        readOuStream = new FileOutputStream(filePath);


                        long size = 0;
                        size = dataInputStream.readLong();
                        long size2 = size;
                        long count = 0;
                        byte[] buffer = new byte[4092];
                        int bytesRead;
                        try {
                            while (size > 0 && (bytesRead = dataInputStream.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                                readOuStream.write(buffer, 0, bytesRead);
                                count += bytesRead;
                                size -= bytesRead;
                                if (count % (size2) == 0) {
                                    System.out.println(utils.getCurrentDateTime() + " Передано: " + utils.readableFileSize(count));
                                }
                            }
                            File file = transferObject.getFiles().get(fileCounter);
                            System.out.println(file.getName());
                            System.out.println("Всього передано: " + utils.readableFileSize(count));
                            System.out.println("Або: " + file.length() + " байт\n");

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        readOuStream.flush();

                        readInStream.close();
                        readOuStream.close();
                        readSocket.close();
                        if (fileCounter == transferObject.getFiles().size() - 1) {
                            fileCounter = 0;
                        }

                    }

                } catch (SocketException se) {
                    se.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };

        thread.start();

    }

    void communicate() {
        try {
            serverSocket = new ServerSocket(4445);
            System.out.println(utils.getCurrentDateTime() + " Запуск сервера");
            while (true) {
                System.out.println(utils.getCurrentDateTime() + " Очікування підключення");
                socket = serverSocket.accept();
                System.out.println(utils.getCurrentDateTime() + " " + socket.getInetAddress() + " Клієнт підключено \n");

                inStream = new ObjectInputStream(socket.getInputStream());

                transferObject = (TransferObject) inStream.readObject();

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


}