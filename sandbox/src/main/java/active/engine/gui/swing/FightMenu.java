package active.engine.gui.swing;

import active.engine.command.CommandHandler;
import active.engine.gui.swing.action.SwingFightActions;
import active.model.fight.FightController;

import javax.swing.*;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class FightMenu {

    private JMenuBar menu;

    public FightMenu(CommandHandler handler){

        this.menu = new JMenuBar();

        JMenuItem importParty = new JMenuItem("Import Party...", 'I');
        importParty.setAction(SwingFightActions.importPartyFile(this.menu::getParent, () -> handler));
        importParty.setAccelerator(ctrl('I'));
        importParty.setText("Import Party...");

        JMenu fightMenu = new JMenu("Fight");
        fightMenu.add(importParty);
        fightMenu.setMnemonic('F');
        this.menu.add(fightMenu);
    }

    public JMenuBar component(){
        return this.menu;
    }


    private static KeyStroke ctrl(char key){
        return KeyStroke.getKeyStroke(Character.valueOf(key), InputEvent.CTRL_DOWN_MASK);
    }
}
