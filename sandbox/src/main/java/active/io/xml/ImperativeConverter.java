package active.io.xml;

import active.engine.internal.action.type.ActionTypeSupport;
import active.engine.internal.action.type.AttackActionType;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.*;

import static active.engine.util.ThrowingFunction.unchecked;
import static java.util.function.Function.identity;

public class ImperativeConverter<T, TImpl> implements Converter {

    private Class<T> type;
    private Supplier<TImpl> supplier;
    private Item<T, TImpl> item;

    public ImperativeConverter(Class<T> type, Supplier<TImpl> supplier, Item<T, TImpl> item) {
        this.type = type;
        this.supplier = supplier;
        this.item = item;
    }

    @Override
    public boolean canConvert(Class arg0) {
        return type.isAssignableFrom(arg0);
    }

    @Override
    public void marshal(Object arg0, HierarchicalStreamWriter writer,
                        MarshallingContext context) {

        this.item.marshal(type.cast(arg0), writer, context);

    }


    @Override
    public Object unmarshal(HierarchicalStreamReader reader,
                            UnmarshallingContext context) {
        UnmarshalTarget<TImpl> result = new UnmarshalTarget<>(this.supplier);

        this.item.unmarshal(result, reader, context);

        if (result.get() == null) {
            throw new IllegalStateException("No result for " + reader.getNodeName());
        } else return result.get();
    }

    public interface Item<T, TImpl> {
        public abstract void marshal(T t, HierarchicalStreamWriter writer,
                                     MarshallingContext context);

        public default void unmarshal(UnmarshalTarget<TImpl> result, HierarchicalStreamReader reader,
                                      UnmarshallingContext context) {
            throw new UnsupportedOperationException(this.getClass().getName());
        }
    }

    public static class ComposedItem<T, TImpl> implements Item<T, TImpl> {
        private final List<Item<T, TImpl>> items;

        public ComposedItem(List<Item<T, TImpl>> items) {
            this.items = new java.util.ArrayList<>(items);
        }

        @Override
        public void marshal(T t, HierarchicalStreamWriter writer,
                            MarshallingContext context) {
            items.forEach(item ->
                    item.marshal(t, writer, context));
        }

        @Override
        public void unmarshal(UnmarshalTarget<TImpl> result, HierarchicalStreamReader reader, UnmarshallingContext context) {
            items.forEach(item -> item.unmarshal(result, reader, context));
        }
    }

    public static class TypeSelectingItem<T, TImpl, D> implements Item<T, TImpl> {
        private final Discriminator<T, TImpl, D> discriminator;
        private final Map<D, Item<? extends T, ? extends TImpl>> map;

        public TypeSelectingItem(Discriminator<T, TImpl, D> discriminator, Map<D, Item<? extends T, ? extends TImpl>> map) {
            this.discriminator = discriminator;
            this.map = new HashMap<>(map);
        }

        @Override
        public void marshal(T t, HierarchicalStreamWriter writer, MarshallingContext context) {
            discriminator.getMarshaller().marshal(t, writer, context);
            Item<? extends T, ?> item = Optional.ofNullable(map.get(discriminator.getDiscriminator(t)))
                            .orElseThrow(() -> new IllegalArgumentException("Unrecognized type: " + t + " => " + discriminator.getDiscriminator(t)));
            marshalSubType(item, t, writer, context);
        }

        private <S extends T> void marshalSubType(Item<S, ?> item, T t, HierarchicalStreamWriter writer, MarshallingContext context) {
            item.marshal((S) t, writer, context);
        }

        @Override
        public void unmarshal(UnmarshalTarget<TImpl> result, HierarchicalStreamReader reader, UnmarshallingContext context) {
            UnmarshalTarget<D> discriminatorValue = new UnmarshalTarget<D>();
            discriminator.getMarshaller().unmarshal(discriminatorValue, reader, context);
            unmarshalSubType(this.map.get(discriminatorValue.get()), result, reader, context);
        }

        private <SImpl extends TImpl> void unmarshalSubType(Item<?, SImpl> item, UnmarshalTarget<TImpl> result, HierarchicalStreamReader reader, UnmarshallingContext context) {
            UnmarshalTarget<SImpl> target = new UnmarshalTarget<>();
            item.unmarshal(target, reader, context);
            result.set(target.get());
        }



        public static class Discriminator<T, TImpl, D> {
            private final Function<T, D> mapper;
            private final Item<T, D> marshaller;

            public Discriminator(Function<T, D> mapper, Item<T, D> marshaller) {
                this.mapper = mapper;
                this.marshaller = marshaller;
            }

            public D getDiscriminator(T value){
                return this.mapper.apply(value);
            }

            private final Item<T, D> getMarshaller(){
                return this.marshaller;
            }
        }
    }


    public static abstract class TransformingItem<T, TImpl, S, SImpl> implements Item<T, TImpl> {
        private final Function<T, S> function;
        private final BiConsumer<? super TImpl, ? super SImpl> reaffecter;

        @Deprecated
        public TransformingItem(Function<T, S> function) {
            this(function, TransformingItem.<TImpl, SImpl> unsupported());
        }

        private static <TImpl, SImpl> BiConsumer<? super TImpl, ? super SImpl> unsupported(){
            Exception cause = new Exception();
            cause.fillInStackTrace();
            return (t, s) -> {
                throw new UnsupportedOperationException(cause);
            };
        }

        public TransformingItem(Function<T, S> function, BiConsumer<? super TImpl, ? super SImpl> reaffecter) {
            this.function = function;
            this.reaffecter = reaffecter;
        }

        @Override
        public void marshal(T t, HierarchicalStreamWriter writer,
                            MarshallingContext context) {
            doMarshal(function.apply(t), writer, context);
        }

        protected abstract void doMarshal(S s, HierarchicalStreamWriter writer,
                                          MarshallingContext context);

        @Override
        public void unmarshal(UnmarshalTarget<TImpl> result, HierarchicalStreamReader reader, UnmarshallingContext context) {
            SImpl subResult = doUnmarshal(reader, context);
            this.reaffecter.accept(result.get(), subResult);
        }

        protected SImpl doUnmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
            throw new UnsupportedOperationException(this.getClass().getName());
        }

        public Function<T, S> getMappingFunction() {
            return function;
        }
    }

    public static class ForEach<T, TImpl, S, SImpl> extends TransformingItem<T, TImpl, Iterator<S>, Iterator<SImpl>> {
        private final Item<S, SImpl> delegate;

        public ForEach(Function<T, Iterator<S>> iterator, BiConsumer<? super TImpl, ? super Iterator<SImpl>> reaffecter, Item<S, SImpl> delegate) {
            super(iterator, reaffecter);
            this.delegate = delegate;
        }

        @Override
        public void doMarshal(Iterator<S> iterator, HierarchicalStreamWriter writer,
                              MarshallingContext context) {
            iterator.forEachRemaining(
                    s -> delegate.marshal(s, writer, context));
        }

        @Override
        protected Iterator<SImpl> doUnmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
            Collection<SImpl> collection = new ArrayList<>();

            int i=0;
            System.out.println("Iterating over " + reader.getNodeName());
            while(reader.hasMoreChildren()){
                reader.moveDown();
                System.out.println("Element " + i + "<"+ reader.getNodeName() + "> with " + delegate);
                UnmarshalTarget<SImpl> result = new UnmarshalTarget<>();
                delegate.unmarshal(result, reader, context);
                collection.add(result.get());
                reader.moveUp();
            }
            return collection.iterator();
        }
    }

    public static class ToValue<T, TImpl> extends TransformingItem<T, TImpl, String, String> {
        public ToValue(Function<T, String> function, BiConsumer<? super TImpl, String> reaffecter) {
            super(function, reaffecter);
        }

        @Override
        protected void doMarshal(String s, HierarchicalStreamWriter writer, MarshallingContext context) {
            writer.setValue(s);
        }

        @Override
        protected String doUnmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
            return reader.getValue();
        }
    }

    public static class ToAttribute<T, TImpl> extends TransformingItem<T, TImpl, String, String> {
        private String attributeName;

        public ToAttribute(String attributeName) {
            this(attributeName, extract(attributeName), inject(attributeName));
        }
        public ToAttribute(String attributeName, Function<String, ?> backConverter) {
            this(attributeName, extract(attributeName), inject(attributeName, backConverter));
        }

        public ToAttribute(String attributeName, Function<T, String> function, BiConsumer<? super TImpl, ? super String> reaffecter) {
            super(function, reaffecter);
            this.attributeName = attributeName;
        }

        @Override
        protected void doMarshal(String s, HierarchicalStreamWriter writer,
                                 MarshallingContext context) {
            writer.addAttribute(this.attributeName, s);
        }

        @Override
        protected String doUnmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
            return reader.getAttribute(this.attributeName);
        }

        protected static <T> Function<T, String> extract(String attributeName) {
            return t -> String.valueOf(ImperativeConverter.extract(t, attributeName));
        }

        protected static <T> BiConsumer<T, String> inject(String attributeName){
            return inject(attributeName, identity());
        }
        protected static <T> BiConsumer<T, String> inject(String attributeName, Function<String, ?> backConverter) {
            return (t, s) -> ImperativeConverter.inject(t, attributeName, backConverter.apply(s));
        }
    }

    public static class DirectAttributeItem<T> extends ToAttribute<T, String> {
        public DirectAttributeItem(String attributeName) {
            super(attributeName, extract(attributeName), (t,att)->{});
        }

        public DirectAttributeItem(String attributeName, Function<T, String> function) {
            super(attributeName, function, (t,att)->{});
        }

        @Override
        public void unmarshal(UnmarshalTarget<String> result, HierarchicalStreamReader reader, UnmarshallingContext context) {
            result.set(doUnmarshal(reader, context));
        }
    }

    public static class InElement<S, SImpl, T, TImpl> implements Item<S, SImpl> {
        private final String elementName;
        private final Function<S, T> transformer;
        private final Item<T, TImpl> delegate;
        private final Function<UnmarshalTarget<SImpl>, UnmarshalTarget<TImpl>> unmarshalTargetTransformer;
        private final String debug;

        private InElement(String elementName, Function<S, T> transformer, Function<UnmarshalTarget<SImpl>, UnmarshalTarget<TImpl>> unmarshalTargetTransformer, Item<T, TImpl> delegate, String debug) {
            super();
            this.elementName = elementName;
            this.transformer = transformer;
            this.delegate = delegate;
            this.unmarshalTargetTransformer = unmarshalTargetTransformer;
            this.debug = debug;
        }

        @Override
        public void marshal(S s, HierarchicalStreamWriter writer,
                            MarshallingContext context) {
            writer.startNode(elementName);
            T t = transformer.apply(s);
            if (t == null){
                throw new NullPointerException("in element " + elementName);
            }
            delegate.marshal(t, writer, context);
            writer.endNode();

        }

        @Override
        public void unmarshal(UnmarshalTarget<SImpl> result, HierarchicalStreamReader reader, UnmarshallingContext context) {
            boolean enter;
            if (reader.getNodeName().equals(this.elementName)){
                enter = false;
                System.out.println("Treating wrapper " + this.elementName + " [" + debug +" ]");
            } else {
                enter = true;
                System.out.println("Treating non-matching wrapper " + this.elementName + " at element " + reader.getNodeName() + " [" + debug +" ]");
            }

            if (enter){
                reader.moveDown();
                if (reader.getNodeName().equals(this.elementName)) {
                    System.out.println("Treating wrapper " + this.elementName + " [" + debug +" ]");
                } else {
                    throw new IllegalStateException("Unexpected child element " + reader.getNodeName() + " / expected: " + this.elementName);
                }
            }


            UnmarshalTarget<TImpl> tresult = this.unmarshalTargetTransformer.apply(result);

            this.delegate.unmarshal(tresult,reader, context);

            if (enter){
                reader.moveUp();
            }
        }

        public static <T, TImpl> InElement<T, TImpl, T, TImpl> in(String elementName, Class<TImpl> targetType) {
            return new InElement<>(elementName, identity(), identity(), recurse(targetType), "(name)");
        }

        public static <T, TImpl> InElement<T, TImpl, T, TImpl> in(String elementName, Item<T, TImpl> delegate) {
            return new InElement<>(elementName, identity(), identity(), delegate, "(name, delegate)");
        }

        static <S, SImpl, T, TImpl> InElement<S, SImpl, T, T> in(String elementName, Function<S, T> transformer, Class<T> targetType, BiConsumer<SImpl, T> reaffecter) {
            return new InElement<>(elementName, transformer, s -> new UnmarshalTarget<>(t -> reaffecter.accept(s.get(), t)), recurse(targetType), "(name, transformer, type)");
        }

        private static <S, SImpl, T, TImpl> InElement<S, SImpl, T, TImpl> in(String elementName, Function<S, T> transformer, Item<T, TImpl> delegate, BiConsumer<SImpl, TImpl> reaffecter) {
            return new InElement<>(elementName, transformer, s -> new UnmarshalTarget<>(t -> reaffecter.accept(s.get(), t)), delegate, "(name, transformer, type, delegate)");
        }
    }

    public static class SupplyingItem<T, TImpl> implements Item<T, TImpl> {
        private Item<T, TImpl> delegate;
        private Supplier<? extends TImpl> supplier;

        public SupplyingItem(Item<T, TImpl> delegate, Supplier<? extends TImpl> supplier) {
            this.delegate = delegate;
            this.supplier = supplier;
        }

        @Override
        public void marshal(T t, HierarchicalStreamWriter writer, MarshallingContext context) {
            delegate.marshal(t, writer, context);
        }

        @Override
        public void unmarshal(UnmarshalTarget<TImpl> result, HierarchicalStreamReader reader, UnmarshallingContext context) {
            result.set(supplier.get());
            delegate.unmarshal(result, reader, context);
        }
    }

    static <T, TImpl> RecurseItem<T, TImpl> recurse(Class<TImpl> targetType) {
        return new RecurseItem<>(targetType);
    }

    public static class RecurseItem<T, TImpl> implements Item<T, TImpl> {

        private final Class<TImpl> targetType;

        public RecurseItem(Class<TImpl> targetType) {
            this.targetType = Objects.requireNonNull(targetType);
        }

        @Override
        public void marshal(T t, HierarchicalStreamWriter writer,
                            MarshallingContext context) {
            context.convertAnother(t);
        }

        @Override
        public void unmarshal(UnmarshalTarget<TImpl> result, HierarchicalStreamReader reader, UnmarshallingContext context) {
            System.out.println(context.getRequiredType() + " - " + context.currentObject());
//            if (true) throw new UnsupportedOperationException("targetType");
            result.set((TImpl) context.convertAnother(context.currentObject(), targetType));
            System.out.println();

        }
    }

    private static Object extract(Object item, String property) {

        // todo improve
        try {

            return select(
                    () -> inspector(item, property)
                            .map(unchecked(m -> m.invoke(item))),
                    () -> field(item, property)
                            .map(unchecked(f -> f.get(item)))
            ).orElseThrow(() -> new IllegalArgumentException("Could not get property " + property + " on " + item));


        } catch (Exception exc) {
            return exc.toString();
        }
    }

    private static void inject(Object item, String property, Object child) {
        // todo improve
        try {
            select(
                    () -> mutator(item, property, child)
                            .map(unchecked(m -> { m.invoke(item, child); return true; })),
                    () -> field(item, property)
                            .map(unchecked(f -> { f.set(item, child); return true;}))
            ).orElseThrow(() -> new IllegalArgumentException("Could not set property " + property + " on " + item + " with value " + child));

        } catch (Exception exc) {
            throw new RuntimeException(exc);
        }
    }

    private static <T> Optional<T> select(Supplier<? extends Optional<T>>... optionals) {
        Optional<T> result;
        for (Supplier<? extends Optional<T>> optional : optionals) {
            result = optional.get();
            if (result.isPresent()) {
                return result;
            }
        }
        return Optional.empty();
    }

    private static Optional<Method> inspector(Object item, String property) {
        return findInHierarchy(item.getClass(), c -> inspector0(c, property));
    }

    private static Optional<Method> mutator(Object item, String property, Object param) {
        return findInHierarchy(item.getClass(), c -> mutator0(c, property, param));
    }

    private static Optional<Method> inspector0(Class<?> type, String property) {

        return Arrays.stream(type.getDeclaredMethods())
                .filter(m -> m.getReturnType() != void.class)
                .filter(m -> m.getParameterTypes().length == 0)
                .filter(isInspectorOf(property))
                .peek(m -> m.setAccessible(true))
                .findAny();
    }

    private static Optional<Method> mutator0(Class<?> type, String property, Object param) {
        return Arrays.stream(type.getDeclaredMethods())
                .filter(m -> m.getReturnType() == void.class)
                .filter(m -> m.getParameterTypes().length == 1)
                .filter(isSetterOf(property))
                .filter(m -> m.getParameterTypes()[0].isInstance(param))
                .peek(m -> m.setAccessible(true))
                .findAny();
    }

    private static Predicate<Method> isInspectorOf(String property) {
        return isGetterOf(property).or(isIsserOf(property)).or(m -> property.equals(m.getName()));
    }

    private static Predicate<Method> isGetterOf(String property) {
        return m -> m.getName().startsWith("get")
                && m.getName().substring(3, 4).toLowerCase().equals(property.substring(0, 1).toLowerCase())
                && m.getName().substring(4).equals(property.substring(1).toLowerCase());
    }

    private static Predicate<Method> isIsserOf(String property) {
        return m -> m.getName().startsWith("is")
                && m.getName().substring(2, 3).toLowerCase().equals(property.substring(0, 1).toLowerCase())
                && m.getName().substring(3).equals(property.substring(1).toLowerCase());
    }

    private static Predicate<Method> isSetterOf(String property) {
        return m -> m.getName().startsWith("set")
                && m.getName().length() > 3
                && m.getName().substring(3, 4).toLowerCase().equals(property.substring(0, 1).toLowerCase())
                && m.getName().substring(4).equals(property.substring(1).toLowerCase());
    }

    private static Optional<Field> field(Object item, String fieldName) {
        return findInHierarchy(item.getClass(), c -> field0(c, fieldName));
    }

    private static Optional<Field> field0(Class<?> type, String fieldName) {
        return Arrays.stream(type.getDeclaredFields())
                .filter(f -> fieldName.equals(f.getName()))
                .peek(f -> f.setAccessible(true))
                .findAny();
    }


    private static <S> Optional<S> findInHierarchy(Class<?> seed, Function<Class<?>, Optional<S>> finder) {
        return findFirst(seed, finder,
                c -> Optional.of(c)
                        .filter(Predicate.isEqual(Object.class).negate())
                        .map(Class::getSuperclass));
    }


    private static <T, S> Optional<S> findFirst(T seed, Function<T, Optional<S>> finder, Function<T, Optional<T>> generator) {
        Optional<T> input = Optional.of(seed);
        while (input.isPresent()) {
            Optional<S> result = finder.apply(input.get());
            if (result.isPresent()) {
                return result;
            } else {
                input = generator.apply(input.get());
            }
        }
        return Optional.empty();
    }

    public static void main(String[] args) {
        AttackActionType action = new AttackActionType("Mine");
        System.out.println(extract(action, "name"));
        System.out.println(inspector(action, "name"));
        System.out.println(inspector0(ActionTypeSupport.class, "name"));
    }

    private static Optional<Function<String, ?>> findAttributeBackConverter(Class<?> type) {
        Function<String, ?> f = null;
        if (type == String.class){
            f = identity();
        } else if (type == int.class || type == Integer.class) {
            f = Integer::parseInt;
        }
        return Optional.ofNullable(f);
    }
    
    private static class UnmarshalTarget<TImpl> {
        private TImpl value;
        private final Supplier<? extends TImpl> supplier;
        private final Consumer<? super TImpl> effect;

        public UnmarshalTarget(){
            this(null, null);
        }

        public UnmarshalTarget(TImpl value){
            this(null, null);
            this.value = value;
        }

        public UnmarshalTarget(Supplier<? extends TImpl> supplier){
            this(supplier, null);
        }

        public UnmarshalTarget(Consumer<? super TImpl> effect) { this(null, effect); }

        private UnmarshalTarget(Supplier<? extends TImpl> supplier, Consumer<? super TImpl> effect){
            this.supplier = supplier;
            this.effect = effect;
        }
        
//        public UnmarshalTarget(Supplier<TImpl> supplier){
//            this.supplier = supplier;
//        }
        
        
        public TImpl get(){
            if (this.value == null && this.supplier != null){
                this.value = this.supplier.get();
            }
            if (this.value == null){
                throw new IllegalStateException("value not set");
            }
            return this.value;
        }
        
        public void set(TImpl value){
            if (this.value != null){
                throw new IllegalStateException("Value previously set: " + this.value);
            }
            this.value = value;
            if (this.effect != null){
                this.effect.accept(this.value);
            }
        }
        
//        public void setNewInstance(){
//            this.value = this.supplier.get();
//        }

    }
}