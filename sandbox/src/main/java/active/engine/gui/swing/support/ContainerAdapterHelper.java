package active.engine.gui.swing.support;

import active.engine.gui.swing.Snapshot;

import java.awt.*;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
class ContainerAdapterHelper {

    static <S> Snapshot modify(Function<? super Component, S> newStateGetter,
                               BiConsumer<? super Component, S> action,
                               Function<? super Component, S> originalStateGetter,
                               BiConsumer<? super Component, S> restoreAction,
                               Component component){
        S originalState = originalStateGetter.apply(component);

        S newState = newStateGetter.apply(component);

        action.accept(component, newState);

        if (component instanceof Container){
            return modify(c -> newState, action, originalStateGetter, restoreAction, Arrays.stream(((Container) component).getComponents()))
                    .and(() -> restoreAction.accept(component, originalState));
        } else {
            return () -> restoreAction.accept(component, originalState);
        }

    }

    static <S> Snapshot modify(Function<? super Component, S> newStateGetter,
                               BiConsumer<? super Component, S> action,
                               Function<? super Component, S> originalStateGetter,
                               BiConsumer<? super Component, S> restoreAction,
                               Stream<? extends Component> components) {
        return components.map(component -> modify(newStateGetter, action, originalStateGetter, restoreAction, component))
                .reduce(() -> {}, Snapshot::and);
    }
}
