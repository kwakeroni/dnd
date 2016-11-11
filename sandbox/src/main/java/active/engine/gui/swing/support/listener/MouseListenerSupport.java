package active.engine.gui.swing.support.listener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.function.Consumer;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public abstract class MouseListenerSupport implements MouseListener {

    @Override
    public void mouseClicked(MouseEvent e) { }

    public static MouseListener onMouseClicked(Reaction reaction){
        return onMouseClicked(e -> reaction.react());
    }
    public static MouseListener onMouseClicked(Consumer<? super MouseEvent> consumer){
        return new MouseListenerSupport(){
            @Override
            public void mouseClicked(MouseEvent e) {
                consumer.accept(e);
            }
        };
    }

    public static MouseListener onMouseDoubleClicked(Reaction reaction){
        return onMouseDoubleClicked(e -> reaction.react());
    }
    public static MouseListener onMouseDoubleClicked(Consumer<? super MouseEvent> consumer){
        return onMouseClicked(2, consumer);
    }

    public static MouseListener onMouseClicked(int times, Reaction reaction){
        return onMouseClicked(times, e -> reaction.react());
    }
    public static MouseListener onMouseClicked(int times, Consumer<? super MouseEvent> consumer){
        return new MouseListenerSupport(){
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == times) {
                    consumer.accept(e);
                }
            }
        };
    }


    @Override
    public void mousePressed(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }
}
