package active.engine.event;

import java.util.Collection;
import java.util.HashSet;
import java.util.function.Consumer;
import java.util.function.Supplier;

import active.engine.channel.Channel;
import active.engine.channel.ChannelAdapter;

public abstract class EventBrokerSupport<C extends Channel<Event>> {

    Collection<Consumer<Event>> channels;


    public static <C extends ChannelAdapter<Event>> EventBrokerSupport<C> of(Supplier<C> channelFactory){
        return new EventBrokerSupport<C>() {
            @Override
            C registerNewChannel() {
                C channel = channelFactory.get();
                this.channels.add(channel);
                return channel;
            }
        };
    }

    public static EventBrokerSupport<Channel<Event>> ofChannel(){
        return new EventBrokerSupport<Channel<Event>>() {
            @Override
            <CA extends Channel<Event> & Consumer<Event>> Channel<Event> registerNewChannel() {
                CA channel = Channel.newInstance();
                this.channels.add(channel);
                return channel;
            }
        };
    }

    private EventBrokerSupport(){
        this.channels = new HashSet<>();
    }
    
    public void fire(Event event){
        this.channels.forEach(consumer -> consumer.accept(event));
    }
    
    public C onEvent(){
        return registerNewChannel();
    }
    
    public <E extends Event> Channel<E> onEvent(Class<E> type){
        return onEvent()
                .filter(event -> type.isInstance(event))
                .map(event -> type.cast(event));
    }

    abstract <CA extends Channel<Event> & Consumer<Event>> C registerNewChannel();
}
