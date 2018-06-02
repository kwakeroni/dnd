package be.kwakeroni.dnd.model.creature.event;

import be.kwakeroni.dnd.model.creature.stats.Statistic;

/**
 * @author Maarten Van Puymbroeck
 */
public class StatChanged<S> extends CreatureEvent {

    @SuppressWarnings("unchecked")
    public static final Class<StatChanged<?>> genclass = (Class<StatChanged<?>>) (Class<?>) StatChanged.class;

    @SuppressWarnings("unchecked")
    public static final <S> Class<StatChanged<S>> genclass(){
        return (Class<StatChanged<S>>) (Class<?>) StatChanged.class;
    }

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
