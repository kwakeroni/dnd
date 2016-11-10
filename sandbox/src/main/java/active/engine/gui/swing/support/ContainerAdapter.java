package active.engine.gui.swing.support;

import active.engine.gui.swing.Snapshot;

import java.awt.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public interface ContainerAdapter extends Iterable<Component> {

//    default void add(ComponentAdapter component){
//        this.add(component.component());
//    }
//
//    void add(Component component);

    Collection<? extends Component> components();

    default Stream<? extends Component> componentStream(){
        return components().stream();
    }

    @Override
    default Iterator<Component> iterator(){
        return (Iterator<Component>) components().iterator();
    }

    default Snapshot modify(Consumer<? super Component> action, Consumer<? super Component> restoreAction){
        return modify(c -> true, (c, b) -> action.accept(c), c -> false, (c, b) -> restoreAction.accept(c));
    }

    default <S> Snapshot modify(Function<? super Component, S> newState,
                            Function<? super Component, S> originalState,
                                BiConsumer<? super Component, S> action){
        return modify(newState, action, originalState, action);
    }

    default <S> Snapshot modify(Function<? super Component, S> newState,
                                BiConsumer<? super Component, S> action,
                                Function<? super Component, S> originalState,
                                BiConsumer<? super Component, S> restoreAction){
        return ContainerAdapterHelper.modify(newState, action, originalState, restoreAction, componentStream());
    }

}
