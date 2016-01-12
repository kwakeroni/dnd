package active.model.fight.event;

import active.engine.event.Event;
import active.model.fight.Fight;
import active.model.fight.Round;
import active.model.fight.Turn;

/**
 * @author Maarten Van Puymbroeck
 */
public class TurnStarted extends FightEvent {

    private final Round round;
    private final Turn turn;

    public TurnStarted(Round round, Turn turn) {
        super();
        this.round = round;
        this.turn = turn;
    }

    public Round getRound() {
        return round;
    }

    public Turn getTurn() {
        return turn;
    }
}
