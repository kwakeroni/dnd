package active.model.creature.stats;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class Statistics {

    private static Map<String, Statistic<?>> STATS = new HashMap<>();

    private static <S extends Statistic<?>> void addAll(Function<S, String> name, S... stats){
        for (S stat : stats){
            STATS.put(name.apply(stat), stat);
        }
    }

    private static <S extends Statistic<?>> void addAll(Function<S, String> name, Collection<S> stats){
        for (S stat : stats){
            STATS.put(name.apply(stat), stat);
        }
    }

    public static <S extends Enum<S> & Statistic<?>> void addAll(Class<S> statisticType){
        addAll(S::name, statisticType.getEnumConstants());
    }

    public static String toString(Statistic<?> stat){
        return stat.toString();
    }

    public static Statistic<?> fromString(String name){
        return STATS.get(name);
    }


    private static final Class<?>[] KNOWN_STATS = {Base.initialized(), Mod.initialized(), Prop.initialized()};

}
