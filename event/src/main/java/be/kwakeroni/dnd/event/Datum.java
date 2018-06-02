package be.kwakeroni.dnd.event;

import be.kwakeroni.dnd.util.function.Reaction;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author Maarten Van Puymbroeck
 */
public interface Datum<S> extends Supplier<S> {

    public default void onChanged(Reaction reaction){
        onChanged(reaction.asConsumer());
    }

    public void onChanged(Consumer<Event> consumer);

}
