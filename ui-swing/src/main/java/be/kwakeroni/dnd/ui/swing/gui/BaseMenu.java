package be.kwakeroni.dnd.ui.swing.gui;

import be.kwakeroni.dnd.ui.base.GUIController;
import be.kwakeroni.dnd.ui.swing.gui.fight.FightMenu;
import be.kwakeroni.dnd.ui.swing.gui.party.PartyMenu;
import be.kwakeroni.dnd.event.Datum;

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
