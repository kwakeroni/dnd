package active.engine.gui.swing.support.listener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.function.Consumer;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public interface MouseListenerSupport extends MouseListener {

    @Override
    default void mouseClicked(MouseEvent e) {
    }

    public static MouseListenerSupport onMouseClicked(Reaction reaction) {
        return onMouseClicked(e -> reaction.react());
    }

    public static MouseListenerSupport onMouseClicked(Consumer<? super MouseEvent> consumer) {
        return new MouseListenerSupport() {
            @Override
            public void mouseClicked(MouseEvent e) {
                consumer.accept(e);
            }
        };
    }

    public static MouseListenerSupport onMouseDoubleClicked(Reaction reaction) {
        return onMouseDoubleClicked(e -> reaction.react());
    }

    public static MouseListenerSupport onMouseDoubleClicked(Consumer<? super MouseEvent> consumer) {
        return onMouseClicked(2, consumer);
    }

    public static MouseListenerSupport onMouseClicked(int times, Reaction reaction) {
        return onMouseClicked(times, e -> reaction.react());
    }

    public static MouseListenerSupport onMouseClicked(int times, Consumer<? super MouseEvent> consumer) {
        return new MouseListenerSupport() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == times) {
                    consumer.accept(e);
                }
            }
        };
    }


    @Override
    default void mousePressed(MouseEvent e) {
    }

    @Override
    default void mouseReleased(MouseEvent e) {
    }

    @Override
    default void mouseEntered(MouseEvent e) {
    }

    public static MouseListenerSupport onMouseEntered(Reaction reaction) {
        return onMouseEntered(e -> reaction.react());
    }

    public static MouseListenerSupport onMouseEntered(Consumer<MouseEvent> consumer) {
        return new MouseListenerSupport() {
            @Override
            public void mouseEntered(MouseEvent e) {
                consumer.accept(e);
            }
        };
    }


    @Override
    default void mouseExited(MouseEvent e) {
    }

    public static MouseListenerSupport onMouseExited(Reaction reaction) {
        return onMouseExited(e -> reaction.react());
    }

    public static MouseListenerSupport onMouseExited(Consumer<MouseEvent> consumer) {
        return new MouseListenerSupport() {
            @Override
            public void mouseExited(MouseEvent e) {
                consumer.accept(e);
            }
        };
    }

        public static MouseListenerSupport onMouseHover(Reaction onEnter, Reaction onExit){
                return onMouseHover(e -> onEnter.react(), e-> onExit.react());
            }
            public static MouseListenerSupport onMouseHover(Consumer<MouseEvent> onEnter, Consumer<MouseEvent> onExit){
                return onMouseEntered(onEnter)
                        .and(onMouseExited(onExit));
            }


    public default MouseListenerSupport and(MouseListener other) {

        class Composed implements MouseListenerSupport {
            private final MouseListener two;

            Composed(MouseListener two) {
                this.two = two;
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                MouseListenerSupport.this.mouseClicked(e);
                two.mouseClicked(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                MouseListenerSupport.this.mouseEntered(e);
                two.mouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                MouseListenerSupport.this.mouseExited(e);
                two.mouseExited(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                MouseListenerSupport.this.mousePressed(e);
                two.mousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                MouseListenerSupport.this.mouseReleased(e);
                two.mouseReleased(e);
            }
        }

        return new Composed(other);
    }
}
