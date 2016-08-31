package active.model.creature.stats;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class StatisticEntry<S> {

    private final Statistic<S> stat;
    private final S value;

    public StatisticEntry(Statistic<S> stat, S value) {
        this.stat = stat;
        this.value = value;
    }

    public Statistic<S> getStat() {
        return stat;
    }

    public S getValue() {
        return value;
    }
}
