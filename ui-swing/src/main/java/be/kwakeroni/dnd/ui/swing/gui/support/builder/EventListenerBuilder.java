package be.kwakeroni.dnd.ui.swing.gui.support.builder;

import javax.swing.AbstractButton;
import java.awt.Component;

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
