package active.engine.gui.business;

import active.model.creature.event.StatChanged;
import active.model.creature.stats.Statistic;
import active.model.fight.Participant;

import javax.swing.*;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public interface ParticipantData {

    default void initBehaviour(Participant participant){
        participant.on()
                .statChanged()
                .forEach(sc -> update(sc));

        setStatUpdateListener(participant::setStat);
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

    void setStatUpdateListener(StatUpdateListener listener);

    interface StatUpdateListener {
        <S> void onStatUpdate(Statistic<S> stat, S value);
    }
}
