package active.io.xml;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;


;public class ImperativeConverter<T> implements Converter {
    
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

    @Override
    public Object unmarshal(HierarchicalStreamReader arg0,
            UnmarshallingContext arg1) {
        // TODO Auto-generated method stub
        return null;
    }
    
    public interface Item<T> {
        public abstract void marshal(T t, HierarchicalStreamWriter writer,
                MarshallingContext context);
    }
    
    public static class ComposedItem<T> implements Item<T> {
        private final List<Item<T>> items;
        
        public ComposedItem(List<Item<T>> items){
            this.items = new java.util.ArrayList<>(items);
        }

        @Override
        public void marshal(T t, HierarchicalStreamWriter writer,
                MarshallingContext context) {
           items.forEach(item -> 
               item.marshal(t,  writer, context));            
        }
        
        
    }
    
    public static abstract class TransformingItem<T, S> implements Item<T> {
        private Function<T, S> function;

        public TransformingItem(Function<T, S> function) {
            this.function = function;
        }

        @Override
        public void marshal(T t, HierarchicalStreamWriter writer,
                MarshallingContext context) {
            doMarshal(function.apply(t), writer, context);
        }
        
        protected abstract void doMarshal(S s, HierarchicalStreamWriter writer,
                MarshallingContext context);
       
    }
    
    public static class ForEach<T, S> extends TransformingItem<T, Iterator<S>> {
        private final Item<? super S> delegate;
        
        public ForEach(Function<T, Iterator<S>> iterator, Item<? super S> delegate){
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
    
    public static class ToAttribute<T> extends TransformingItem<T, String> {
        private String attributeName;

        public ToAttribute(Function<T, String> function, String attributeName) {
            super(function);
            this.attributeName = attributeName;
        }

        @Override
        protected void doMarshal(String s, HierarchicalStreamWriter writer,
                MarshallingContext context) {
            writer.addAttribute(this.attributeName, s);
            
        }
    }
    
    public static class InElement<T> implements Item<T> {
        private String elementName;
        private Item<T> delegate;
        
        public InElement(String elementName){
            this(elementName, recurse());
        }
        
        public InElement(String elementName, Item<T> delegate) {
            super();
            this.elementName = elementName;
            this.delegate = delegate;
        }

        @Override
        public void marshal(T t, HierarchicalStreamWriter writer,
                MarshallingContext context) {
            writer.startNode(elementName);
            delegate.marshal(t, writer, context);
            writer.endNode();
            
        }
        
    }
    
    
    
    private static RecurseItem<Object> RECURSE = new RecurseItem<>();
    
    static <T> RecurseItem<T> recurse(){
        return (RecurseItem<T>) RECURSE;
    }
    
    public static class RecurseItem<T> implements Item<T> {

        @Override
        public void marshal(T t, HierarchicalStreamWriter writer,
                MarshallingContext context) {
            context.convertAnother(t);            
        }
        
    }
    
    
}