package active.model.creature.stats;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class StatisticEntry<S> {

    private Statistic<S> stat;
    private S value;

    public StatisticEntry(){

    }

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

    public void setStat(Statistic<S> stat) {
        this.stat = stat;
    }

    public void setValue(S value) {
        this.value = value;
    }

    public void setStringValue(String value){
        setValue(this.stat.fromString(value));
    }
}
