package be.kwakeroni.dnd.rule;

import be.kwakeroni.dnd.model.creature.stats.Statistic;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

public interface StatsRule {

    default <S> S effect(Statistic<S> stat) {
        return effect(stat, stat.baseline());
    }

    <S> S effect(Statistic<S> stat, S value);

    static <T> StatsRule modify(Statistic<T> statistic, UnaryOperator<T> operator) {
        class Simple implements StatsRule {
            @Override
            public <S> S effect(Statistic<S> stat, S value) {
                if (statistic.equals(stat)) {
                    @SuppressWarnings("unchecked") // S == T
                    S result = (S) operator.apply((T) value);
                    return result;
                } else {
                    return value;
                }
            }
        }

        Objects.requireNonNull(statistic, "statistic");
        Objects.requireNonNull(operator, "operator");
        return new Simple();
    }

    static StatsRule aggregate(StatsRule... rules) {
        return aggregate0(List.of(rules));
    }

    static StatsRule aggregate(Collection<? extends StatsRule> rules) {
        return aggregate0(List.copyOf(rules));
    }

    private static StatsRule aggregate0(List<StatsRule> trustedRules) {
        class Aggregate implements StatsRule {
            @Override
            public <S> S effect(Statistic<S> stat, S value) {
                for (StatsRule rule : trustedRules) {
                    value = rule.effect(stat, value);
                }
                return value;
            }
        }
        return new Aggregate();
    }
}
