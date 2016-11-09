package active.engine.gui.swing.fight;

import java.util.function.Predicate;
import java.util.function.Supplier;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import active.engine.command.Command;
import active.engine.command.CommandHandler;
import active.engine.gui.business.FightActions;
import active.model.fight.command.*;
import active.model.fight.event.FightData;

public class JFightActionPane implements FightActions<JButton> {

    private Supplier<CommandHandler> handler;
    private JPanel panel;
    
    public JFightActionPane(Supplier<CommandHandler> handler, FightData fight){
        this.panel = new JPanel();
        this.panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        this.handler = handler;

        initBehaviour(fight);
    }

    @Override
    public JButton addActionGUI(String name, Character mnemonic, Command command) {
        JButton button = new JButton(name);
        button.addActionListener(a -> {
            handler.get().execute(command);});
        if (mnemonic != null) {
            button.setMnemonic(mnemonic);
        }
        panel.add(button);
        return button;
    }

    @Override
    public void setEnabled(JButton jButton, boolean enabled) {
        jButton.setEnabled(enabled);
    }

    public JComponent component(){
        return this.panel;
    }


}
