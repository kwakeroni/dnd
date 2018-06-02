package be.kwakeroni.dnd.io.xml;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import be.kwakeroni.dnd.io.xml.ImperativeConverter.*;

public class ConverterBuilder<T, TImpl, CB extends ConverterBuilder<T, TImpl, CB>> {

    protected final List<Item<T, TImpl>> items;
    private final Class<T> type;
    private final Supplier<? extends TImpl> supplier;

//    public ConverterBuilder(Class<T> type, Class<TImpl> implType) {
//        this(type, byDefaultConstructor(implType));
//    }

    public ConverterBuilder(Class<T> type, Supplier<? extends TImpl> implSupplier) {
        this.type = type;
        this.supplier = implSupplier;
        this.items = new ArrayList<>();
    }

//    public static <T, CB extends ConverterBuilder<T, T, CB>> ConverterBuilder<T, T, CB> converter(Class<T> type) {
//        return new ConverterBuilder<>(type, type);
//    }

//    public static <T, TImpl, CB extends ConverterBuilder<T, TImpl, CB>> ConverterBuilder<T, TImpl, CB> converter(Class<T> type, Class<TImpl> implType) {
//        return new ConverterBuilder<>(type, implType);
//    }

    public static <T, TImpl, CB extends ConverterBuilder<T, TImpl, CB>> ConverterBuilder<T, TImpl, CB> converter(Class<T> type, Supplier<? extends TImpl> implSupplier) {
        return new ConverterBuilder<>(type, implSupplier);
    }

//    private static <Q> Supplier<Q> byDefaultConstructor(Class<Q> targetType){
//        return () -> { try {
//                Constructor<Q> constructor = targetType.getDeclaredConstructor();
//                constructor.setAccessible(true);
//                return constructor.newInstance();
//            } catch (Exception e) {
//                throw new IllegalStateException(e);
//            }};
//    }

    public ImperativeConverter<T, TImpl> build() {
        return new ImperativeConverter<>(type, supplier, new ImperativeConverter.ComposedItem<>(this.items));
    }

    public CB withAttribute(String attributeName, Function<T, String> value, BiConsumer<TImpl, String> reaffecter) {
        return withItem(new ImperativeConverter.ToAttribute<>(attributeName, value, reaffecter));
    }

    public CB withAttribute(String attributeName, Function<String, ?> backConverter){
        return withItem(new ImperativeConverter.ToAttribute<>(attributeName, backConverter));
    }

    public CB withAttributes(String... attributeNames) {
        for (String attributeName : attributeNames) {
            withItem(new ImperativeConverter.ToAttribute<>(attributeName));
        }
        return (CB) this;
    }

    public CB withValue(Function<T, String> value, BiConsumer<? super TImpl, String> reaffecter) {
        return withItem(new ImperativeConverter.ToValue<>(value, reaffecter));
    }

    public <S> CB withElement(String name, Class<S> targetType, Function<T, S> from, BiConsumer<TImpl, S> reaffecter) {
        return withItem(ImperativeConverter.InElement.in(name, from, targetType, reaffecter));
    }

    public <S, SImpl> EmbeddedElement<S, SImpl, T, TImpl, CB> withStream(String containerName, Class<SImpl> elementType, Supplier<? extends SImpl> elementSupplier, Function<T, Stream<S>> from, BiConsumer<? super TImpl, ? super SImpl> addTo) {
        return withForEachInContainer(
                containerName,
                elementType,
                elementSupplier,
                from.andThen(Stream::iterator),
                (TImpl t, Iterator<SImpl> iter) -> iter.forEachRemaining(
                        s -> addTo.accept(t, s)));
    }


    public <S, SImpl> EmbeddedElement<S, SImpl, T, TImpl, CB> withStream(Class<SImpl> elementType, Supplier<? extends SImpl> elementSupplier, Function<T, Stream<S>> from, BiConsumer<? super TImpl, ? super SImpl> addTo) {
        return withForEach(
                elementType,
                elementSupplier,
                from.andThen(Stream::iterator),
                (TImpl t, Iterator<SImpl> iter) -> iter.forEachRemaining(s -> addTo.accept(t, s)));
    }

    public <S, SImpl> EmbeddedElement<S, SImpl, T, TImpl, CB> withElements(String containerName, Class<SImpl> elementType, Supplier<? extends SImpl> elementSupplier, Function<T, Iterable<S>> from, BiConsumer<? super TImpl, ? super SImpl> addTo) {
        return withForEachInContainer(
                containerName,
                elementType,
                elementSupplier,
                from.andThen(Iterable::iterator),
                (TImpl t, Iterator<SImpl> iter) -> iter.forEachRemaining(s -> addTo.accept(t, s)));
    }

    public <S, SImpl> EmbeddedElement<S, SImpl, T, TImpl, CB> withElements(Class<SImpl> elementType, Supplier<? extends SImpl> elementSupplier, Function<T, Iterable<S>> from, BiConsumer<? super TImpl, ? super SImpl> addTo) {
        return withForEach(
                elementType,
                elementSupplier,
                from.andThen(Iterable::iterator),
                (TImpl t, Iterator<SImpl> iter) -> iter.forEachRemaining(s -> addTo.accept(t, s)));
    }

    private <S, SImpl> EmbeddedElement<S, SImpl, T, TImpl, CB> withForEachInContainer(String containerName, Class<SImpl> elementType, Supplier<? extends SImpl> elementSupplier, Function<T, Iterator<S>> iterator, BiConsumer<? super TImpl, ? super Iterator<SImpl>> reaffecter) {

        // Function<T, Iterator<S>> iterator, BiConsumer<? super TImpl, ? super Iterator<SImpl>> reaffecter, Item<S, SImpl> delegate

        Function<Item<S, SImpl>, Item<T, TImpl>> integrator = elementConverter ->
                ImperativeConverter.InElement.in(containerName,
                        new ImperativeConverter.ForEach<>(iterator, reaffecter, elementConverter));

        return new EmbeddedElement<S, SImpl, T, TImpl, CB>((CB) this, elementType, elementSupplier, integrator);
    }

    private <S, SImpl> EmbeddedElement<S, SImpl, T, TImpl, CB> withForEach(Class<SImpl> elementType, Supplier<? extends SImpl> elementSupplier, Function<T, Iterator<S>> iterator, BiConsumer<? super TImpl, ? super Iterator<SImpl>> reaffecter) {
        Function<Item<S,SImpl>, Item<T, TImpl>> integrator = elementConverter -> new ImperativeConverter.ForEach<>(iterator, reaffecter, elementConverter);

        return new EmbeddedElement<>((CB) this, elementType, elementSupplier, integrator);
    }

    public SubtypingBuilder<CB, T, TImpl, String> withSubtypeAttribute(String attribute){
        ImperativeConverter.DirectAttributeItem<T> toAttribute = new ImperativeConverter.DirectAttributeItem<>(attribute);
        ImperativeConverter.TypeSelectingItem.Discriminator<T, TImpl, String> discriminator =
                new ImperativeConverter.TypeSelectingItem.Discriminator<>(
                        toAttribute.getMappingFunction(),
                        toAttribute
                );
        return new SubtypingBuilder<>((CB) this, discriminator);
    }

     CB withItem(Item<T, TImpl> item) {
        this.items.add(item);
        return (CB) this;
    }

    public static class EmbeddedConverterBuilder<T, TImpl, Parent, CB extends EmbeddedConverterBuilder<T, TImpl, Parent, CB>> extends ConverterBuilder<T, TImpl, CB> {

        private final Parent parent;
        private final BiConsumer<Parent, Item<T, TImpl>> integrator;
        private final Class<TImpl> targetType;

        public EmbeddedConverterBuilder(Parent parent, BiConsumer<Parent, Item<T, TImpl>> integrator, Class<TImpl> targetType, Supplier<? extends TImpl> targetSupplier) {
            super(null, targetSupplier);
            this.parent = parent;
            this.integrator = integrator;
            this.targetType = targetType;
        }

        public Parent done() {
            Item<T, TImpl> delegate = (this.items.isEmpty()) ? ImperativeConverter.recurse(this.targetType) : new ComposedItem<>(this.items);
            this.integrator.accept(this.parent, delegate);
            return this.parent;
        }

        @Override
        public ImperativeConverter<T, TImpl> build() {
            throw new UnsupportedOperationException();
        }
    }

    public static class EmbeddedElement<T, TImpl, P, PImpl, PCB extends ConverterBuilder<P, PImpl, PCB>> {

        private final PCB parent;
        private final Function<Item<T, TImpl>, Item<P, PImpl>> integrator;
        private final Class<TImpl> targetType;
        private final Supplier<? extends TImpl> targetSupplier;

        public EmbeddedElement(PCB parent, Class<TImpl> targetType, Supplier<? extends TImpl> targetSupplier, Function<Item<T, TImpl>, Item<P, PImpl>> integrator) {
            this.parent = parent;
            this.integrator = integrator;
            this.targetType = targetType;
            this.targetSupplier = targetSupplier;
        }

        EmbeddedConverterBuilder<T, TImpl, PCB, ?> as(String element) {
            Function<Item<T, TImpl>, Item<P, PImpl>> embeddedIntegrator = item -> integrator.apply(InElement.in(element, item));

            BiConsumer<PCB, Item<T, TImpl>> parentIntegrator =
                    (p, delegate) -> p.withItem(embeddedIntegrator.apply(delegate));

            return new EmbeddedConverterBuilder<>(parent, parentIntegrator, targetType, targetSupplier);
        }

    }

    public static class SubtypingBuilder<Parent extends ConverterBuilder<P,PImpl,Parent>, P, PImpl, D> {

        private final Parent parent;
        private final ImperativeConverter.TypeSelectingItem.Discriminator<P, PImpl, D> discriminator;
        private final Map<D, Item<? extends P,? extends PImpl>> types = new HashMap<>();

        public SubtypingBuilder(Parent parent, ImperativeConverter.TypeSelectingItem.Discriminator<P, PImpl, D> discriminator) {
            this.parent = parent;
            this.discriminator = discriminator;
        }

        private <T extends P, TImpl extends T> void addSubType(D discriminator, Item<T,TImpl> item, Supplier<? extends TImpl> targetSupplier){
            this.types.put(discriminator, (Item<? extends P, ? extends PImpl>) new SupplyingItem<>( item, targetSupplier));
        }

        public <T extends P> EmbeddedConverterBuilder<T,T,SubtypingBuilder<Parent,P,PImpl,D>,?> withSubType(D discriminator, Class<T> targetType, Supplier<? extends T> targetSupplier){
            return new EmbeddedConverterBuilder<>(this, (p, b) -> p.addSubType(discriminator, b, targetSupplier), targetType, targetSupplier);
        }

        public Parent done(){
            return parent.withItem(new TypeSelectingItem<>(this.discriminator, this.types));
        }
    }

}
