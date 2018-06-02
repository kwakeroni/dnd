package be.kwakeroni.dnd.engine.model.fight;

import be.kwakeroni.dnd.event.EventBroker;
import be.kwakeroni.dnd.io.model.MutableFight;
import be.kwakeroni.dnd.type.collection.Streamable;
import be.kwakeroni.dnd.model.actor.Actor;
import be.kwakeroni.dnd.model.target.Hittable;
import be.kwakeroni.dnd.model.fight.Fight;
import be.kwakeroni.dnd.model.fight.Participant;
import be.kwakeroni.dnd.model.fight.Round;
import be.kwakeroni.dnd.model.fight.event.FightEventStream;
import be.kwakeroni.dnd.model.fight.event.ParticipantsChanged;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Stream;

/**
 * @author Maarten Van Puymbroeck
 */
public final class DefaultFight implements Fight, MutableFight {

    private ActorSet<?> actors = new ActorSet<>();
    private Collection<Participant> targets = new TreeSet<>(Comparator.comparing(Participant::getName));
    private Map<String, Participant> byName = new HashMap<>();
    private DefaultRound currentRound;
    private int roundCount;
    private Optional<? extends EventBroker<FightEventStream>> broker;
    
    public DefaultFight(){
        this(Optional.empty());
    }
    
    public DefaultFight(EventBroker<FightEventStream> broker){
        this(Optional.of(broker));
    }
    
    private DefaultFight(Optional<? extends EventBroker<FightEventStream>> broker){
        this.broker = broker;
    }

    public void setBroker(EventBroker<FightEventStream> newBroker){
        broker.ifPresent(old -> { throw new IllegalStateException("Broker already present"); });
        this.broker = Optional.of(newBroker);
    }

    public void add(Participant participant) {

        if (! participant.isActor() && ! participant.isTarget()){
            throw new IllegalArgumentException("Participant is neither an actor nor a target: " + participant);
        }

        if (participant.isActor()){
            this.actors.add(participant.asActor().get());
        }

        if (participant.isTarget()){
            this.targets.add(participant.asTarget().get());
        }

        this.broker.ifPresent(broker -> broker.fire(new ParticipantsChanged()));
    }

    @Override
    public <AP extends Participant & Actor> Stream<AP> getActors() {
        return ((Streamable<AP>) this.actors).stream();
    }

    @Override
    public <AP extends Participant & Actor> Optional<AP> getCurrentActor() {
        return getCurrentRound().flatMap(Round::getCurrentActor);
    }

    @Override
    public <TP extends Participant & Hittable> Stream<TP> getTargets() {
        return ((Collection<TP>) (Collection<? extends Participant>) this.targets).stream();
    }

    @Override
    public Optional<DefaultRound> getCurrentRound() {
        return Optional.ofNullable(this.currentRound);
    }

    @Override
    public int getLastRoundNumber() {
        return roundCount;
    }

    public void setLastRoundNumber(int count){ this.roundCount = count; }

    public DefaultRound startRound() {
        if (this.currentRound != null){
            throw new IllegalStateException("Previous round unfinished");
        }
        this.currentRound = new DefaultRound(getActors(), this.broker);
        roundCount++;
        return this.currentRound;
    }

    public void endRound() {
        if (this.currentRound == null){
            throw new IllegalStateException("No round active");
        }
        this.currentRound = null;
    }

    @Override
    public FightEventStream on() {
        return this.broker.orElseThrow(() -> new IllegalStateException("No Fight Broker")).on();
    }
}
