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
    
    public static <T> ConverterBuilder<T> converter(){
        return new ConverterBuilder<T>();
    }
    
    public static <T> ConverterBuilder<T> converter(Class<T> type){
        return new ConverterBuilder<T>();
    }
    
    
    public ImperativeConverter<T> build(Class<T> type){
        return new ImperativeConverter<T>(type, new ImperativeConverter.ComposedItem<>(this.items));
    }
    
    public ConverterBuilder<T> withAttribute(String attributeName, Function<T, String> value){
        return withItem(new ImperativeConverter.ToAttribute<>(value, attributeName));
    }
    
   public <S> EmbeddedElement<S, T> withStream(Function<T, Stream<S>> from){
       Function<T, Iterator<S>> iterator = from.andThen(Stream::iterator);
       return new EmbeddedElement<>(this, 
               elementConverter -> new ImperativeConverter.ForEach<>(iterator, elementConverter));
   }
   
   public <S> EmbeddedElement<S, T> withElements(Function<T, Iterable<S>> from) {
       Function<T, Iterator<S>> iterator = from.andThen(Iterable::iterator);
       return new EmbeddedElement<>(this, 
               elementConverter -> new ImperativeConverter.ForEach<>(iterator, elementConverter));
       
   }
   
   private ConverterBuilder<T> withItem(Item<T> item){
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
    
    public ConverterBuilder<P> done(){
        Item<T> delegate = (this.items.isEmpty())? ImperativeConverter.recurse() : new ComposedItem<>(this.items);
        return this.parent.withItem(integrator.apply( delegate ));
    }
       
   }
   
   public static class EmbeddedElement<T, P> {
       
       private final ConverterBuilder<P> parent;
       private final Function<Item<T>, Item<P>> integrator;
       
       public EmbeddedElement(ConverterBuilder<P> parent, Function<Item<T>, Item<P>> integrator) {
           this.parent = parent;
           this.integrator = integrator;
       }       
       
       EmbeddedConverterBuilder<T, P> as(String element){
           Function<Item<T>, Item<P>> embeddedIntegrator = item -> integrator.apply(new InElement<>(element, item));
           return new EmbeddedConverterBuilder<>(parent, embeddedIntegrator);
       }

   }

}
