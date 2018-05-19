package active.engine.gui.swing.fight;

import active.engine.gui.swing.GUIController;
import active.engine.gui.swing.PluggableMenu;
import active.engine.gui.swing.action.SwingBaseActions;
import active.engine.gui.swing.action.SwingFightActions;
import active.model.event.Datum;
import active.model.event.Reaction;
import active.model.fight.FightController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import static active.engine.gui.swing.support.KeySupport.ctrl;
import static active.engine.gui.swing.support.KeySupport.shift;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class FightMenu implements PluggableMenu {

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
        this.fightMenu = new JMenu("Fight");
        fightMenu.setMnemonic('F');

        JMenuItem newFight = new JMenuItem("New Fight", 'N');
        fightMenu.add(newFight);
        newFight.setAction(SwingBaseActions.startFight(gui));
        newFight.setMnemonic('N');
        newFight.setAccelerator(ctrl('N'));
        newFight.setText("New Fight");

        JMenuItem endFight = new JMenuItem("End Fight", 'E');
        fightMenu.add(endFight);
        endFight.setAction(SwingBaseActions.endFight(gui));
        endFight.setMnemonic('E');
        endFight.setText("End Fight");


        JMenuItem saveFight = new JMenuItem("Save Fight", 'S');
        fightMenu.add(saveFight);
        saveFight.setAction(SwingFightActions.exportFight(this::getParentWindow, gui::getCommandHandler));
        saveFight.setMnemonic('S');
        saveFight.setAccelerator(ctrl('S'));
        saveFight.setText("Save Fight");

        JMenuItem saveFightAs = new JMenuItem("Save Fight As...", 'S');
        fightMenu.add(saveFightAs);
        saveFightAs.setAction(SwingFightActions.exportFightAs(this::getParentWindow, gui::getCommandHandler));
        saveFightAs.setMnemonic('A');
        saveFightAs.setAccelerator(ctrl(shift('S')));
        saveFightAs.setText("Save Fight As...");

        JMenuItem logFight = new JMenuItem("Log Fight");
        fightMenu.add(logFight);
        logFight.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.getCommandHandler().execute(context -> {
                    System.out.println(context.getContext(FightController.class).getState());
                });

            }
        });


        Reaction refreshState = () -> {
            boolean isFight = content.get() != null;
            newFight.setEnabled(content.get() == null);
            endFight.setEnabled(isFight);
            saveFight.setEnabled(isFight);
            saveFightAs.setEnabled(isFight);
        };

        refreshState.react();
        content.onChanged(refreshState);


    }

    public JMenu component() {
        return this.fightMenu;
    }

}
