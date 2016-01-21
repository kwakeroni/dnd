package active.model.event;

import active.engine.channel.Channel;
import active.model.event.Event;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author Maarten Van Puymbroeck
 */
public interface Datum<S> extends Supplier<S> {

    public default void onChanged(Reaction reaction){
        onChanged(ignored -> reaction.react());
    }

    public void onChanged(Consumer<Event> consumer);

}
