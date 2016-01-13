package active.engine.event;

import java.util.Collection;
import java.util.HashSet;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import active.engine.channel.Channel;
import active.engine.channel.ChannelAdapter;
import active.engine.channel.ChannelEntry;

public abstract class EventBrokerSupport<E extends Event, C extends Channel<? super E>> implements EventBroker<E, C> {

    public static EventBrokerSupport<Event, Channel<Event>> newInstance(){
        return new Root<>();
    }

    private EventBrokerSupport(){

    }

    abstract <CA extends ChannelEntry<? super E>> CA registerNewChannel(CA channel);

    public <X extends E> Channel<X> onEvent(Class<X> type){
        return onEvent()
                .filter(event -> type.isInstance(event))
                .map(event -> type.cast(event));
    }

    public <CA extends ChannelAdapter<E>> EventBrokerSupport<E, CA> supplying(Supplier<CA> channelFactory){
        return new ChannelSupplier<E, CA>(this, channelFactory);
    }

    public <CO extends Channel<? super E>> EventBrokerSupport<E, CO> preparing(Function<C, CO> transformer){
        return new Prep<>(this, transformer);
    }

    private static class Root<E extends Event> extends EventBrokerSupport<E, Channel<E>> {

        Collection<ChannelEntry<? super E>> channels;

        private Root(){
            this.channels = new HashSet<>();
        }

        public void fire(E event){
            this.channels.forEach(consumer -> consumer.accept(event));
        }

        @Override
        public Channel<E> onEvent() {
            return registerNewChannel();
        }

        private ChannelEntry<E> registerNewChannel(){
            return this.registerNewChannel(active.engine.channel.Channel.newInstance());
        }

        @Override
        <CA extends ChannelEntry<? super E>> CA registerNewChannel(CA channel) {
            this.channels.add(channel);
            return channel;
        }
    }

    private static abstract class BrokerAdapter<E extends Event, C extends Channel<? super E>, D extends EventBrokerSupport<?,?>> extends EventBrokerSupport<E, C> {
        protected final D delegate;

        public BrokerAdapter(D delegate) {
            this.delegate = delegate;
        }
    }

    private static abstract class ChanAdapter<E extends Event, C extends Channel<? super E>, D extends EventBrokerSupport<E,?>> extends BrokerAdapter<E, C, D> {

        public ChanAdapter(D delegate) {
            super(delegate);
        }

        @Override
        public void fire(E event) {
            delegate.fire(event);
        }

        @Override
        <CA extends ChannelEntry<? super E>> CA registerNewChannel(CA channel) {
            return delegate.registerNewChannel(channel);
        }
    }

    private static class ChannelSupplier<E extends Event, C extends ChannelEntry<? super E>> extends ChanAdapter<E, C, EventBrokerSupport<E, ?>> {

        private final Supplier<C> channelFactory;

        public ChannelSupplier(EventBrokerSupport<E, ?> delegate, Supplier<C> channelFactory) {
            super(delegate);
            this.channelFactory = channelFactory;
        }

        @Override
        public C onEvent() {
            return registerNewChannel(channelFactory.get());
        }

    }


    private static class Prep<E extends Event, C extends Channel<? super E>, OC extends Channel<? super E>> extends ChanAdapter<E, C, EventBrokerSupport<E, OC>> {

        private Function<OC, C> transformer;

        public Prep(EventBrokerSupport<E, OC> delegate, Function<OC, C> transformer) {
            super(delegate);
            this.transformer = transformer;
        }

        @Override
        public C onEvent() {
            return transformer.apply(delegate.onEvent());
        }
    }

}
