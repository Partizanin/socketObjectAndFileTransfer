import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created with Intellij IDEA.
 * Project name: socketObjectTransfer.
 * Date: 14.08.2016.
 * Time: 9:47.
 * To change this template use File|Setting|Editor|File and Code Templates.
 */
public class TranserObject implements Serializable {

    private static final long serialVersionUID = 5950169519310163575L;

    private ArrayList<File> files = new ArrayList<File>();

    public TranserObject(ArrayList<File> files) {
        this.files = files;
    }

    public TranserObject() {

    }

    public void addFile(File file){
        files.add(file);
    }

    public ArrayList<File> getFiles() {
        return files;
    }

}
