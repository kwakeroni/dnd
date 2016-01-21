package active.model.creature;

import active.model.cat.Actor;
import active.model.cat.Hittable;
import active.model.cat.Named;
import active.model.cat.Observable;
import active.model.creature.event.CreatureEventStream;
import active.model.creature.stats.Statistic;
import active.model.value.Modifier;
import active.model.value.Score;

/**
 * @author Maarten Van Puymbroeck
 */
public interface Creature extends Actor, Hittable, Named, Observable<CreatureEventStream> {

    <S> S get(Statistic<S> stat);

}
