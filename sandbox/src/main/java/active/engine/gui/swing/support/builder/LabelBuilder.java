package active.engine.gui.swing.support.builder;

import javax.swing.JLabel;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class LabelBuilder implements
        ComponentListenerBuilder<JLabel, LabelBuilder>,
        ComponentBuilder<JLabel, LabelBuilder> {

    JLabel label;

    LabelBuilder(String text) {
        this.label = new JLabel(text);
    }

    @Override
    public final JLabel component() {
        return label;
    }

    @Override
    public final LabelBuilder self() {
        return this;
    }

    public JLabel build(){
        return label;
    }
}
