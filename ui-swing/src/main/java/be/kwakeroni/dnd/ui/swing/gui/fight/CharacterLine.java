package be.kwakeroni.dnd.ui.swing.gui.fight;

import be.kwakeroni.dnd.type.base.Named;
import be.kwakeroni.dnd.ui.swing.gui.support.ContainerAdapter;
import be.kwakeroni.dnd.ui.swing.gui.support.builder.LabelBuilder;

import java.awt.*;

/**
 * @author Maarten Van Puymbroeck
 */
public abstract class CharacterLine<C extends Named> implements ContainerAdapter {

    private static Font FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 24);

    protected CharacterLine() {
    }

    protected LabelBuilder newLabel(String text) {
        return LabelBuilder.of(text)
                .middle()
                .font(FONT)
                .background(new Color(196, 128, 255));
    }

    public abstract void select();

    public abstract void deselect();

}
