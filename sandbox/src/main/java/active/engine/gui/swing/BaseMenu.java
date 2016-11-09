package active.engine.gui.swing;

import active.engine.command.CommandHandler;
import active.engine.gui.swing.action.SwingBaseActions;
import active.engine.gui.swing.action.SwingFightActions;
import active.model.event.Datum;

import static active.engine.gui.swing.support.KeySupport.*;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.awt.Component;
import java.awt.Container;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class BaseMenu {

    private JMenuBar parentMenu;
    private JMenu menu;


    public BaseMenu(GUIController gui, Datum<Object> content){

        JMenuItem newFight = new JMenuItem("New Fight", 'N');
        newFight.setAction(SwingBaseActions.startFight(gui));
        newFight.setMnemonic('N');
        newFight.setAccelerator(ctrl('N'));
        newFight.setText("New Fight");

        content.onChanged(() -> {
                    newFight.setEnabled(content.get() == null);
//                    System.out.println("Changing new fight menu item: " + newFight.isEnabled());
                }
        );

        this.menu = new JMenu("Base");
        this.menu.setMnemonic('B');
        this.menu.add(newFight);

        this.parentMenu = new JMenuBar();
        this.parentMenu.add(this.menu);
    }

    public JMenuBar component(){
        return this.parentMenu;
    }

}
