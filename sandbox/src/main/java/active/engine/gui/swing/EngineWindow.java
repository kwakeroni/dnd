package active.engine.gui.swing;

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import javax.swing.*;
import javax.swing.text.JTextComponent;

import active.engine.command.CommandHandler;
import active.engine.event.EventBroker;
import active.engine.gui.InteractionHandler;
import active.engine.gui.swing.action.SwingFightActions;
import active.engine.gui.swing.fight.*;
import active.engine.gui.swing.support.ContainerAdapter;
import active.engine.internal.fight.BattleField;
import active.engine.util.gui.swing.WindowAdapter;
import active.model.event.*;
import active.model.event.Event;
import active.model.fight.Participant;
import active.model.fight.command.Attack;
import active.model.fight.event.FightData;

public class EngineWindow implements GUIController {

    private final JFrame frame;
    private final CommandHandler commandHandler;
    private final BattleField battleField;
    private final EventBroker<?> broker;
    private PluggableContent content;

    public EngineWindow(BattleField battleField, CommandHandler appCommandHandler, EventBroker<?> broker){

        this.battleField = battleField;
        this.commandHandler = appCommandHandler;
        this.broker = broker;

        this.frame = new JFrame();
        this.frame.setMinimumSize(new Dimension(640,480));
        this.frame.setTitle("Dungeons and Dragons 3.5 Fight Assistant");
        this.frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.frame.addWindowListener(WindowAdapter.closing(event -> showCloseDialog()));

        this.frame.setJMenuBar(new BaseMenu(this, content()).component());

    }
    
    public void show(){
        this.frame.pack();
        this.frame.setVisible(true);
    }
    
    private void close(){
        this.frame.setVisible(false);
        this.frame.dispose();
    }
    
    private void showCloseDialog(){
        int choice = JOptionPane.showOptionDialog(this.frame, "Do you really want to quit ?", "Quit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        if (choice == JOptionPane.YES_OPTION){
            this.close();
        }
    }

    @Override
    public JFrame getAncestorWindow() {
        return this.frame;
    }

    @Override
    public CommandHandler getCommandHandler() {
        return this.commandHandler;
    }

    @Override
    public BattleField getBattleField() {
        return this.battleField;
    }

    @Override
    public void registerMenu(PluggableMenu menu) {
        menu.attach(this.frame.getJMenuBar());
    }

    @Override
    public void unregisterMenu(PluggableMenu menu) {
        menu.detach();
        this.frame.getJMenuBar().invalidate();
        this.frame.getJMenuBar().repaint();
    }

    @Override
    public void setContent(PluggableContent content) {
        if (this.content != null){
            throw new IllegalStateException("Other content already active");
        }
        this.content = content;
        this.frame.setContentPane(content.getComponent());
        content.activate(this);
        this.broker.fire(CONTENT_CHANGE);
        this.frame.pack();
    }

    @Override
    public void clearContent(PluggableContent content) {
        if (this.content != null && (content == null || this.content.equals(content))){
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            this.frame.setContentPane(panel);
            this.content.deactivate(this);
            this.content = null;
            this.broker.fire(CONTENT_CHANGE);
            this.frame.pack();
        }
    }

    private Datum<Object> content(){
        return new Datum<Object>() {
            @Override
            public void onChanged(Consumer<Event> consumer) {
                broker.on().event().filter(CONTENT_CHANGE::equals).forEach(consumer);
            }

            @Override
            public Object get() {
                return content;
            }
        };
    }

    private static final Event CONTENT_CHANGE = new Event() {
    };
}
