package active.model.creature;

import active.model.action.ActionType;
import active.model.cat.Actor;
import active.model.cat.Hittable;
import active.model.cat.Named;
import active.model.cat.Observable;
import active.model.creature.event.CreatureEventStream;
import active.model.creature.stats.Statistic;
import active.model.creature.stats.StatisticEntry;
import active.model.value.Modifier;
import active.model.value.Score;

import java.util.Map;
import java.util.stream.Stream;

/**
 * @author Maarten Van Puymbroeck
 */
public interface Creature extends Actor, Hittable, Named, Observable<CreatureEventStream> {

    <S> S get(Statistic<S> stat);
    Stream<StatisticEntry<?>> statistics();
    Stream<ActionType> actions();

}
