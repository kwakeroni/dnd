package be.kwakeroni.dnd.model.creature.stats;

import java.util.StringJoiner;
import java.util.function.Function;

/**
 * @author Maarten Van Puymbroeck
 */
public interface Statistic<S> {

    S baseline();

    S fromString(String value);

    static <S> Statistic<S> of(String name, S baseline, Function<String, S> fromString) {
        return new Statistic<S>() {
            @Override
            public S baseline() {
                return baseline;
            }

            @Override
            public S fromString(String value) {
                return fromString.apply(value);
            }

            @Override
            public String toString() {
                return name;
            }
        };
    }
}
