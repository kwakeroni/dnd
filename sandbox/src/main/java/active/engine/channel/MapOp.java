package active.engine.channel;

import java.util.function.Function;

class MapOp<E_IN, E_OUT> extends Pipeline<E_IN, E_OUT> {

    final Function<? super E_IN, ? extends E_OUT> mapper;

    public MapOp(Function<? super E_IN, ? extends E_OUT> mapper) {
        super();
        this.mapper = mapper;
    }

    @Override
    public void accept(E_IN t) {
        forward(mapper.apply(t));
    }
    
    
    
}
