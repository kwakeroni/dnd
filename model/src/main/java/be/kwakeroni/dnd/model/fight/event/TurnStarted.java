package be.kwakeroni.dnd.model.fight.event;

import be.kwakeroni.dnd.event.Event;
import be.kwakeroni.dnd.model.fight.Round;
import be.kwakeroni.dnd.model.fight.Turn;

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
