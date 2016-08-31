package active.engine.gui.swing;

import active.engine.internal.action.category.FightAction;
import active.engine.internal.action.type.AttackActionType;
import active.model.action.ActionType;
import active.model.fight.command.Attack;

import javax.swing.*;
import java.awt.Dimension;
import java.util.List;
import java.util.stream.Collectors;

import static active.engine.gui.swing.support.builder.BorderLayoutBuilder.borderLayout;
import static active.engine.gui.swing.support.builder.SwingBuilders.button;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class AttackWindow {

    private JDialog window;
    private JPanel attackPanel;
    private AttackInstancePane instancePane;

    public AttackWindow(JFrame parent, Attack.AttackData attack) {
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
                    || this.attackPanel.getSize().width < this.attackPanel.getMinimumSize().width){
                System.out.println(this.attackPanel.getSize() + "<" + this.attackPanel.getMinimumSize());
                this.window.pack();
            }

        });


        this.attackPanel = new JPanel();
        this.attackPanel.add(new JLabel());

        JButton execute = button("Execute")
                .onAction(() -> {
                    this.window.setVisible(false);
                    this.window.dispose();
                    this.instancePane.addToAttackData(attack);
                    attack.execute();
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

//        list.setSelectedIndex(0);

        window.pack();
        window.setVisible(true);
    }

}
