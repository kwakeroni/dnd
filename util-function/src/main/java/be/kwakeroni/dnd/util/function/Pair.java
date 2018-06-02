package be.kwakeroni.dnd.util.function;

import java.util.function.Function;

/**
 * @author Maarten Van Puymbroeck
 */
public final /* value */ class Pair<A, B> {
    private final A a;
    private final B b;

    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public A getA(){
        return a;
    }

    public B getB(){
        return b;
    }

    public <C> Pair<C, B> withA(C c){
        return new Pair<>(c, b);
    }

    public <C> Pair<A, C> withB(C c){
        return new Pair<>(a, c);
    }

    public <C> Pair<C, B> mapA(Function<A, C> mapper) {
        return new Pair<>(mapper.apply(a), b);
    }

    public <C> Pair<A, C> mapB(Function<B, C> mapper) {
        return new Pair<>(a, mapper.apply(b));
    }

    public Pair<B, A> invert(){
        return new Pair<>(b, a);
    }

}
