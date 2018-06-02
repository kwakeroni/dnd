package be.kwakeroni.dnd.ui.swing.gui.support.builder;

import javax.swing.*;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class SwingBuilders {

    private SwingBuilders() {

    }

    public static LabelBuilder label(Object object) {
        return LabelBuilder.of(String.valueOf(object));
    }

    public static LabelBuilder label(String text) {
        return LabelBuilder.of(text);
    }

    public static TextFieldBuilder textField() {
        return new TextFieldBuilder();
    }

    public static ButtonBuilder<JButton> button(String text) {
        return ButtonBuilder.jbutton(text);
    }
}
