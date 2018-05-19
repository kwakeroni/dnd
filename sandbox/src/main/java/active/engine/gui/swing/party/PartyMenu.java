package active.engine.gui.swing.party;

import active.engine.gui.swing.GUIController;
import active.engine.gui.swing.PluggableMenu;
import active.engine.gui.swing.action.SwingFightActions;
import active.model.event.Datum;
import active.model.event.Reaction;

import javax.swing.*;

import java.awt.*;

import static active.engine.gui.swing.support.KeySupport.ctrl;

public class PartyMenu implements PluggableMenu {

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

    private Container getParentWindow(){
        return this.parent.getParent();
    }

    public PartyMenu(GUIController gui, Datum<Object> content) {

        this.partyMenu = new JMenu("Party");
        partyMenu.setMnemonic('P');

        JMenuItem importParty = new JMenuItem("Import Party...", 'I');
        partyMenu.add(importParty);
        importParty.setAction(SwingFightActions.importPartyFile(this::getParentWindow, gui::getCommandHandler));
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
}
