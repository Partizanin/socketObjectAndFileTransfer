/**
 * Created with Intellij IDEA.
 * Project name: socketObjectTransfer.
 * Date: 14.08.2016.
 * Time: 12:00.
 * To change this template use File|Setting|Editor|File and Code Templates.
 */
public class Main {
    public static void main(String[] args) {
        new Thread() {
            @Override
            public void run() {

                new TCPSocketServer().communicate();
            }
        }.start();

        new TCPSocketClient().communicate();
    }
}
