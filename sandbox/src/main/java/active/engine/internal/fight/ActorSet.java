package active.engine.internal.fight;

import active.model.action.Actor;
import active.model.fight.Participant;
import active.model.value.Modifier;
import active.model.value.Score;

import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;
import static java.util.Comparator.*;

/**
 * @author Maarten Van Puymbroeck
 */
class ActorSet {

    private SortedSet<ActorChain> actors = new TreeSet<>(
                comparing(ActorChain::getLeader,
                comparing(ActorSet::getRequiredInitiative).reversed()
           .thenComparing(Participant::asActor,
                comparing(Actor::getInitiativeModifier).reversed()
        .thenComparingInt(System::identityHashCode)
                )));

    public void add(Participant participant){
        this.actors.add(new ActorChain(participant));
    }

    public Stream<Participant> stream(){
        return this.actors.stream()
                          .flatMap( chain -> chain.getParticipants());
    }

    private static class ActorChain {

        private ParticipantChain chain;

        public ActorChain(Participant actor){
            this.chain = new SingletonParticipantChain(actor);
        }

        public void addFollower(Participant parent, Participant follower){
            this.chain = this.chain.addFollower(parent, follower);
        }

        public boolean contains(Participant participant){
            return this.chain.contains(participant);
        }

        public Stream<Participant> getParticipants(){
            return this.chain.getParticipants();
        }

        public Participant getLeader(){
            return this.chain.getLeader();
        }

    }

    private static Score getRequiredInitiative(Participant p){
        return p.getInitiative()
                .orElseThrow(() ->
                    new IllegalArgumentException(String.format("Actor %s has no defined initiative", p.getName())));
    }

}
