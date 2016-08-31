package active.engine.util;

import java.util.function.Function;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public interface ThrowingFunction<T, R> {
    public R apply(T t) throws Exception;

    public default Function<T, R> unchecked(){
        return t -> {
            try {
                return this.apply(t);
            } catch (Exception exc) {
                throw new RuntimeException(exc);
            }
        };
    }

    public static <T, R> Function<T, R> unchecked(ThrowingFunction<T, R> function){
        return function.unchecked();
    }

}
