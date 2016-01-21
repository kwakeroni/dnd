package active.engine.util.gui.swing;

import com.google.common.base.Optional;

import javax.swing.*;
import java.awt.*;

/**
 * @author Maarten Van Puymbroeck
 */
public class LabelBuilder {

    private Optional<JLabel> label;

    public static LabelBuilder of(String text){
        return new LabelBuilder(new JLabel(text));
    }

    private LabelBuilder(JLabel label){
        this.label = Optional.of(label);
    }

    public LabelBuilder right(){
        this.label.get().setHorizontalAlignment(SwingConstants.RIGHT);
        this.label.get().setAlignmentX(Component.RIGHT_ALIGNMENT);
        return this;
    }

    public LabelBuilder left(){
        this.label.get().setHorizontalAlignment(SwingConstants.LEFT);
        this.label.get().setAlignmentX(Component.LEFT_ALIGNMENT);
        return this;
    }

    public LabelBuilder center(){
        this.label.get().setHorizontalAlignment(SwingConstants.CENTER);
        this.label.get().setAlignmentX(Component.CENTER_ALIGNMENT);
        return this;
    }

    public LabelBuilder middle(){
        this.label.get().setVerticalAlignment(SwingConstants.CENTER);
        return this;
    }

    public LabelBuilder font(Font font){
        this.label.get().setFont(font);
        return this;
    }


    public JLabel create(){
        JLabel result = label.get();
        this.label = Optional.absent();
        return result;
    }

}
