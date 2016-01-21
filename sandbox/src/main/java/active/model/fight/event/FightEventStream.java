package active.model.fight.event;

import active.engine.channel.Channel;
import active.model.event.EventStream;

/**
 * @author Maarten Van Puymbroeck
 */
public interface FightEventStream extends EventStream {

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

    public default Channel<ActionExecuted> action() {
        return ofType(ActionExecuted.class);
    }

    public default Channel<ParticipantsChanged> participantsChanged(){
        return ofType(ParticipantsChanged.class);
    }

}
