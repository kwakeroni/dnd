package active.model.fight;

import active.model.cat.Actor;
import active.model.cat.Hittable;
import active.model.fight.event.FightData;
import active.model.fight.event.FightEventStream;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author Maarten Van Puymbroeck
 */
public interface Fight {

    public <AP extends Participant & Actor> Stream<AP> getActors();

    public  <HP extends Participant & Hittable> Stream<HP> getTargets();

    public default Stream<Participant> getParticipants(){
        return Stream.concat( this.getActors(),
                              this.getTargets().filter(p -> ! p.isActor()) );
    }

    public Optional<? extends Round> getCurrentRound();

    public int getLastRoundNumber();

    public default String getCurrentTurnNumber(){
        return getLastRoundNumber() + (getCurrentRound().map(round -> "." + round.getLastTurnNumber()).orElse(""));
    }


    public default <AP extends Participant & Actor> Optional<AP> getCurrentActor(){
        return getCurrentRound().flatMap(Round::getCurrentActor);
    }

    public FightEventStream on();

}
