package be.kwakeroni.dnd.type.description;

import be.kwakeroni.dnd.type.collection.HierarchicalClassMap;
import be.kwakeroni.dnd.type.base.Describable;
import be.kwakeroni.dnd.type.base.Description;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author Maarten Van Puymbroeck
 */
public class DecoratedDescription implements Description {

    private final HierarchicalClassMap<Describable, BiConsumer<DecoratedDescription, ? extends Describable>> decorators;
    private final Description delegate;

    private DecoratedDescription(){
        this(new DefaultDescription());
    }

    private DecoratedDescription(Description delegate){
        this(delegate, new HierarchicalClassMap<>(Describable.class));
    }

    private DecoratedDescription(Description delegate, HierarchicalClassMap<Describable, 
                                 BiConsumer<DecoratedDescription, ? extends Describable>> decorators){
        this.decorators = decorators;
        this.delegate = delegate;
    }

    public DecoratedDescription appendDirect(Describable describable){
        this.delegate.append(describable);
        return this;
    }

    @Override
    public DecoratedDescription append(Describable describable) {
        doAppend(describable);
        return this;
    }

    private <D extends Describable> void doAppend(D describable){
        decorators.getForInstance(describable)
                  .map(bic -> (BiConsumer<DecoratedDescription, D>) bic)
                  .orElse(DecoratedDescription::appendDirect)
                  .accept(this, describable);
    }

    @Override
    public DecoratedDescription append(String string) {
        delegate.append(string);
        return this;
    }

    @Override
    public String toString(){
        return this.delegate.toString();
    }

    public static Builder builder(){
        return new Builder();
    }

    public static final class Builder {
        private final HierarchicalClassMap<Describable, BiConsumer<DecoratedDescription, ? extends Describable>> decorators;


        private Builder(){
            this.decorators = new HierarchicalClassMap<>(Describable.class);
        }

        public <D extends Describable> DecorationBuilder<D> intercept(Class<D> type){
            return new DecorationBuilder<>(type);
        }

        public DecoratedDescription newDescription(){
            return new DecoratedDescription(new DefaultDescription(), this.decorators);
        }

        public class DecorationBuilder<D extends Describable> {
            private final Class<D> type;
            private BiConsumer<DecoratedDescription, D> consumer;

            private DecorationBuilder(Class<D> type) {
                this.type = type;
            }

            public Builder thenPrefixWith(String prefix){
                return then((description, d) -> {
                    description.append(prefix);
                    d.describe(description);
                }).thenReturn();
            }

            public Builder thenPostfixWith(String postfix){
                return then((description, d) -> {
                    d.describe(description);
                    description.append(postfix);
                }).thenReturn();
            }

            public Builder thenWrapBetween(String prefix, String postfix){
                return then((description, d) -> {
                    description.append(prefix);
                    d.describe(description);
                    description.append(postfix);
                }).thenReturn();
            }

            public DecorationBuilder<D> thenAppend(String constant){
                return then((description, d) -> description.append(constant));
            }

            public DecorationBuilder<D> thenAppend(Function<D, String> dependent){
                return then((description, d) -> description.append(dependent.apply(d)));
            }

            public DecorationBuilder<D> thenDescribe(Function<D, Describable> dependent){
                return then((description, d) -> description.append(dependent.apply(d)));
            }

            public DecorationBuilder<D> thenAppendOriginal(){
                return then((description, d) -> d.describe(description));
            }

            public DecorationBuilder<D> thenAppendDirect(Function<D, Describable> dependent){
                return then((description, d) -> description.appendDirect(dependent.apply(d)));
            }

            public DecorationBuilder<D> then(BiConsumer<? super DecoratedDescription, ? super D> consumer){
                this.consumer = (this.consumer == null)?  (BiConsumer<DecoratedDescription, D>) consumer : this.consumer.andThen(consumer);
                return this;
            }

            public Builder thenReturn(){
                decorators.put(this.type, this.consumer);
                return Builder.this;
            }
        }

    }



}
