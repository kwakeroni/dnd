package active.engine.util.gui.swing;

import java.awt.Dimension;
import java.awt.Insets;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public final class DimensionCalculator {

    private DimensionCalculator(){

    }

    public static Dimension add(Dimension d1, Dimension d2){
        return new Dimension(d1.width + d2.width, d2.height + d2.height);
    }

    public static Dimension subtract(Dimension d1, Dimension d2){
        return new Dimension(d1.width - d2.width, d2.height - d2.height);
    }

    public static Dimension add(Dimension d, Insets insets){
        return new Dimension(d.width + insets.left + insets.right, d.height + insets.top + insets.bottom);
    }

    public static Dimension subtract(Dimension d, Insets insets){
        return new Dimension(d.width - insets.left - insets.right, d.height - insets.top - insets.bottom);
    }

}
