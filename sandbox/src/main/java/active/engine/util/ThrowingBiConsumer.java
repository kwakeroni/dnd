package active.engine.util;

import java.util.function.BiConsumer;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public interface ThrowingBiConsumer<T, U> {

    void accept(T t, U u) throws Exception;


    public default BiConsumer<T, U> unchecked(){
        return (t, u) -> {
            try {
                this.accept(t, u);
            } catch (Exception exc) {
                throw new RuntimeException(exc);
            }
        };
    }

    public static <T, U> BiConsumer<T, U> unchecked(ThrowingBiConsumer<T, U> consumer){
        return consumer.unchecked();
    }

}
