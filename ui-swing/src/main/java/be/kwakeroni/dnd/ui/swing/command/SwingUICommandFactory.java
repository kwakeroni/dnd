package be.kwakeroni.dnd.ui.swing.command;

import be.kwakeroni.dnd.engine.api.FightController;
import be.kwakeroni.dnd.engine.api.command.fight.FightCommandFactory;
import be.kwakeroni.dnd.engine.api.command.io.IOCommandFactory;
import be.kwakeroni.dnd.ui.base.GUIController;
import be.kwakeroni.dnd.ui.base.PluggableContent;
import be.kwakeroni.dnd.ui.base.command.UICommandFactory;
import be.kwakeroni.dnd.ui.base.config.Directories;
import be.kwakeroni.dnd.ui.base.fight.PluggableFightUI;
import be.kwakeroni.dnd.ui.swing.gui.fight.FightPane;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class SwingUICommandFactory implements UICommandFactory<JFrame, Container> {


    private final FightCommandFactory fightCommandFactory;
    private final IOCommandFactory ioCommandFactory;

    public SwingUICommandFactory(FightCommandFactory fightCommandFactory, IOCommandFactory ioCommandFactory) {
        this.fightCommandFactory = fightCommandFactory;
        this.ioCommandFactory = ioCommandFactory;
    }

    @Override
    public FightCommandFactory getFightCommandFactory() {
        return fightCommandFactory;
    }

    @Override
    public IOCommandFactory getIOCommandFactory() {
        return ioCommandFactory;
    }

    @Override
    public PluggableFightUI<? super JFrame, Container> getFightUI(FightController fightController) {
        return new FightPane(fightController, this.fightCommandFactory, this);
    }

    @Override
    public IOCommandFactory.FileSelection getImportPartyDescriptor() {
        return IMPORT_PARTY_SELECTION;
    }

    @Override
    public IOCommandFactory.FileSelection getExportFightDescriptor() {
        return EXPORT_FIGHT_SELECTION;
    }

    @Override
    public IOCommandFactory.FileSelection getImportFightDescriptor() {
        return IMPORT_FIGHT_SELECTION;
    }

    private static final IOCommandFactory.FileSelection IMPORT_PARTY_SELECTION =
            IOCommandFactory.FileSelection.of(Directories.PARTY_DIRECTORY.name(), "Select Party File", "Import");

    private static final IOCommandFactory.FileSelection IMPORT_FIGHT_SELECTION =
            IOCommandFactory.FileSelection.of(Directories.FIGHT_DIRECTORY.name(), "Select Fight File", "Import");

    private static final IOCommandFactory.FileSelection EXPORT_FIGHT_SELECTION =
            IOCommandFactory.FileSelection.of(Directories.FIGHT_DIRECTORY.name(), "Select Fight File", "Export");
}
