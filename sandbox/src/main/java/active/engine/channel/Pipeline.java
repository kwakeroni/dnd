package active.engine.channel;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

abstract class Pipeline<E_IN, E_OUT> implements Consumer<E_IN>, Channel<E_OUT> {
    
    private Consumer<? super E_OUT> nextOp = null;
    
    protected void forward(E_OUT toNextOp){
        this.nextOp.accept(toNextOp);
    }
    
    private <R, C extends Consumer<E_OUT> & Channel<R>> Channel<R> forwardTo(C destination){
        if (this.nextOp != null){
            throw new IllegalStateException("Channel has already been operated upon");
        }
        this.nextOp = destination;
        return destination;
    }

    @Override
    public Channel<E_OUT> filter(Predicate<? super E_OUT> predicate) {
        return forwardTo(new FilterOp<>(predicate));
    }

    @Override
    public <R> Channel<R> map(Function<? super E_OUT, ? extends R> mapper) {
        return forwardTo(new MapOp<>(mapper));
    }

    @Override
    public Channel<E_OUT> limit(long maxSize) {
        return forwardTo(new LimitOp<>(maxSize));
    }

    @Override
    public Channel<E_OUT> skip(long n) {
        return forwardTo(new SkipOp<>(n));
    }

    @Override
    public Channel<E_OUT> peek(Consumer<? super E_OUT> action) {
        return forwardTo(new PeekOp<>(action));
    }

    @Override
    public void forEach(Consumer<? super E_OUT> action) {
        this.nextOp = action;
    }
    
}
