package be.kwakeroni.dnd.util.function;

import java.util.function.Consumer;

/**
 * @author Maarten Van Puymbroeck
 */
@FunctionalInterface
public interface Reaction {

    public void react();

    default <T> Consumer<T> asConsumer(){
        return __ -> this.react();
    }
}
