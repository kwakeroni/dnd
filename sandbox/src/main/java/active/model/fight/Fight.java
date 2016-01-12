package active.model.fight;

import active.model.cat.Actor;
import active.model.cat.Hittable;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author Maarten Van Puymbroeck
 */
public interface Fight {

    public void add(Participant participant);

    public <AP extends Participant & Actor> Stream<AP> getActors();

    public  <HP extends Participant & Hittable> Stream<HP> getTargets();

    public Optional<? extends Round> getCurrentRound();

    public int getLastRoundNumber();

    public default String getCurrentTurnNumber(){
        return getLastRoundNumber() + (getCurrentRound().map(round -> "." + round.getLastTurnNumber()).orElse(""));
    }


    public default <AP extends Participant & Actor> Optional<AP> getCurrentActor(){
        return getCurrentRound().flatMap(Round::getCurrentActor);
    }

}
