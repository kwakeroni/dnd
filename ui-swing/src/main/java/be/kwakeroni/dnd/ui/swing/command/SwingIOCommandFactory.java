package be.kwakeroni.dnd.ui.swing.command;

import be.kwakeroni.dnd.engine.api.command.Command;
import be.kwakeroni.dnd.engine.api.command.CommandContext;
import be.kwakeroni.dnd.engine.api.command.io.IOCommandFactory;
import be.kwakeroni.dnd.io.xml.XMLInput;
import be.kwakeroni.dnd.io.xml.XMLOutput;
import be.kwakeroni.dnd.model.creature.Party;
import be.kwakeroni.dnd.model.fight.Fight;
import be.kwakeroni.dnd.ui.base.config.Directories;
import be.kwakeroni.dnd.ui.swing.gui.SwingConfigProvider;
import be.kwakeroni.dnd.ui.swing.gui.SwingUIContext;
import be.kwakeroni.dnd.ui.swing.gui.support.builder.ImportFileBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

public class SwingIOCommandFactory implements IOCommandFactory {


//
//    @Override
//    public Command.WithOptionalResult<File> getActiveOrSelect(FileSelection selection) {
//        Directories directory = Directories.valueOf(selection.getKind());
//        return context ->
//                Optional.ofNullable(context.getContext(CurrentFightFile.class))
//                   .map(CurrentFightFile::getFile)
//                   .or(() -> showFileSelectionDialog(context, directory, selection));
//    }

    @Override
    public Command.WithOptionalResult<File> selectFile(FileSelection selection) {
        Directories directory = Directories.valueOf(selection.getKind());
        return context -> showFileSelectionDialog(context, directory, selection);
    }

    private Optional<File> showFileSelectionDialog(CommandContext context, Directories directory, FileSelection selection){
        return ImportFileBuilder
                .selectFile()
                .forInput()
                .forComponent(context.getContext(SwingUIContext.class).getActiveComponent())
                .withTitle(selection.getTitle())
                .withButton(selection.getActionText())
                .withStartDirectory(SwingConfigProvider.getConfig().get(directory))
//                .onConfirmation(file -> adaptConfigTo(context, directory, file))
                .select();
    }
//
//    private void adaptConfigTo(CommandContext context, Directories directory, File selected) {
//        SwingConfigProvider.getConfig().set(directory, selected.getParentFile().toPath());
//        if (activeFiles.containsKey(directory)){
//            SwingConfigProvider.setLocalProperty(activeFiles.get(directory), selected);
//        }
//    }



    @Override
    public Command.WithResult<Collection<Party>> importParties(File file) {
        return context -> {
            try {
                return XMLInput.readParties(file);
            } catch (IOException exc) {
                exc.printStackTrace();
                return Collections.emptyList();
            }
        };
    }

    @Override
    public Command.WithResult<Fight> importFight(File file) {
        return context -> {
            try {
                return XMLInput.readFight(file);
            } catch (IOException exc) {
                exc.printStackTrace();
                return null;
            }
        };
    }

    @Override
    public Command exportFight(Fight fight, File file) {
        return context -> {
            try {
                XMLOutput.writeToFile(fight, file);
            } catch (IOException exc) {
                exc.printStackTrace();
            }
        };
    }

}
