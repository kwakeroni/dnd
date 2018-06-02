package be.kwakeroni.dnd.ui.swing.gui.support.builder;

import java.awt.Color;
import java.awt.Component;
import java.util.function.Supplier;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public interface ComponentBuilder<C extends Component, B extends ComponentBuilder<C, B>> extends Supplier<C> {

    public C component();

    public B self();

    public default B bg(int r, int g, int b){
        return bg(new Color(r, g, b));
    }

    public default B bg(int r, int g, int b, int a){
        return bg(new Color(r, g, b, a));
    }

    public default B bg(Color color){
        component().setBackground(color);
        return self();
    }

    public default C get(){
        return component();
    }
}
