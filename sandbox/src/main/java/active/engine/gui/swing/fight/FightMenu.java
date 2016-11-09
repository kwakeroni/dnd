package active.engine.gui.swing.fight;

import active.engine.command.CommandHandler;
import active.engine.gui.swing.GUIController;
import active.engine.gui.swing.PluggableMenu;
import active.engine.gui.swing.action.SwingBaseActions;
import active.engine.gui.swing.action.SwingFightActions;
import active.model.fight.Fight;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.awt.Container;
import java.util.function.Supplier;

import static active.engine.gui.swing.support.KeySupport.ctrl;
import static active.engine.gui.swing.support.KeySupport.shift;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class FightMenu implements PluggableMenu {

    private JMenuBar parent;
    private final JMenu fightMenu;

    public void attach(JMenuBar parent){
        this.parent = parent;
        this.parent.add(fightMenu);

    }

    public void detach(){
        this.parent.remove(fightMenu);
        this.parent = null;
    }

    private Container getParentWindow(){
        return this.parent.getParent();
    }

    public FightMenu(GUIController gui, Supplier<Fight> fight){
        this.fightMenu = new JMenu("Fight");

        JMenuItem endFight = new JMenuItem("End Fight", 'E');
        fightMenu.add(endFight);
        endFight.setAction(SwingBaseActions.endFight(gui));
        endFight.setMnemonic('E');
        endFight.setText("End Fight");

        JMenuItem importParty = new JMenuItem("Import Party...", 'I');
        fightMenu.add(importParty);
        importParty.setAction(SwingFightActions.importPartyFile(this::getParentWindow, gui::getCommandHandler));
        importParty.setMnemonic('I');
        importParty.setAccelerator(ctrl('I'));
        importParty.setText("Import Party...");

        JMenuItem saveFight = new JMenuItem("Save Fight", 'S');
        fightMenu.add(saveFight);
        saveFight.setAction(SwingFightActions.exportFight(this::getParentWindow, fight));
        saveFight.setMnemonic('S');
        saveFight.setAccelerator(ctrl('S'));
        saveFight.setText("Save Fight");

        JMenuItem saveFightAs = new JMenuItem("Save Fight As...", 'S');
        fightMenu.add(saveFightAs);
        saveFightAs.setAction(SwingFightActions.exportFightAs(this::getParentWindow, fight));
        saveFightAs.setMnemonic('A');
        saveFightAs.setAccelerator(ctrl(shift('S')));
        saveFightAs.setText("Save Fight As...");

        fightMenu.setMnemonic('F');

    }

    public JMenu component(){
        return this.fightMenu;
    }

}
