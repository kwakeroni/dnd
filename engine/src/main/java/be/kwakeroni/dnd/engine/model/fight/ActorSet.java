package be.kwakeroni.dnd.engine.model.fight;

import be.kwakeroni.dnd.type.collection.Streamable;
import be.kwakeroni.dnd.model.actor.Actor;
import be.kwakeroni.dnd.model.fight.Participant;
import be.kwakeroni.dnd.type.value.Score;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author Maarten Van Puymbroeck
 */
class ActorSet <ActPar extends Participant & Actor> implements Streamable<ActPar> {

    private SortedSet<ActorChain> actors = treeset();
    
    private <AP extends Participant & Actor> TreeSet<ActorChain> treeset(){
        return new TreeSet<ActorChain>(c());
    }
    
    private <AP extends Participant & Actor> Comparator<ActorChain> c(){
        return Comparator.<ActorChain, AP> comparing(ActorChain::getLeader,
               Comparator.<AP, Score>      comparing(ActorSet::getRequiredInitiative)
                                      .thenComparing(a -> a.getInitiativeModifier()).reversed()
                                      .thenComparingInt(System::identityHashCode)
        );
    }
    
    private <AP extends Participant & Actor> Function<AP, ComparableActor> c(Function<AP, Actor> f){
        
        return (Function<AP, ComparableActor>) (Function<?,?>) f;
    }
    private static interface ComparableActor extends Actor, Comparable<Actor> { }

    public <AP extends Participant & Actor> void add(AP participant){
        this.actors.add(new ActorChain(participant));
    }

    public Stream<ActPar> stream(){
        return this.actors.stream()
                          .flatMap( chain -> chain.getParticipants());
    }

    private static class ActorChain {

        private ParticipantChain<?> chain;

        public <AP extends Participant & Actor> ActorChain(AP actor){
            this.chain = new SingletonParticipantChain<>(actor);
        }

        public <AP extends Participant & Actor> void addFollower(AP parent, AP follower){
            this.chain = ((ParticipantChain<AP>) this.chain).addFollower(parent, follower);
        }

        public boolean contains(Participant participant){
            return this.chain.contains(participant);
        }

        public <AP extends Participant & Actor> Stream<AP> getParticipants(){
            return (Stream<AP>) this.chain.getParticipants();
        }

        public <AP extends Participant & Actor> AP getLeader(){
            return (AP) this.chain.getLeader();
        }

    }

    private static Score getRequiredInitiative(Participant p){
        return p.getInitiative()
                .orElseThrow(() ->
                    new IllegalArgumentException(String.format("Actor %s has no defined initiative", p.getName())));
    }

}
