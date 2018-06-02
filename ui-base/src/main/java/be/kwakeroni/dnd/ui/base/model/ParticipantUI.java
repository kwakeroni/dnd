package be.kwakeroni.dnd.ui.base.model;

import be.kwakeroni.dnd.model.creature.event.StatChanged;
import be.kwakeroni.dnd.model.creature.stats.Statistic;
import be.kwakeroni.dnd.model.fight.Participant;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public interface ParticipantUI {

    default void initBehaviour(Participant participant){
        participant.on()
                .statChanged()
                .forEach(sc -> update(sc));
    }

    default <S> void update(StatChanged<S> event){
        Statistic<S> stat = event.getStat();
        if (has(stat)) {
            S oldValue = event.getOldValue();
            S newValue = event.getNewValue();
            update(stat, oldValue, newValue);
        }
    }

    boolean has(Statistic<?> stat);

    <S> void update(Statistic<S> stat, S oldValue, S newValue);
}
