package active.engine.channel;

import java.util.function.Consumer;

/**
 * @author Maarten Van Puymbroeck
 */
public interface ChannelEntry<T> extends Channel<T>, Consumer<T> {


}
