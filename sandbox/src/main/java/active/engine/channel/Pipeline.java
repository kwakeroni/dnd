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
        return forwardTo(new FilterOp<E_OUT>(predicate));
    }

    @Override
    public <R> Channel<R> map(Function<? super E_OUT, ? extends R> mapper) {
        return forwardTo(new MapOp<E_OUT, R>(mapper));
    }

    @Override
    public Channel<E_OUT> limit(long maxSize) {
        return forwardTo(new LimitOp<E_OUT>(maxSize));
    }

    @Override
    public Channel<E_OUT> skip(long n) {
        return forwardTo(new SkipOp<E_OUT>(n));
    }

    @Override
    public void forEach(Consumer<? super E_OUT> action) {
        this.nextOp = action;
    }
    
}
