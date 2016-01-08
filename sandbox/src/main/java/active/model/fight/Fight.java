package active.model.fight;

import active.model.action.Actor;
import active.model.action.Hittable;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author Maarten Van Puymbroeck
 */
public interface Fight {

    public void add(Participant participant);

    public Stream<Participant> getActors();

    public Stream<Participant> getTargets();

    public Optional<? extends Round> getCurrentRound();

    public int getLastRoundNumber();

    public default String getCurrentTurnNumber(){
        return getLastRoundNumber() + (getCurrentRound().map(round -> "." + round.getLastTurnNumber()).orElse(""));
    }


    public default Optional<Participant> getCurrentActor(){
        return getCurrentRound().flatMap(Round::getCurrentActor);
    }

}
