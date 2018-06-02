package be.kwakeroni.dnd.ui.swing.gui.support.builder;

import be.kwakeroni.dnd.util.function.Reaction;

import javax.swing.AbstractButton;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public interface ButtonListenerBuilder<C extends AbstractButton, B extends ButtonListenerBuilder<C, B>>
                    extends ComponentListenerBuilder<C, B>{

    default B onAction(Reaction reaction){
        return onAction(reaction.asConsumer());
    }

    default B onAction(Consumer<? super ActionEvent> action){
        component().addActionListener(action::accept);
        return self();
    }
}
