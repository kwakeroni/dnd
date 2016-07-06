package active.engine.event;

import java.util.Collection;
import java.util.HashSet;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import active.engine.channel.Channel;
import active.engine.channel.ChannelAdapter;
import active.engine.channel.ChannelEntry;
import active.model.event.Event;
import active.model.event.EventStream;

public abstract class EventBrokerSupport<ES extends EventStream> implements EventBroker<ES> {

    public static EventBrokerSupport<EventStream> newInstance(){
        return new Root();
    }

    private EventBrokerSupport(){

    }

    public <NES extends EventStream> EventBrokerSupport<NES> supplying(Function<? super ES, NES> mapper){
        return new Adapter<NES, EventBrokerSupport<ES>>(this) {
            @Override
            public NES on() {
                return mapper.apply(delegate.on());
            }
            
        };
    }

    public EventBrokerSupport<ES> preparing(Consumer<Event> modifier) {
        return new Adapter<ES, EventBrokerSupport<ES>>(this) {

            @Override
            public void fire(Event event) {
                modifier.accept(event);
                super.fire( event );
            }

        };
    }

    public EventBrokerSupport<ES> preparing(Function<Event, Event> mapper) {
        return new Adapter<ES, EventBrokerSupport<ES>>(this) {

            @Override
            public void fire(Event event) {
                super.fire( mapper.apply(event) );
            }

        };
    }


    
    private static class Root extends EventBrokerSupport<EventStream> {

        Collection<Consumer<Event>> channels;

        private Root(){
            this.channels = new HashSet<>();
        }

        public void fire(Event event){
            this.channels.forEach(consumer -> consumer.accept(event));
        }

        @Override
        public EventStream on() {
            Channel<Event> channel = registerNewChannel();
            return () -> channel;
        }
        
        private <CA extends Channel<Event> & Consumer<Event>> CA registerNewChannel(){
            CA channel = (CA) (Channel<?>) Channel.newInstance();
            this.channels.add(channel);
            return channel;
        };
        
    }
    
    private static abstract class Adapter<NES extends EventStream, D extends EventBroker<?>> extends EventBrokerSupport<NES> {

        protected final D delegate;
        
        protected Adapter(D delegate){
            this.delegate = delegate;
        }
        
        @Override
        public void fire(Event event) {
            delegate.fire(event);           
        }

        @Override
        public NES on() {
            return (NES) delegate.on();
        }
    }


}
