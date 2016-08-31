package active.io.xml;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import active.engine.internal.action.type.ActionTypeSupport;
import active.engine.internal.action.type.AttackActionType;
import active.model.fight.command.Attack;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;


;import static active.engine.util.ThrowingFunction.unchecked;

public class ImperativeConverter<T> implements Converter {

    public Class<T> type;
    public Item<T> item;

    public ImperativeConverter(Class<T> type, Item<T> item) {
        this.type = type;
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

    private T newInstance(){
        try {
            return this.type.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader,
                            UnmarshallingContext context) {
        AtomicReference<T> result = new AtomicReference<>(newInstance());
        this.item.unmarshal(result, reader, context);

        if (result.get() == null){
            throw new IllegalStateException("No result for " + reader.getNodeName());
        } else return result.get();
    }

    public interface Item<T> {
        public abstract void marshal(T t, HierarchicalStreamWriter writer,
                                     MarshallingContext context);

        public default void unmarshal(AtomicReference<T> result, HierarchicalStreamReader reader,
                                      UnmarshallingContext context){
            throw new UnsupportedOperationException(this.getClass().getName());
        }
    }

    public static class ComposedItem<T> implements Item<T> {
        private final List<Item<T>> items;

        public ComposedItem(List<Item<T>> items) {
            this.items = new java.util.ArrayList<>(items);
        }

        @Override
        public void marshal(T t, HierarchicalStreamWriter writer,
                            MarshallingContext context) {
            items.forEach(item ->
                    item.marshal(t, writer, context));
        }

        @Override
        public void unmarshal(AtomicReference<T> result, HierarchicalStreamReader reader, UnmarshallingContext context) {
            items.forEach( item -> item.unmarshal(result, reader, context));
        }
    }

    public static class TypeSelectingItem<T> implements Item<T> {
        private final Map<Class<?>, Item<? extends T>> map;

        public TypeSelectingItem(Map<Class<?>, Item<? extends T>> map) {
            this.map = new HashMap<>(map);
        }

        @Override
        public void marshal(T t, HierarchicalStreamWriter writer, MarshallingContext context) {
            Item<? extends T> item =
                    map.entrySet().stream()
                            .filter(c -> c.getKey().isInstance(t))
                            .findAny()
                            .map(Map.Entry::getValue)
                            .orElseThrow(() -> new IllegalArgumentException("Unrecognized type: " + t));
            marshalSubType(item, t, writer, context);
        }

        private <S extends T> void marshalSubType(Item<S> item, T t, HierarchicalStreamWriter writer, MarshallingContext context) {
            item.marshal((S) t, writer, context);
        }

    }


    public static abstract class TransformingItem<T, S> implements Item<T> {
        private final Function<T, S> function;
        private final BiConsumer<S, T> reaffecter;

        @Deprecated
        public TransformingItem(Function<T, S> function) {
            this(function, s -> {throw new UnsupportedOperationException(); });
        }

        public TransformingItem(Function<T, S> function, Function<S, T> reaffecter) {
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
        public void unmarshal(AtomicReference<T> result, HierarchicalStreamReader reader, UnmarshallingContext context) {
            AtomicReference<S> subResult = new AtomicReference<>(null);
            doUnmarshal(subResult, reader, context));
        }

        protected void doUnmarshal(T t, HierarchicalStreamReader reader, UnmarshallingContext context){
            throw new UnsupportedOperationException(this.getClass().getName());
        }
    }

    public static class ForEach<T, S> extends TransformingItem<T, Iterator<S>> {
        private final Item<? super S> delegate;

        public ForEach(Function<T, Iterator<S>> iterator, Item<? super S> delegate) {
            super(iterator);
            this.delegate = delegate;
        }

        @Override
        public void doMarshal(Iterator<S> iterator, HierarchicalStreamWriter writer,
                              MarshallingContext context) {
            iterator.forEachRemaining(
                    s -> delegate.marshal(s, writer, context));
        }

    }

    public static class ToValue<T> extends TransformingItem<T, String> {
        public ToValue(Function<T, String> function) {
            super(function);
        }

        @Override
        protected void doMarshal(String s, HierarchicalStreamWriter writer, MarshallingContext context) {
            writer.setValue(s);
        }
    }

    public static class ToAttribute<T> extends TransformingItem<T, String> {
        private String attributeName;

        public ToAttribute(String attributeName) {
            this(extract(attributeName), attributeName);
        }

        public ToAttribute(Function<T, String> function, String attributeName) {
            super(function);
            this.attributeName = attributeName;
        }

        @Override
        protected void doMarshal(String s, HierarchicalStreamWriter writer,
                                 MarshallingContext context) {
            writer.addAttribute(this.attributeName, s);

        }

        private static <T> Function<T, String> extract(String attributeName) {
            return t -> String.valueOf(ImperativeConverter.extract(t, attributeName));
        }
    }

    public static class InElement<S, T> implements Item<S> {
        private final String elementName;
        private final Function<S, T> transformer;
        private final Item<T> delegate;

        private InElement(String elementName, Function<S, T> transformer, Item<T> delegate) {
            super();
            this.elementName = elementName;
            this.transformer = transformer;
            this.delegate = delegate;
        }

        @Override
        public void marshal(S s, HierarchicalStreamWriter writer,
                            MarshallingContext context) {
            writer.startNode(elementName);
            T t = transformer.apply(s);
            delegate.marshal(t, writer, context);
            writer.endNode();

        }

        public static <T> InElement<T, T> in(String elementName){
            return new InElement<>(elementName, Function.identity(), recurse());
        }

        public static <T> InElement<T, T> in(String elementName, Item<T> delegate){
            return new InElement<>(elementName, Function.identity(), delegate);
        }

        public static <S, T> InElement<S, T> in(String elementName, Function<S, T> transformer){
            return new InElement<>(elementName, transformer, recurse());
        }

        public static <S, T> InElement<S, T> in(String elementName, Function<S, T> transformer, Item<T> delegate){
            return new InElement<>(elementName, transformer, delegate);
        }
    }


    private static RecurseItem<Object> RECURSE = new RecurseItem<>();

    static <T> RecurseItem<T> recurse() {
        return (RecurseItem<T>) RECURSE;
    }

    public static class RecurseItem<T> implements Item<T> {

        @Override
        public void marshal(T t, HierarchicalStreamWriter writer,
                            MarshallingContext context) {
            context.convertAnother(t);
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

    private static Predicate<Method> isInspectorOf(String property) {
        return isGetterOf(property).or(isIsserOf(property)).or(m -> property.equals(m.getName()));
    }

    private static Optional<Method> inspector(Object item, String property) {
        return findInHierarchy(item.getClass(), c -> inspector0(c, property));
    }

    private static Optional<Method> inspector0(Class<?> type, String property) {

        return Arrays.stream(type.getDeclaredMethods())
                .filter(m -> m.getReturnType() != void.class)
                .filter(m -> m.getParameterTypes().length == 0)
                .filter(isGetterOf(property))
                .peek(m -> m.setAccessible(true))
                .findAny();
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

}