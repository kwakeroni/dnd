package active.engine.gui.swing;

import static active.engine.gui.swing.ContainerAdapterHelper.*;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public interface ContainerAdapter {

    Stream<? extends Component> components();

    default Snapshot setEnabled(Predicate<? super Component> predicate){
        return ContainerAdapterHelper.setEnabled(predicate, components());
    }

    default Snapshot modify(Consumer<? super Component> action, Consumer<? super Component> restoreAction){
        return modify(c -> true, (c, b) -> action.accept(c), c -> false, (c, b) -> restoreAction.accept(c));
    }

    default <S> Snapshot modify(Function<? super Component, S> newState,
                                BiConsumer<? super Component, S> action,
                                Function<? super Component, S> originalState,
                                BiConsumer<? super Component, S> restoreAction){
        return ContainerAdapterHelper.modify(newState, action, originalState, restoreAction, components());
    }

}
