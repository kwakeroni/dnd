package be.kwakeroni.dnd.ui.swing.gui.party;

import be.kwakeroni.dnd.engine.api.command.Command;
import be.kwakeroni.dnd.event.Datum;
import be.kwakeroni.dnd.ui.base.GUIController;
import be.kwakeroni.dnd.ui.base.PluggableMenu;
import be.kwakeroni.dnd.ui.base.command.UICommandFactory;
import be.kwakeroni.dnd.ui.swing.action.CommandAction;
import be.kwakeroni.dnd.util.function.Reaction;

import javax.swing.*;
import java.awt.*;
import java.util.function.Supplier;

import static be.kwakeroni.dnd.ui.swing.gui.support.KeySupport.ctrl;

public class PartyMenu implements PluggableMenu<JMenuBar> {

    private JMenuBar parent;
    private final JMenu partyMenu;

    @Override
    public void attach(JMenuBar parent) {
        this.parent = parent;
        this.parent.add(partyMenu);
    }

    @Override
    public void detach() {
        this.parent.remove(partyMenu);
        this.parent = null;
    }

    private Container getParentWindow() {
        return this.parent.getParent();
    }

    public PartyMenu(GUIController gui, Datum<Object> content) {

        UICommandFactory<?, ?> uiCommandFactory = gui.getUICommandFactory();

        this.partyMenu = new JMenu("Party");
        partyMenu.setMnemonic('P');

        JMenuItem importParty = new JMenuItem("Import Party...", 'I');
        partyMenu.add(importParty);
        importParty.setAction(execute(uiCommandFactory::importPartiesToFight, gui));
        importParty.setMnemonic('I');
        importParty.setAccelerator(ctrl('I'));
        importParty.setText("Import Party...");

        Reaction refreshState = () -> {
            boolean isFight = content.get() != null;
            importParty.setEnabled(isFight);
        };

        refreshState.react();
        content.onChanged(refreshState);
    }

    private Action execute(Supplier<Command> command, GUIController gui) {
        return new CommandAction(this::component, command, gui::getCommandHandler);
    }

    public JMenu component() {
        return this.partyMenu;
    }

}
