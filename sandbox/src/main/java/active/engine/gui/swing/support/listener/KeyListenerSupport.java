package active.engine.gui.swing.support.listener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.function.Consumer;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public abstract class KeyListenerSupport implements KeyListener {

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public static KeyListener onKeyTyped(Reaction reaction) {
        return onKeyTyped(e -> reaction.react());
    }

    public static KeyListener onKeyTyped(Consumer<KeyEvent> consumer) {
        return new KeyListenerSupport() {
            @Override
            public void keyTyped(KeyEvent e) {
                consumer.accept(e);
            }
        };
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    public static KeyListener onKeyPressed(Reaction reaction) {
        return onKeyPressed(e -> reaction.react());
    }

    public static KeyListener onKeyPressed(Consumer<KeyEvent> consumer) {
        return new KeyListenerSupport() {
            @Override
            public void keyPressed(KeyEvent e) {
                consumer.accept(e);
            }
        };
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public static KeyListener onKeyReleased(Reaction reaction) {
        return onKeyReleased(e -> reaction.react());
    }

    public static KeyListener onKeyReleased(Consumer<KeyEvent> consumer) {
        return new KeyListenerSupport() {
            @Override
            public void keyReleased(KeyEvent e) {
                consumer.accept(e);
            }
        };
    }

}
