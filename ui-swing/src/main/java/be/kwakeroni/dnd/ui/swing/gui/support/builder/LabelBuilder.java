package be.kwakeroni.dnd.ui.swing.gui.support.builder;


import javax.swing.*;

import java.awt.*;
import java.util.Optional;

/**
 * @author Maarten Van Puymbroeck
 */
public class LabelBuilder implements
        ComponentListenerBuilder<JLabel, LabelBuilder>,
        ComponentBuilder<JLabel, LabelBuilder> {

    private Optional<JLabel> label;

    public static LabelBuilder of(String text){
        return new LabelBuilder(new JLabel(text));
    }

    private LabelBuilder(JLabel label){
        this.label = Optional.of(label);
    }

    @Override
    public final JLabel component() {
        return label.get();
    }

    public JLabel build(){
        JLabel result = label.get();
        this.label = Optional.empty();
        return result;
    }

    @Override
    public final LabelBuilder self() {
        return this;
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
    
    public LabelBuilder background(Color color){
        this.label.get().setBackground(color);
        return this;
    }

}
