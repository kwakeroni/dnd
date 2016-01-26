package active.model.creature.event;

import active.model.creature.stats.Statistic;

/**
 * @author Maarten Van Puymbroeck
 */
public class StatChanged<S> extends CreatureEvent {

    private final Statistic<S> stat;
    private final S oldValue;
    private final S newValue;

    public StatChanged(Statistic<S> stat, S oldValue, S newValue) {
        this.newValue = newValue;
        this.stat = stat;
        this.oldValue = oldValue;
    }

    public S getNewValue() {
        return newValue;
    }

    public S getOldValue() {
        return oldValue;
    }

    public Statistic<S> getStat() {
        return stat;
    }
}
