package active.engine.gui.swing.action;

import active.engine.gui.swing.GUIController;
import active.engine.gui.swing.menu.PluggableMenuItem;
import active.model.event.Reaction;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class ActionBuilder extends AbstractAction {

//    private final GUIController gui;
    private Consumer<? super ActionEvent> action = e -> {};

    private ActionBuilder(){

    }

    private ActionBuilder(Consumer<? super ActionEvent> action){
        this.action = action;
    }

    public static ActionBuilder action(String text){
        ActionBuilder builder = new ActionBuilder();
        builder.putValue(Action.NAME, text);
        return builder;
    }

    public ActionBuilder doing(Consumer<? super ActionEvent> action){
        this.action = action;
        return this;
    }

    public ActionBuilder doing(Reaction reaction){
        return doing(reaction.asConsumer());
    }

    public ActionBuilder withMnemonic(char mnemonic){
        putValue(Action.MNEMONIC_KEY, Integer.valueOf(mnemonic));
        return this;
    }

    public ActionBuilder withAccelerator(KeyStroke key){
        putValue(Action.ACCELERATOR_KEY, key);
        return this;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.action.accept(e);
    }
}
