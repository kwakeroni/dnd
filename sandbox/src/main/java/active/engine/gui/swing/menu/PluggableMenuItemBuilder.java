package active.engine.gui.swing.menu;

import active.engine.gui.swing.GUIController;
import active.engine.gui.swing.action.SwingBaseActions;
import active.model.event.Datum;
import active.model.event.Reaction;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static active.engine.gui.swing.support.KeySupport.ctrl;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class PluggableMenuItemBuilder implements PluggableMenuItem {

    private final JMenuItem item;
    private JMenu menu;

    private PluggableMenuItemBuilder(Action action) {
        this.item = new JMenuItem(action);
    }

    public static PluggableMenuItemBuilder menuItem(Action action){
        return new PluggableMenuItemBuilder(action);
    }

    public static PluggableMenuItemBuilder menuItem(GUIController gui, Function<GUIController, ? extends Action> action){
        return menuItem(action.apply(gui));
    }

    public PluggableMenuItemBuilder enabledBy(Consumer<? super Reaction> trigger, Supplier<Boolean> state){
        trigger.accept((Reaction) () -> {
                    this.item.setEnabled(Boolean.TRUE.equals(state.get()));
                }
        );

        return this;
    }

    @Override
    public void attach(JMenu menu) {
        this.menu = menu;
        this.menu.add(this.item);
        System.out.println(this.item);
    }

    @Override
    public void detach() {
        this.menu.remove(this.item);
        this.menu = null;
    }
}
