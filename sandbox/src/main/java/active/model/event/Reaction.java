package active.model.event;

import java.util.function.Consumer;

/**
 * @author Maarten Van Puymbroeck
 */
@FunctionalInterface
public interface Reaction {

    public void react();

    default <T> Consumer<? super T> asConsumer(){
        return t -> this.react();
    }
}
