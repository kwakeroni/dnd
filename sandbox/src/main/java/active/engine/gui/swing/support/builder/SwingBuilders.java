package active.engine.gui.swing.support.builder;

import javax.swing.JButton;
import java.awt.Button;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class SwingBuilders {

    private SwingBuilders(){

    }

    public static LabelBuilder label(Object object){
        return new LabelBuilder(String.valueOf(object));
    }

    public static LabelBuilder label(String text){
        return new LabelBuilder(text);
    }

    public static TextFieldBuilder textField(){
        return new TextFieldBuilder();
    }

    public static ButtonBuilder<JButton> button(String text) {
        return ButtonBuilder.jbutton(text);
    }
}
