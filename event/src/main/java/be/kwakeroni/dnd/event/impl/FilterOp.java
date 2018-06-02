package be.kwakeroni.dnd.event.impl;

import java.util.function.Predicate;

class FilterOp<E> extends Pipeline<E, E> {
    
    final Predicate<? super E> predicate;

    public FilterOp(Predicate<? super E> predicate) {
        super();
        this.predicate = predicate;
    }

    @Override
    public void accept(E t) {
        if (this.predicate.test(t)){
            forward(t);
        }
    }
    
    
}
