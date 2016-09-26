package active.engine.gui;

import sun.nio.fs.DefaultFileSystemProvider;

import javax.swing.SwingUtilities;
import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class Config {

    public static Config getInstance(){
        try {
            Path userDir = Paths.get(System.getProperty("user.dir"));
        } catch (Exception exc){
            exc.printStackTrace();
        }
        return null;
    }

}
