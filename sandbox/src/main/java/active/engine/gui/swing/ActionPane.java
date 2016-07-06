package active.engine.gui.swing;

import java.util.function.Predicate;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import active.engine.command.Command;
import active.engine.command.CommandHandler;
import active.model.fight.command.*;
import active.model.fight.event.FightData;

public class ActionPane {

    private CommandHandler handler;
    private JPanel panel;
    
    public ActionPane(CommandHandler handler, FightData fight){
        this.panel = new JPanel();
        this.panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        this.handler = handler;

        add("Start Turn", new StartTurn(), this::noActiveTurn, fight);
        add("Next Turn", new NextTurn(), this::isActiveTurn, fight);
        add("End Turn", new EndTurn(), this::isActiveTurn, fight);
        add("Attack", SelectParticipant.hittable(Attack::new), this::isActiveTurn, fight);
    }

    private boolean  isActiveTurn(FightData f){
        return f.turn().get().isPresent();
    }
    private boolean noActiveTurn(FightData f){
        return ! isActiveTurn(f);
    }

    private void add(String name, Command command, Predicate<FightData> enabled, FightData fight){


        JButton button = new JButton(name);
        button.addActionListener(a -> handler.execute(command));
        
        fight.turn().onChanged(() -> button.setEnabled(enabled.test(fight)));
        
        button.setEnabled(enabled.test(fight));
//        fight.turn().onChanged(() -> button.setEnabled(enabled.test(fight)  && (! modal.isDisabled(button))));
        
        panel.add(button);
    }

    JComponent component(){
        return this.panel;
    }


}
