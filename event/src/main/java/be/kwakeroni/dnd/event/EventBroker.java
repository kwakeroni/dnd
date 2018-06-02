package be.kwakeroni.dnd.event;

import be.kwakeroni.dnd.event.Event;
import be.kwakeroni.dnd.event.EventStream;

/**
 * @author Maarten Van Puymbroeck
 */
public interface EventBroker<ES extends EventStream> {

    public void fire(Event event);

    public ES on();

}
