package be.kwakeroni.dnd.event;

@FunctionalInterface
public interface EventStream {
    
    public Channel<Event> event();

    public default <E extends Event> Channel<E> event(Class<E> type){
        return ofType(type);
    }
    
    public default <E extends Event> Channel<E> ofType(Class<E> type){
        return event()
                .filter(type::isInstance)
                .map(type::cast);
    }
}
