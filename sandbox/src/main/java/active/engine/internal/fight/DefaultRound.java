package active.engine.internal.fight;

import active.model.action.Actor;
import active.model.fight.Participant;
import active.model.fight.Round;
import active.model.fight.Turn;

import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Maarten Van Puymbroeck
 */
public class DefaultRound implements Round {

    private final ListIterator<ActorTurn> turns;
    /**
     * @invar currentTurn == null || currentTurn.turn != null
     */
    private ActorTurn currentTurn;

    public DefaultRound(Stream<Participant> actors){
        this.turns =
            actors.map(actor -> new ActorTurn(actor))
                  .collect(Collectors.toList())
                  .listIterator();
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
        ActorTurn next = turns.next();
        next.start();
        this.currentTurn = next;
        return this.currentTurn.turn;
    }

    public void endTurn() {
        if (this.currentTurn == null){
            throw new IllegalStateException("No active turn");
        }
        this.currentTurn.end();
        this.currentTurn = null;

    }

    private static /* value */ class ActorTurn {
        final Participant actor;
        Turn turn;

        public ActorTurn(Participant actor) {
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
