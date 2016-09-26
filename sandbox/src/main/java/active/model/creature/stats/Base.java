package active.model.creature.stats;

import active.model.value.Score;

import java.util.function.Function;

/**
 * @author Maarten Van Puymbroeck
 */
public final class Base {

    static {
        initialized();
    }

    static Class<Base> initialized(){
        Statistics.addAll(RawBase.class);
        return Base.class;
    }

    public static Statistic<String> NAME = RawBase.NAME;
    public static Statistic<Score> HP = RawBase.HP;
    public static Statistic<Score> MAX_HP = RawBase.MAX_HP;

    private Base(){

    }

    @SuppressWarnings("rawtypes")
    enum RawBase implements Statistic {
        NAME(Function.identity()),
        HP(Score::fromString),
        MAX_HP(Score::fromString);

        private final Function<String, ?>  fromString;

        RawBase(Function<String, ?> fromString) {
            this.fromString = fromString;
        }


        @Override
        public Object fromString(String value) {
            return fromString.apply(value);
        }
    }

}
