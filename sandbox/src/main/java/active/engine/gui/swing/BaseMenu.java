package active.engine.gui.swing;

import active.engine.gui.swing.fight.FightMenu;
import active.engine.gui.swing.party.PartyMenu;
import active.model.event.Datum;

import javax.swing.*;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class BaseMenu {

    private JMenuBar parentMenu;
    private JMenu menu;


    public BaseMenu(GUIController gui, Datum<Object> content) {

        this.parentMenu = new JMenuBar();

        new PartyMenu(gui, content).attach(parentMenu);
        new FightMenu(gui, content).attach(parentMenu);

    }

    public JMenuBar component() {
        return this.parentMenu;
    }

}
