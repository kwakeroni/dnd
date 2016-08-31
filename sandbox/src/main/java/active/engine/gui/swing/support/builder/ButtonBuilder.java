package active.engine.gui.swing.support.builder;

import javax.swing.AbstractButton;
import javax.swing.JButton;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class ButtonBuilder<C extends AbstractButton> implements
    ButtonListenerBuilder<C, ButtonBuilder<C>>,
    ComponentBuilder<C, ButtonBuilder<C>> {

    C button;

    private ButtonBuilder(C component){
        this.button = component;
    }

    @Override
    public C component() {
        return this.button;
    }

    @Override
    public ButtonBuilder<C> self() {
        return this;
    }

    public C build(){
        return this.button;
    }

    static ButtonBuilder<JButton> jbutton(String text){
        return new ButtonBuilder<>(new JButton(text));
    }
}
