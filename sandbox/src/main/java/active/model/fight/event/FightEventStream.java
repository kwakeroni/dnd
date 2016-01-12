package active.model.fight.event;

import active.engine.channel.Channel;
import active.engine.event.Event;

/**
 * @author Maarten Van Puymbroeck
 */
public interface FightEventStream extends Channel<Event> {

    public default <E extends Event> Channel<E> ofType(Class<E> type){
        return filter(event -> type.isInstance(event))
                .map( event -> type.cast(event));
    }

    public default Channel<FightStarted> fightStarted(){
        return ofType(FightStarted.class);
    }

    public default Channel<FightEnded> fightEnded(){
        return ofType(FightEnded.class);
    }

    public default Channel<TurnEnded> turnEnded(){
        return ofType(TurnEnded.class);
    }

    public default Channel<TurnStarted> turnStarted(){
        return ofType(TurnStarted.class);
    }

}
