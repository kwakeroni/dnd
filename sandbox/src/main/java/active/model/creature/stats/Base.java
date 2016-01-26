package active.model.creature.stats;

import active.model.value.Score;

/**
 * @author Maarten Van Puymbroeck
 */
public final class Base {

    public static Statistic<String> NAME = RawBase.NAME;
    public static Statistic<Score> HP = RawBase.HP;
    public static Statistic<Score> MAX_HP = RawBase.MAX_HP;

    private Base(){

    }

    @SuppressWarnings("rawtypes")
    private static enum RawBase implements Statistic {
        NAME,
        HP,
        MAX_HP;
    }

}
