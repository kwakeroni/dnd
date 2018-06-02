package be.kwakeroni.dnd.ui.swing.gui.fight;

import be.kwakeroni.dnd.engine.api.InteractionHandler;
import be.kwakeroni.dnd.model.actor.ActionType;
import be.kwakeroni.dnd.model.fight.AttackActionType;
import be.kwakeroni.dnd.model.fight.FightAction;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static be.kwakeroni.dnd.ui.swing.gui.support.builder.BorderLayoutBuilder.borderLayout;
import static be.kwakeroni.dnd.ui.swing.gui.support.builder.SwingBuilders.button;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class AttackWindow {

    private JDialog window;
    private JPanel attackPanel;
    private AttackInstancePane instancePane;
    private Consumer<? super InteractionHandler.InteractiveAttackData> action = __ -> {
    };

    public AttackWindow(Frame parent, InteractionHandler.InteractiveAttackData attack) {
        this.window = new JDialog(parent, "Attack", true);

        List<ActionType> actions = attack
                .getActor()
                .getActions()
                .stream()
                .filter(ActionType.hasCategory(FightAction.ATTACK))
                .collect(Collectors.toList());

        JList<String> list = new JList<>(actions
                .stream()
                .map(ActionType::getName)
                .toArray(String[]::new));


        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addListSelectionListener(e -> {
            this.instancePane = new AttackInstancePane((AttackActionType) actions.get(list.getSelectedIndex()), attack.getTarget());
            this.attackPanel.removeAll();
            this.attackPanel.add(this.instancePane.component());
            this.attackPanel.repaint();
            this.attackPanel.revalidate();

            if (this.attackPanel.getSize().height < this.attackPanel.getMinimumSize().height
                    || this.attackPanel.getSize().width < this.attackPanel.getMinimumSize().width) {
//                System.out.println(this.attackPanel.getSize() + "<" + this.attackPanel.getMinimumSize());
                this.window.pack();
            }

        });


        this.attackPanel = new JPanel();
        this.attackPanel.add(new JLabel());

        JButton execute = button("Execute")
                .onAction(() -> {
                    Consumer<? super InteractionHandler.InteractiveAttackData> finisher = this.action;
                    this.action = null;
                    if (this.instancePane != null){
                        this.instancePane.addToAttackData(attack);
                    }
                    this.window.setVisible(false);
                    this.window.dispose();
                    finisher.accept(attack);
                })
                .build();


        JPanel buttonPanel = new JPanel();
        buttonPanel.add(execute);

        borderLayout(window.getContentPane())
                .west(list)
                .east(borderLayout(new JPanel())
                        .north(this.attackPanel)
                        .south(buttonPanel)
                );

    }

    public void withAttackData(Consumer<? super InteractionHandler.InteractiveAttackData> action) {
        this.action = action;
        window.pack();
        window.setVisible(true);
    }

}
