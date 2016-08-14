import java.io.Serializable;

/**
 * Created with Intellij IDEA.
 * Project name: socketObjectTransfer.
 * Date: 14.08.2016.
 * Time: 11:28.
 * To change this template use File|Setting|Editor|File and Code Templates.
 */
class MyFile implements Serializable{
    private String fileName;
    private byte[] byteArray;

    public MyFile(String fileName, byte[] byteArray) {
        this.fileName = fileName;
        this.byteArray = byteArray;
    }

    public MyFile() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getByteArray() {
        return byteArray;
    }

    public void setByteArray(byte[] byteArray) {
        this.byteArray = byteArray;
    }
}
