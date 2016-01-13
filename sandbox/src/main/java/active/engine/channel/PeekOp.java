package active.engine.channel;

import java.util.function.Consumer;

/**
 * @author Maarten Van Puymbroeck
 */
class PeekOp<E> extends Pipeline<E, E> {

    private Consumer<? super E> action;

    public PeekOp(Consumer<? super E> action) {
        this.action = action;
    }

    @Override
    public void accept(E e) {
        this.action.accept(e);
        forward(e);
    }
}
