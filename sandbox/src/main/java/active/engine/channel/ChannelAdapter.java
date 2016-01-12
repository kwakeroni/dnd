package active.engine.channel;

import java.util.function.Consumer;

/**
 * @author Maarten Van Puymbroeck
 */
public abstract class ChannelAdapter<T> extends Pipeline<T,T> implements Channel<T>, Consumer<T> {

            @Override
            public void accept(T t) {
                    forward(t);
            }

}
