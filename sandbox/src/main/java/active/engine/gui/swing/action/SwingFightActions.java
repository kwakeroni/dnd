package active.engine.gui.swing.action;

import active.engine.command.CommandHandler;
import active.engine.gui.business.IOActions;
import active.engine.gui.config.Directories;
import active.engine.gui.swing.ImportFileBuilder;
import active.engine.gui.swing.SwingConfigProvider;
import active.io.xml.XMLInput;
import active.model.fight.FightController;
import active.model.fight.event.FightData;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.function.Supplier;

import static active.engine.gui.swing.ImportFileBuilder.selectFile;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class SwingFightActions {

    public static Action importPartyFile(Supplier<Component> parent, Supplier<CommandHandler> handler) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectFile()
                        .forInput()
                        .forWindow(parent.get())
                        .withTitle("Select Party File")
                        .withButton("Import")
                        .withStartDirectory(SwingConfigProvider.getConfig().get(Directories.IMPORT_PARTY_DIRECTORY))
                        .andThen(file -> {
                            SwingConfigProvider.getConfig().set(Directories.IMPORT_PARTY_DIRECTORY, file.getParentFile().toPath());
                            IOActions.importParties(file, handler.get());
                        } );
            }
        };
    }

}
