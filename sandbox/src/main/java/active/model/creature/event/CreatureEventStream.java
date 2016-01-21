package active.model.creature.event;

import active.engine.channel.Channel;
import active.model.creature.stats.Statistic;
import active.model.event.EventStream;

/**
 * @author Maarten Van Puymbroeck
 */
public interface CreatureEventStream extends EventStream {

    public default Channel<StatChanged<?>> statChanged(){
        return ofType(StatChanged.class)
                .map(sc -> (StatChanged<?>) sc);
    }

    public default <S> Channel<StatChanged<S>> statChanged(Statistic<S> stat){
        return ofType(StatChanged.class)
                .filter(sc -> stat.equals(sc.getStat()))
                .map(sc -> (StatChanged<S>) sc);
    }

}
