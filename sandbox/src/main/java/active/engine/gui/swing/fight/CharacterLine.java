package active.engine.gui.swing.fight;

import active.engine.gui.swing.support.ContainerAdapter;
import active.engine.util.gui.swing.LabelBuilder;
import active.model.cat.Named;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;

/**
 * @author Maarten Van Puymbroeck
 */
public abstract class CharacterLine<C extends Named> implements ContainerAdapter {

    private static Font FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 24);

    protected CharacterLine(){
    }

    protected static LabelBuilder newLabel(String text){
        return LabelBuilder.of(text)
                           .middle()
                           .font(FONT)
                           .background(new Color(196,128,255));
    }

    public abstract void select();
    public abstract void deselect();
    
}
