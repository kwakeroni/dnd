package be.kwakeroni.dnd.ui.swing.gui.support;

import javax.swing.KeyStroke;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.function.IntUnaryOperator;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class KeySupport {

    public static KeyStroke ctrl(char key){
        return KeyStroke.getKeyStroke(key, InputEvent.CTRL_DOWN_MASK);
    }

    public static KeyStroke ctrl(KeyStroke key){
        return modify(key, mod -> mod | InputEvent.CTRL_DOWN_MASK);
    }

    public static KeyStroke shift(char key){
        return KeyStroke.getKeyStroke(key, InputEvent.SHIFT_DOWN_MASK);
    }

    public static KeyStroke shift(KeyStroke key){
        return modify(key, mod -> mod | InputEvent.SHIFT_DOWN_MASK);
    }

    private static KeyStroke modify(KeyStroke key, IntUnaryOperator mod){
        if (key.getKeyCode() == 0){
            return KeyStroke.getKeyStroke(Character.valueOf(key.getKeyChar()), mod.applyAsInt(key.getModifiers()));
        } else {
            return KeyStroke.getKeyStroke(key.getKeyCode(), mod.applyAsInt(key.getModifiers()), key.isOnKeyRelease());
        }
    }


    private static final KeyListener KEY_LISTENER = new KeyListener() {
        private boolean debug = false;

        @Override
        public void keyTyped(KeyEvent e) {
            dump("T>", e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            dump("D>", e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            dump("U>", e);
        }

        private void dump(String prefix, KeyEvent e) {
            if (debug) {
                System.out.println(prefix + " " + toString(e));
            }
        }

        private String toString(KeyEvent e) {
            return (e.isControlDown() ? "ctrl " : "") + (e.isShiftDown() ? "shift " : "") + ((char) e.getKeyCode()) + " (" + e.getKeyCode() + ")" + " | " + e.getKeyChar() + " (" + ((int) e.getKeyChar()) + ")";
        }
    };

}
