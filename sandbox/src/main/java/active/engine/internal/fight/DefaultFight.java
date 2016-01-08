package active.engine.internal.fight;

import active.model.fight.Fight;
import active.model.fight.Participant;
import active.model.fight.Round;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Stream;

/**
 * @author Maarten Van Puymbroeck
 */
public class DefaultFight implements Fight {

    private ActorSet actors = new ActorSet();
    private Collection<Participant> targets = new TreeSet<>(Comparator.comparing(Participant::getName));
    private DefaultRound currentRound;
    private int roundCount;

    @Override
    public void add(Participant participant) {
        if (participant.isActor()){
            this.actors.add(participant);
        }

        if (participant.isTarget()){
            this.targets.add(participant);
        }
    }

    @Override
    public Stream<Participant> getActors() {
        return this.actors.stream();
    }

    @Override
    public Stream<Participant> getTargets() {
        return this.targets.stream();
    }

    @Override
    public Optional<DefaultRound> getCurrentRound() {
        return Optional.ofNullable(this.currentRound);
    }

    @Override
    public int getLastRoundNumber() {
        return roundCount;
    }

    public DefaultRound startRound() {
        if (this.currentRound != null){
            throw new IllegalStateException("Previous round unfinished");
        }
        this.currentRound = new DefaultRound(getActors());
        roundCount++;
        return this.currentRound;
    }

    public void endRound() {
        if (this.currentRound == null){
            throw new IllegalStateException("No round active");
        }
        this.currentRound = null;
    }

}
