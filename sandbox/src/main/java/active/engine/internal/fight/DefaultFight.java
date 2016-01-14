package active.engine.internal.fight;

import active.engine.event.EventBroker;
import active.engine.util.Streamable;
import active.model.cat.Actor;
import active.model.cat.Hittable;
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
public final class DefaultFight implements Fight {

    private ActorSet<?> actors = new ActorSet<>();
    private Collection<Participant> targets = new TreeSet<>(Comparator.comparing(Participant::getName));
    private DefaultRound currentRound;
    private int roundCount;
    private Optional<EventBroker<?>> broker;
    
    public DefaultFight(){
        this(Optional.empty());
    }
    
    public DefaultFight(EventBroker<?> broker){
        this(Optional.of(broker));
    }
    
    private DefaultFight(Optional<EventBroker<?>> broker){
        this.broker = broker;
    }

    public void setBroker(EventBroker<?> newBroker){
        broker.ifPresent(old -> { throw new IllegalStateException("Broker already present"); });
        this.broker = Optional.of(newBroker);
    }

    @Override
    public void add(Participant participant) {
        if (participant.isActor()){
            this.actors.add(participant.asActor().get());
        }

        if (participant.isTarget()){
            this.targets.add(participant.asTarget().get());
        }
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

}
