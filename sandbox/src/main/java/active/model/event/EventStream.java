package active.model.event;

import active.engine.channel.Channel;

@FunctionalInterface
public interface EventStream {
    
    public Channel<Event> event();

    
    public default <E extends Event> Channel<E> event(Class<E> type){
        return ofType(type);
    }
    
    public default <E extends Event> Channel<E> ofType(Class<E> type){
        return event()
                .filter(event -> type.isInstance(event))
                .map( event -> type.cast(event));
    }
}
