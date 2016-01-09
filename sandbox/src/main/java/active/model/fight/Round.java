package active.model.fight;

import java.util.Optional;

/**
 * @author Maarten Van Puymbroeck
 */
public interface Round {

    /**
     * Indicates if this Round is finished, i.e. if the last turn was ended.
     * @return {@code true} if this Round is finished
     */
    public boolean isFinished();

    /**
     * @return The currently active turn.
     */
    public Optional<Turn> getCurrentTurn();

    /**
     * @return The sequence number of the current turn, of the previous turn if no turn is active, or 0 in case of a new round.
     */
    public int getLastTurnNumber();

    /**
     * @return The currently active actor.
     */
    public default  <AP extends Participant & IsActor> Optional<AP> getCurrentActor() {
        return getCurrentTurn().map(Turn::getActor);
    }

}
