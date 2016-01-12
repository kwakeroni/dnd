package active.model.fight;

import active.model.fight.event.FightEventStream;

/**
 * @author Maarten Van Puymbroeck
 */
public interface FightController {

    public Fight getState();

    /**
     * Advances to the next turn in the fight.
     * If the current actor is the last in the round, a new round is started.
     */
    public void nextTurn();

    public Round startRound();

    public void endRound();

    /**
     * Starts the turn of the next actor.
     * @return The new turn.
     */
    public Turn startTurn();

    /**
     * Ends the turn of the current actor.
     */
    public void endTurn();

    public FightEventStream onEvent();

}
