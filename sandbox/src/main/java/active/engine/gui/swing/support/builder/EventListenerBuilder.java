package active.engine.gui.swing.support.builder;

import active.model.event.Reaction;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class EventListenerBuilder {

    public static <C extends Component> ComponentListenerBuilder<C, ?> of(C component){
        return new CombinedBuilder(component);
    }

    public static <C extends AbstractButton> ButtonListenerBuilder<C, ?> of(C button){
        return new CombinedBuilder(button);
    }

    private EventListenerBuilder() {

    }

    private static class CombinedBuilder implements
            ComponentListenerBuilder,
            ButtonListenerBuilder{

        protected final Component component;

        public CombinedBuilder(Component component) {
            this.component = component;
        }

        @Override
        public Component component() {
            return component;
        }

        @Override
        public CombinedBuilder self() {
            return this;
        }
    }

}
