package be.kwakeroni.dnd.model.creature.stats;

import be.kwakeroni.dnd.type.value.Score;

import java.util.function.Function;

/**
 * @author Maarten Van Puymbroeck
 */
public final class Base {

    static {
        initialized();
    }

    static Class<Base> initialized(){
        Statistics.addAll(Stat.class);
        return Base.class;
    }

    @SuppressWarnings("unchecked")
    public static Statistic<String> NAME = Stat.NAME;
    @SuppressWarnings("unchecked")
    public static Statistic<Score> HP = Stat.HP;
    @SuppressWarnings("unchecked")
    public static Statistic<Score> MAX_HP = Stat.MAX_HP;

    private Base(){

    }

    @SuppressWarnings("rawtypes")
    enum Stat implements Statistic {
        NAME("", Function.identity()),
        HP(Score.ZERO, Score::fromString),
        MAX_HP(Score.ZERO, Score::fromString);

        private final Object baseline;
        private final Function<String, ?>  fromString;

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
