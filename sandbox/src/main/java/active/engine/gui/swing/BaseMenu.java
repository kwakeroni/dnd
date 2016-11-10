package active.engine.gui.swing;

import active.engine.gui.swing.action.SwingFightActions;
import active.engine.gui.swing.menu.PluggableMenuItem;
import active.model.event.Datum;
import active.model.event.Reaction;

import static active.engine.gui.swing.menu.PluggableMenuItemBuilder.menuItem;
import static active.engine.gui.swing.support.KeySupport.*;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class BaseMenu {

    private JMenuBar parentMenu;
    private JMenu menu;
    private GUIController gui;

    public BaseMenu(GUIController gui, Datum<? extends PluggableContent> content){
        this.menu = new JMenu("Base");
        this.menu.setMnemonic('B');

        this.parentMenu = new JMenuBar();
        this.parentMenu.add(this.menu);
        this.gui = gui;

        add(SwingFightActions::newFight, content::onChanged, () -> content.get() == null);
        add(SwingFightActions::loadFight, content::onChanged, () -> content.get() == null);

    }

    private void add(Function<GUIController, Action> action){
        add(menuItem(gui, action));
    }
    private void add(Function<GUIController, Action> action, Consumer<? super Reaction> trigger, Supplier<Boolean> state){
        add(menuItem(gui, action).enabledBy(trigger, state));
    }

    private void add(PluggableMenuItem item){
        item.attach(this.menu);
    }

    public JMenuBar component(){
        return this.parentMenu;
    }

}
