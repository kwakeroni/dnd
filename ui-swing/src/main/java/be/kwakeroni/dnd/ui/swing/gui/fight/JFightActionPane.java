package be.kwakeroni.dnd.ui.swing.gui.fight;

import java.util.function.Supplier;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import be.kwakeroni.dnd.engine.api.command.Command;
import be.kwakeroni.dnd.engine.api.command.CommandHandler;
import be.kwakeroni.dnd.engine.api.command.fight.FightCommandFactory;
import be.kwakeroni.dnd.ui.base.action.FightActions;
import be.kwakeroni.dnd.model.fight.event.FightData;
import be.kwakeroni.dnd.ui.base.command.UICommandFactory;

public class JFightActionPane implements FightActions<JButton> {

    private final Supplier<CommandHandler> handler;
    private final Supplier<FightCommandFactory> fightCommandFactory;
    private final Supplier<UICommandFactory> uiCommandFactory;
    private final JPanel panel;
    
    public JFightActionPane(Supplier<FightCommandFactory> fightCommandFactory, Supplier<UICommandFactory> uiCommandFactory, Supplier<CommandHandler> handler, FightData fight){
        this.fightCommandFactory = fightCommandFactory;
        this.uiCommandFactory = uiCommandFactory;
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

    @Override
    public FightCommandFactory getFightCommandFactory() {
        return fightCommandFactory.get();
    }

    @Override
    public UICommandFactory getUICommandFactory() {
        return uiCommandFactory.get();
    }
}
