package be.kwakeroni.dnd.model.creature;

import be.kwakeroni.dnd.model.actor.ActionType;
import be.kwakeroni.dnd.model.actor.Actor;
import be.kwakeroni.dnd.model.creature.event.CreatureEventStream;
import be.kwakeroni.dnd.model.creature.stats.Statistic;
import be.kwakeroni.dnd.model.creature.stats.StatisticEntry;
import be.kwakeroni.dnd.model.target.Hittable;
import be.kwakeroni.dnd.type.base.Named;

import java.util.stream.Stream;

/**
 * @author Maarten Van Puymbroeck
 */
public interface Creature extends Actor, Hittable, Named {

    <S> S get(Statistic<S> stat);

    Stream<StatisticEntry<?>> statistics();

    Stream<ActionType> actions();

    public CreatureEventStream on();
}
