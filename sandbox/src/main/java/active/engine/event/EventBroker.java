package active.engine.event;

import active.engine.channel.Channel;
import active.model.event.Event;
import active.model.event.EventStream;

/**
 * @author Maarten Van Puymbroeck
 */
public interface EventBroker<ES extends EventStream> {

    public void fire(Event event);

    public ES on();

}
