package active.engine.internal.fight;

import active.model.fight.Fight;
import active.model.fight.Participant;

import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;

/**
 * @author Maarten Van Puymbroeck
 */
public class DefaultFight implements Fight {

    private ActorSet actors = new ActorSet();
    private Collection<Participant> targets = new TreeSet<>(Comparator.comparing(Participant::getName));


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
}
