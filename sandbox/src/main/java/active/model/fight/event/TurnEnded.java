package active.model.fight.event;

import active.model.event.Event;
import active.model.fight.Fight;
import active.model.fight.Round;
import active.model.fight.Turn;

/**
 * @author Maarten Van Puymbroeck
 */
public class TurnEnded extends FightEvent {

    private final Round round;
    private final Turn turn;

    public TurnEnded(Round round, Turn turn) {
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
