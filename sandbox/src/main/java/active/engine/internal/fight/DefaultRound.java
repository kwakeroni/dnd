package active.engine.internal.fight;

import active.engine.event.EventBrokerSupport;
import active.model.cat.Actor;
import active.model.fight.Participant;
import active.model.fight.Round;
import active.model.fight.Turn;
import active.model.fight.event.TurnEnded;
import active.model.fight.event.TurnStarted;

import java.util.ListIterator;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Maarten Van Puymbroeck
 */
public final class DefaultRound implements Round {

    private final ListIterator<? extends ActorTurn<?>> turns;

    /**
     * @invar currentTurn == null || currentTurn.turn != null
     */
    private ActorTurn<?> currentTurn;

    private final Optional<EventBrokerSupport> broker;

    public <AP extends Participant & Actor> DefaultRound(Stream<AP> actors){
        this(actors, Optional.empty());
    }
    public <AP extends Participant & Actor> DefaultRound(Stream<AP> actors, EventBrokerSupport broker){
        this(actors, Optional.of(broker));
    }

    private <AP extends Participant & Actor> DefaultRound(Stream<AP> actors, Optional<EventBrokerSupport> broker){
        this.turns =
            actors.map(actor -> new ActorTurn<AP>(actor))
                  .collect(Collectors.toList())
                  .listIterator();
        this.broker = broker;
    }



    @Override
    public boolean isFinished() {
        return (! turns.hasNext()) && currentTurn == null  ;
    }

    @Override
    public Optional<Turn> getCurrentTurn() {
        return (currentTurn == null)? Optional.empty() : Optional.of(currentTurn.turn);
    }

    @Override
    public int getLastTurnNumber() {
        if (turns.hasNext()){
            return turns.nextIndex();
        } else {
            return turns.nextIndex();
        }
    }

    public Turn startTurn() {
        if (this.currentTurn != null){
            throw new IllegalStateException("Turn already active");
        }
        ActorTurn<?> next = turns.next();
        next.start();
        this.currentTurn = next;

        this.broker.ifPresent(broker -> broker.fire(new TurnStarted(this, this.currentTurn.turn)));

        return this.currentTurn.turn;
    }

    public void endTurn() {
        if (this.currentTurn == null){
            throw new IllegalStateException("No active turn");
        }
        Turn turn = this.currentTurn.turn;
        this.currentTurn.end();
        this.currentTurn = null;

        this.broker.ifPresent(broker -> broker.fire(new TurnEnded(this, this.currentTurn.turn)));
    }

    private static /* value */ class ActorTurn<AP extends Participant & Actor> {
        final AP actor;
        Turn turn;

        public ActorTurn(AP actor) {
            this.actor = actor;
        }

        public Turn start() {
            if (this.turn != null){
                throw new IllegalStateException("Turn already started");
            }
            this.turn = new DefaultTurn(this.actor);
            return this.turn;
        }

        public void end() {
            if (this.turn == null){
                throw new IllegalStateException("Turn not yet started");
            }
        }

    }

}
