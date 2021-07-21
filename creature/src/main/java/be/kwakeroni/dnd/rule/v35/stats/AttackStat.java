package be.kwakeroni.dnd.rule.v35.stats;

import be.kwakeroni.dnd.model.creature.stats.Statistic;
import be.kwakeroni.dnd.type.value.Modifier;
import be.kwakeroni.dnd.type.value.Score;

import java.util.function.Function;

public final class AttackStat {
    @SuppressWarnings("unchecked")
    public static final Statistic<Score> BASE_ATTACK_BONUS = Stat.BASE_ATTACK_BONUS;
    @SuppressWarnings("unchecked")
    public static final Statistic<Score> ATTACK_BONUS = Stat.ATTACK_BONUS;

    @SuppressWarnings("rawtypes")
    private enum Stat implements Statistic {
        BASE_ATTACK_BONUS(Score.ZERO, Score::fromString),
        ATTACK_BONUS(Score.ZERO, Score::fromString);

        private final Object baseline;
        private final Function<String, ?> fromString;

        <S> Stat(S baseline, Function<String, S> fromString) {
            this.baseline = baseline;
            this.fromString = fromString;
        }


        @Override
        public Object baseline() {
            return baseline;
        }

        @Override
        public Object fromString(String value) {
            return fromString.apply(value);
        }
    }
}
