package active.engine.gui.swing;

import java.awt.*;
import java.util.function.Consumer;

import javax.swing.*;

import active.engine.command.CommandHandler;
import active.engine.event.EventBroker;
import active.engine.gui.swing.menu.PluggableMenu;
import active.engine.gui.swing.support.JStatusBar;
import active.engine.internal.fight.BattleField;
import active.engine.util.gui.swing.WindowAdapter;
import active.model.event.*;
import active.model.event.Event;

public class EngineWindow implements GUIController {

    private final JFrame frame;
    private final CommandHandler commandHandler;
    private final BattleField battleField;
    private final EventBroker<?> broker;
    private PluggableContent content;
    private Container currentPaneContent;
    private JStatusBar statusBar;

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
        this.statusBar = new JStatusBar();

        this.frame.getContentPane().setLayout(new BorderLayout());
        this.frame.getContentPane().add(this.statusBar, BorderLayout.SOUTH);
        clearContent(null);
    }

    private void setContentPane(Container content){
        this.frame.getContentPane().remove(currentPaneContent);
        this.frame.getContentPane().add(content, BorderLayout.CENTER);
        this.currentPaneContent = content;
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
        setContentPane(content.getComponent());
        content.activate(this);
        this.broker.fire(CONTENT_CHANGE);
        this.frame.pack();
    }

    @Override
    public void clearContent(PluggableContent content) {
        if (this.content != null && (content == null || this.content.equals(content))){
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            setContentPane(panel);
            this.content.deactivate(this);
            this.content = null;
            this.broker.fire(CONTENT_CHANGE);
            this.frame.pack();
        }
    }


    public Datum<PluggableContent> content(){
        return new Datum<PluggableContent>() {
            @Override
            public void onChanged(Consumer<Event> consumer) {
                broker.on().event().filter(CONTENT_CHANGE::equals).forEach(consumer);
            }

            @Override
            public PluggableContent get() {
                return content;
            }
        };
    }

    private static final Event CONTENT_CHANGE = new Event() {
    };

    @Override
    public void setStatusBarText(String text) {
        this.statusBar.setText(text);
    }
}
