package active.engine.gui.swing.support.listener;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.function.Consumer;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public abstract class FocusListenerSupport implements FocusListener {

    @Override
    public void focusGained(FocusEvent e) { }

    public static FocusListener onFocusGained(Reaction reaction){
        return onFocusGained(e -> reaction.react());
    }
    public static FocusListener onFocusGained(Consumer<FocusEvent> consumer){
        return new FocusListenerSupport() {
            @Override
            public void focusGained(FocusEvent e) {
                consumer.accept(e);
            }
        };
    }

    @Override
    public void focusLost(FocusEvent e) { }

    public static FocusListener onFocusLost(Reaction reaction){
        return onFocusLost(e -> reaction.react());
    }
    public static FocusListener onFocusLost(Consumer<FocusEvent> consumer){
        return new FocusListenerSupport() {
            @Override
            public void focusLost(FocusEvent e) {
                consumer.accept(e);
            }
        };
    }
}
