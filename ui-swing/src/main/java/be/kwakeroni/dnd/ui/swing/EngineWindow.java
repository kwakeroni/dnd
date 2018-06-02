package be.kwakeroni.dnd.ui.swing;

import be.kwakeroni.dnd.engine.api.command.CommandHandler;
import be.kwakeroni.dnd.engine.api.command.fight.FightCommandFactory;
import be.kwakeroni.dnd.engine.api.command.io.IOCommandFactory;
import be.kwakeroni.dnd.event.Datum;
import be.kwakeroni.dnd.event.Event;
import be.kwakeroni.dnd.event.EventBroker;
import be.kwakeroni.dnd.ui.base.GUIController;
import be.kwakeroni.dnd.ui.base.PluggableContent;
import be.kwakeroni.dnd.ui.base.PluggableMenu;
import be.kwakeroni.dnd.ui.base.command.UICommandFactory;
import be.kwakeroni.dnd.ui.swing.command.SwingIOCommandFactory;
import be.kwakeroni.dnd.ui.swing.command.SwingUICommandFactory;
import be.kwakeroni.dnd.ui.swing.gui.BaseMenu;
import be.kwakeroni.dnd.ui.swing.gui.support.WindowAdapter;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class EngineWindow implements GUIController<JFrame, Container> {

    private final JFrame frame;
    private final CommandHandler commandHandler;
    private final EventBroker<?> broker;
    private final FightCommandFactory fightCommandFactory;
    private final IOCommandFactory ioCommandFactory;
    private final UICommandFactory uiCommandFactory;
    private PluggableContent<? super JFrame, Container> content;

    public EngineWindow(CommandHandler appCommandHandler, EventBroker<?> broker, FightCommandFactory fightCommandFactory) {

        this.commandHandler = appCommandHandler;
        this.broker = broker;
        this.fightCommandFactory = fightCommandFactory;
        this.ioCommandFactory = new SwingIOCommandFactory();

        this.frame = new JFrame();
        this.frame.setMinimumSize(new Dimension(640, 480));
        this.frame.setTitle("Dungeons and Dragons 3.5 Fight Assistant");
        this.frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        this.uiCommandFactory = new SwingUICommandFactory(this.fightCommandFactory, this.ioCommandFactory);
        publish();
    }

    private void publish(){
        this.commandHandler.registerContext(GUIController.class, this);
        this.frame.setJMenuBar(new BaseMenu(this, content()).component());
        this.frame.addWindowListener(WindowAdapter.closing(event -> exitWithOptionalDialog()));
    }

    @Override
    public FightCommandFactory getFightCommandFactory() {
        return this.fightCommandFactory;
    }

    @Override
    public IOCommandFactory getIOCommandFactory() {
        return this.ioCommandFactory;
    }

    @Override
    public UICommandFactory getUICommandFactory() {
        return this.uiCommandFactory;
    }

    public void show() {
        this.frame.pack();
        this.frame.setVisible(true);
    }

    private void close() {
        this.frame.setVisible(false);
        this.frame.dispose();
    }

    private void exitWithOptionalDialog() {
        if (this.content == null || confirmExit()) {
            this.close();
        }
    }

    private boolean confirmExit() {
        int choice = JOptionPane.showOptionDialog(this.frame, "Do you really want to quit ?", "Quit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        return choice == JOptionPane.YES_OPTION;
    }

    @Override
    public CommandHandler getCommandHandler() {
        return (this.content == null)? this.commandHandler : this.content.getCommandHandler();
    }

    @Override
    public <C> void registerContext(Class<C> contextType, C context) {
        getCommandHandler().registerContext(contextType, context);
    }

    @Override
    public void unregisterContext(Object context) {
        getCommandHandler().unregisterContext(context);
    }

    @Override
    public <C> void registerGlobalContext(Class<C> contextType, C context) {
        this.commandHandler.registerContext(contextType, context);
    }

    @Override
    public void unregisterGlobalContext(Object context) {
        this.commandHandler.unregisterContext(context);
    }

    @Override
    public JFrame getAncestorWindow() {
        return this.frame;
    }

    public void registerMenu(PluggableMenu<JMenuBar> menu) {
        menu.attach(this.frame.getJMenuBar());
    }

    public void unregisterMenu(PluggableMenu<JMenuBar> menu) {
        menu.detach();
        this.frame.getJMenuBar().invalidate();
        this.frame.getJMenuBar().repaint();
    }

    @Override
    public void setContent(PluggableContent<? super JFrame, Container> content) {
        if (this.content != null) {
            throw new IllegalStateException("Other content already active");
        }
        this.content = content;
        this.frame.setContentPane(content.getComponent());
        content.activate(this, this.commandHandler.overlay());
        this.broker.fire(CONTENT_CHANGE);
        this.frame.pack();
    }

    @Override
    public void clearContent(PluggableContent<? super JFrame, Container> content) {
        if (this.content != null && (content == null || this.content.equals(content))) {
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            this.frame.setContentPane(panel);
            this.content.deactivate(this);
            this.content = null;
            this.broker.fire(CONTENT_CHANGE);
            this.frame.pack();
        }
    }

    private Datum<Object> content() {
        return new Datum<>() {
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
