package active.engine.event;

import java.util.Collection;
import java.util.HashSet;
import java.util.function.Consumer;

import active.engine.channel.Channel;

public class EventBroker {

    private Collection<Consumer<Event>> channels;
    
    public EventBroker(){
        this.channels = new HashSet<>();
    }
    
    public void fire(Event event){
        this.channels.forEach(consumer -> consumer.accept(event));
    }
    
    public Channel<Event> onEvent(){
        return registerNewChannel();
    }
    
    public <E extends Event> Channel<E> onEvent(Class<E> type){
        return onEvent()
                .filter(event -> type.isInstance(event))
                .map(event -> type.cast(event));
    }
    
    private <ChannelEntry extends Channel<Event> & Consumer<Event>> Channel<Event> registerNewChannel(){
        ChannelEntry channel = Channel.<Event, ChannelEntry> newInstance();
        this.channels.add(channel);
        return channel;
    }
}
