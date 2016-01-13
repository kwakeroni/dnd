package active.engine.event;

import active.engine.channel.Channel;

/**
 * @author Maarten Van Puymbroeck
 */
public interface EventBroker<E extends Event, C extends Channel<? super E>> {

    public void fire(E event);

    public C onEvent();

    public <X extends E> Channel<X> onEvent(Class<X> type);
}
