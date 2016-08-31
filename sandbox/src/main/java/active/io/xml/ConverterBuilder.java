package active.io.xml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import active.io.xml.ImperativeConverter.*;
import active.model.creature.Party;

public class ConverterBuilder<T> {

    protected final List<Item<T>> items;

    public ConverterBuilder() {
        this.items = new ArrayList<>();
    }

    public static <T> ConverterBuilder<T> converter() {
        return new ConverterBuilder<T>();
    }

    public static <T> ConverterBuilder<T> converter(Class<T> type) {
        return new ConverterBuilder<T>();
    }


    public ImperativeConverter<T> build(Class<T> type) {
        return new ImperativeConverter<T>(type, new ImperativeConverter.ComposedItem<>(this.items));
    }

    public ConverterBuilder<T> withAttribute(String attributeName, Function<T, String> value) {
        return withItem(new ImperativeConverter.ToAttribute<>(value, attributeName));
    }

    public ConverterBuilder<T> withAttributes(String... attributeNames) {
        for (String attributeName : attributeNames) {
            withItem(new ImperativeConverter.ToAttribute<>(attributeName));
        }
        return this;
    }

    public ConverterBuilder<T> withValue(Function<T, String> value) {
        return withItem(new ImperativeConverter.ToValue<>(value));
    }

    public <S> ConverterBuilder<T> withElement(String name, Function<T, S> from) {
        return withItem(ImperativeConverter.InElement.in(name, from));
    }

    public <S> EmbeddedElement<S, T> withStream(String containerName, Function<T, Stream<S>> from) {
        return withForEachInContainer(containerName, from.andThen(Stream::iterator));
    }


    public <S> EmbeddedElement<S, T> withStream(Function<T, Stream<S>> from) {
        return withForEach(from.andThen(Stream::iterator));
    }

    public <S> EmbeddedElement<S, T> withElements(String containerName, Function<T, Iterable<S>> from) {
        return withForEachInContainer(containerName, from.andThen(Iterable::iterator));
    }

    public <S> EmbeddedElement<S, T> withElements(Function<T, Iterable<S>> from) {
        return withForEach(from.andThen(Iterable::iterator));
    }

    private <S> EmbeddedElement<S, T> withForEachInContainer(String containerName, Function<T, Iterator<S>> iterator) {
        return new EmbeddedElement<>(this, elementConverter ->
                ImperativeConverter.InElement.in(containerName,
                        new ImperativeConverter.ForEach<>(iterator, elementConverter)));
    }

    private <S> EmbeddedElement<S, T> withForEach(Function<T, Iterator<S>> iterator) {
        return new EmbeddedElement<>(this,
                elementConverter -> new ImperativeConverter.ForEach<>(iterator, elementConverter));
    }

    private ConverterBuilder<T> withItem(Item<T> item) {
        this.items.add(item);
        return this;
    }

    public static class EmbeddedConverterBuilder<T, P> extends ConverterBuilder<T> {

        private final ConverterBuilder<P> parent;
        private final Function<Item<T>, Item<P>> integrator;

        public EmbeddedConverterBuilder(ConverterBuilder<P> parent, Function<Item<T>, Item<P>> integrator) {
            this.parent = parent;
            this.integrator = integrator;
        }

        public ConverterBuilder<P> done() {
            Item<T> delegate = (this.items.isEmpty()) ? ImperativeConverter.recurse() : new ComposedItem<>(this.items);
            return this.parent.withItem(integrator.apply(delegate));
        }

    }

    public static class EmbeddedElement<T, P> {

        private final ConverterBuilder<P> parent;
        private final Function<Item<T>, Item<P>> integrator;

        public EmbeddedElement(ConverterBuilder<P> parent, Function<Item<T>, Item<P>> integrator) {
            this.parent = parent;
            this.integrator = integrator;
        }

        EmbeddedConverterBuilder<T, P> as(String element) {
            Function<Item<T>, Item<P>> embeddedIntegrator = item -> integrator.apply(InElement.in(element, item));
            return new EmbeddedConverterBuilder<>(parent, embeddedIntegrator);
        }

    }

}
