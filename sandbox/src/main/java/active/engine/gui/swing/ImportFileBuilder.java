package active.engine.gui.swing;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import java.awt.Component;
import java.io.File;
import java.nio.file.Path;
import java.util.function.Consumer;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class ImportFileBuilder {

    private Component parent;
    private JFileChooser fileChooser;

    private ImportFileBuilder(){
        this.fileChooser = new JFileChooser();
    }

    public static ImportFileBuilder selectFile() {
        return new ImportFileBuilder();
    }

    public ImportFileBuilder forWindow(Component parent){
        this.parent = parent;
        return this;
    }

    public ImportFileBuilder forInput(){
        this.fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        return this;
    }

    public ImportFileBuilder forOutput(){
        this.fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        return this;
    }

    public ImportFileBuilder withTitle(String title){
        this.fileChooser.setDialogTitle(title);
        return this;
    }

    public ImportFileBuilder withButton(String button){
        this.fileChooser.setApproveButtonText(button);
        return this;
    }

    public ImportFileBuilder withStartDirectory(Path dir){
        if (dir != null){
            return withStartDirectory(dir.toFile());
        } else {
            return this;
        }
    }

    public ImportFileBuilder withStartDirectory(File dir){
        if (dir != null) {
            this.fileChooser.setCurrentDirectory(dir);
        }
        return this;
    }

    public void andThen(Consumer<File> action){
        switch ( this.fileChooser.showDialog(this.parent, null) ){
            case JFileChooser.APPROVE_OPTION :
                action.accept(this.fileChooser.getSelectedFile());
                break;
            default :
        }
    }
}
