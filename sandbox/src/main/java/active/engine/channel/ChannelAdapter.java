package active.engine.channel;

/**
 * @author Maarten Van Puymbroeck
 */
public abstract class ChannelAdapter<T> extends Pipeline<T,T> implements ChannelEntry<T> {

            @Override
            public void accept(T t) {
                    forward(t);
            }

}
