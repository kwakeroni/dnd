package active.engine.gui.swing;

import active.engine.util.gui.swing.LabelBuilder;
import active.model.cat.Named;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;

/**
 * @author Maarten Van Puymbroeck
 */
public abstract class CharacterLine<C extends Named> {

    private static Font FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 24);

    protected CharacterLine(){
    }

    protected LabelBuilder newLabel(String text){
        return LabelBuilder.of(text)
                           .middle()
                           .font(FONT);
    }

    public abstract Collection<? extends JComponent> components();
}
