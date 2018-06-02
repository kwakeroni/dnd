package be.kwakeroni.dnd.ui.swing.gui.support;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.function.Consumer;

public class WindowAdapter implements WindowListener {

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    public static WindowListener closing(Consumer<WindowEvent> listener){
        return new WindowAdapter(){

            @Override
            public void windowClosing(WindowEvent e) {
                listener.accept(e);
            }
            
        };
    }
}
