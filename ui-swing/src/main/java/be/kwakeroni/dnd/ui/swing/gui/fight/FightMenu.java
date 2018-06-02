package be.kwakeroni.dnd.ui.swing.gui.fight;

import be.kwakeroni.dnd.engine.api.command.Command;
import be.kwakeroni.dnd.engine.api.command.fight.FightCommandFactory;
import be.kwakeroni.dnd.event.Datum;
import be.kwakeroni.dnd.ui.base.GUIController;
import be.kwakeroni.dnd.ui.base.PluggableMenu;
import be.kwakeroni.dnd.ui.base.command.UICommandFactory;
import be.kwakeroni.dnd.ui.swing.action.CommandAction;
import be.kwakeroni.dnd.ui.swing.console.ConsoleFightLogger;
import be.kwakeroni.dnd.util.function.Reaction;

import javax.swing.*;
import java.awt.*;
import java.util.function.Supplier;

import static be.kwakeroni.dnd.ui.swing.gui.support.KeySupport.ctrl;
import static be.kwakeroni.dnd.ui.swing.gui.support.KeySupport.shift;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class FightMenu implements PluggableMenu<JMenuBar> {

    private JMenuBar parent;
    private final JMenu fightMenu;

    public void attach(JMenuBar parent) {
        this.parent = parent;
        this.parent.add(fightMenu);

    }

    public void detach() {
        this.parent.remove(fightMenu);
        this.parent = null;
    }

    private Container getParentWindow() {
        return this.parent.getParent();
    }

    public FightMenu(GUIController gui, Datum<Object> content) {
        FightCommandFactory fightCommandFactory = gui.getFightCommandFactory();
        UICommandFactory uiCommandFactory = gui.getUICommandFactory();

        this.fightMenu = new JMenu("Fight");
        fightMenu.setMnemonic('F');

        JMenuItem newFight = new JMenuItem("New Fight", 'N');
        fightMenu.add(newFight);
        newFight.setAction(execute(uiCommandFactory::startFight, gui));
        newFight.setMnemonic('N');
        newFight.setAccelerator(ctrl('N'));
        newFight.setText("New Fight");

        JMenuItem importFight = new JMenuItem("Import Fight...", 'I');
        fightMenu.add(importFight);
        importFight.setAction(execute(uiCommandFactory::importFight, gui));
        importFight.setMnemonic('I');
        importFight.setText("Import Fight...");


        JMenuItem endFight = new JMenuItem("End Fight", 'E');
        fightMenu.add(endFight);
        endFight.setAction(execute(uiCommandFactory::endFight, gui));
        endFight.setMnemonic('E');
        endFight.setText("End Fight");


        JMenuItem saveFight = new JMenuItem("Save Fight", 'S');
        fightMenu.add(saveFight);
        saveFight.setAction(execute(uiCommandFactory::exportFight, gui));
        saveFight.setMnemonic('S');
        saveFight.setAccelerator(ctrl('S'));
        saveFight.setText("Save Fight");

        JMenuItem saveFightAs = new JMenuItem("Save Fight As...", 'S');
        fightMenu.add(saveFightAs);
        saveFightAs.setAction(execute(uiCommandFactory::exportFightAs, gui));
        saveFightAs.setMnemonic('A');
        saveFightAs.setAccelerator(ctrl(shift('S')));
        saveFightAs.setText("Save Fight As...");


        Reaction refreshState = () -> {
            boolean isFight = content.get() != null;
            newFight.setEnabled(content.get() == null);
            importFight.setEnabled(content.get() == null);
            endFight.setEnabled(isFight);
            saveFight.setEnabled(isFight);
            saveFightAs.setEnabled(isFight);
        };

        refreshState.react();
        content.onChanged(refreshState);


    }

    private Action execute(Supplier<Command> command, GUIController gui) {
        return new CommandAction(this::component, command, gui::getCommandHandler);
    }

    public JMenu component() {
        return this.fightMenu;
    }

}
