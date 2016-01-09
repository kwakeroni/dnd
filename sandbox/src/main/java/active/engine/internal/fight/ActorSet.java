package active.engine.internal.fight;

import active.engine.util.Streamable;
import active.model.cat.Actor;
import active.model.fight.IsActor;
import active.model.fight.Participant;
import active.model.value.Modifier;
import active.model.value.Score;

import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Comparator.*;

/**
 * @author Maarten Van Puymbroeck
 */
class ActorSet implements Streamable<Participant> {

    private SortedSet<ActorChain> actors = treeset();
    
    private <AP extends Participant & IsActor> TreeSet<ActorChain> treeset(){
        return new TreeSet<ActorChain>(c());
    }
    
    private <AP extends Participant & IsActor> Comparator<ActorChain> c(){
        return Comparator. comparing(         ActorChain::getLeader, 
                Comparator.<AP, Score> comparing(           ActorSet::getRequiredInitiative ).reversed()
                .thenComparing( c(p -> p.actor()), 
                comparing(              Actor::getInitiativeModifier ).reversed()
                .thenComparingInt(     System::identityHashCode)
                ));
    }
    
    private <AP extends Participant & IsActor> Function<AP, ComparableActor> c(Function<AP, Actor> f){
        
        return (Function<AP, ComparableActor>) (Function<?,?>) f;
    }
    private static interface ComparableActor extends Actor, Comparable<Actor> { }

    public <AP extends Participant & IsActor> void add(Participant participant){
        this.actors.add(new ActorChain((AP) (IsActor) participant));
    }

    public Stream<Participant> stream(){
        return this.actors.stream()
                          .flatMap( chain -> chain.getParticipants());
    }

    private static class ActorChain {

        private ParticipantChain chain;

        public <AP extends Participant & IsActor> ActorChain(AP actor){
            this.chain = new SingletonParticipantChain(actor);
        }

        public <AP extends Participant & IsActor> void addFollower(AP parent, AP follower){
            this.chain = this.chain.addFollower(parent, follower);
        }

        public boolean contains(Participant participant){
            return this.chain.contains(participant);
        }

        public <AP extends Participant & IsActor> Stream<AP> getParticipants(){
            return (Stream<AP>) this.chain.getParticipants();
        }

        public <AP extends Participant & IsActor> AP getLeader(){
            return (AP) this.chain.getLeader();
        }

    }

    private static Score getRequiredInitiative(Participant p){
        return p.getInitiative()
                .orElseThrow(() ->
                    new IllegalArgumentException(String.format("Actor %s has no defined initiative", p.getName())));
    }

}
