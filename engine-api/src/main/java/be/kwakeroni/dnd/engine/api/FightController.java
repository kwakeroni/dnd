package be.kwakeroni.dnd.engine.api;

import be.kwakeroni.dnd.model.actor.Action;
import be.kwakeroni.dnd.model.creature.Party;
import be.kwakeroni.dnd.model.fight.Fight;
import be.kwakeroni.dnd.model.fight.Participant;
import be.kwakeroni.dnd.model.fight.Round;
import be.kwakeroni.dnd.model.fight.Turn;
import be.kwakeroni.dnd.model.fight.event.FightData;
import be.kwakeroni.dnd.model.fight.event.FightEventStream;

/**
 * @author Maarten Van Puymbroeck
 */
public interface FightController {

    public Fight getState();

    public void addParty(Party party);

    public void addParticipant(Participant p);

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

    public FightEventStream on();

    public void execute(Action<? super Fight> action);

    public default FightData getData(){
        return new FightData(this.getState());
    }


}
