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

    public <AP extends Participant & IsActor> Stream<AP> getActors();

    public <TP extends Participant & IsTarget> Stream<TP> getTargets();

    public Optional<? extends Round> getCurrentRound();

    public int getLastRoundNumber();

    public default String getCurrentTurnNumber(){
        return getLastRoundNumber() + (getCurrentRound().map(round -> "." + round.getLastTurnNumber()).orElse(""));
    }


    public <AP extends Participant & IsActor> Optional<AP> getCurrentActor();

}
