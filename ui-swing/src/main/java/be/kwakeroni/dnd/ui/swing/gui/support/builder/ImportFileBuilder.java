package be.kwakeroni.dnd.ui.swing.gui.support.builder;

import javax.swing.JFileChooser;
import java.awt.Component;
import java.io.File;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class ImportFileBuilder {

    private Component parent;
    private JFileChooser fileChooser;
    private Consumer<File> finisher = file -> {};

    private ImportFileBuilder(){
        this.fileChooser = new JFileChooser();
    }

    public static ImportFileBuilder selectFile() {
        return new ImportFileBuilder();
    }

    public ImportFileBuilder forComponent(Component parent){
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

    public ImportFileBuilder onConfirmation(Consumer<File> finisher) {
        this.finisher = this.finisher.andThen(finisher);
        return this;
    }

    public void andThen(Consumer<File> action){
        select().ifPresent(action);
    }

    public Optional<File> select() {
        switch ( this.fileChooser.showDialog(this.parent, null) ){
            case JFileChooser.APPROVE_OPTION :
                File file = this.fileChooser.getSelectedFile();
                this.finisher.accept(file);
                return Optional.of(file);
            default :
                return Optional.empty();
        }
    }
}
