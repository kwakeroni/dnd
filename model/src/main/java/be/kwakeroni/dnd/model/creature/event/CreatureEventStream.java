package be.kwakeroni.dnd.model.creature.event;

import be.kwakeroni.dnd.model.creature.stats.Statistic;
import be.kwakeroni.dnd.event.Channel;
import be.kwakeroni.dnd.event.EventStream;

/**
 * @author Maarten Van Puymbroeck
 */
public interface CreatureEventStream extends EventStream {

    public default Channel<StatChanged<?>> statChanged(){
        return ofType(StatChanged.genclass);
    }

    public default <S> Channel<StatChanged<S>> statChanged(Statistic<S> stat){
        return ofType(StatChanged.<S> genclass())
                .filter(sc -> stat.equals(sc.getStat()));
    }

}
