package be.kwakeroni.dnd.engine.model.fight;

import be.kwakeroni.dnd.event.EventBroker;
import be.kwakeroni.dnd.model.actor.Action;
import be.kwakeroni.dnd.model.creature.Party;
import be.kwakeroni.dnd.model.fight.Fight;
import be.kwakeroni.dnd.engine.api.FightController;
import be.kwakeroni.dnd.model.fight.Participant;
import be.kwakeroni.dnd.model.fight.Turn;
import be.kwakeroni.dnd.model.fight.event.ActionExecuted;
import be.kwakeroni.dnd.model.fight.event.FightEventStream;

/**
 * @author Maarten Van Puymbroeck
 */
public class DefaultFightController implements FightController {

    private final DefaultFight fight;
    private final EventBroker<FightEventStream> broker;


    public DefaultFightController(DefaultFight fight, EventBroker<FightEventStream> broker) {

        this.fight = fight;
        this.broker = broker;
    }

    @Override
    public void addParty(Party party) {
        party.members()
                .map(DefaultParticipant::ofCreature)
                .forEach(this::addParticipant);
    }


    @Override
    public void addParticipant(Participant p) {
        this.fight.add(p);
    }

    @Override
    public Fight getState() {
        return fight;
    }

    @Override
    public DefaultRound startRound() {
        return fight.startRound();
    }

    @Override
    public void endRound() {
        fight.endRound();
    }

    @Override
    public Turn startTurn() {
        return fight.getCurrentRound()
                .orElseGet(fight::startRound)
                .startTurn();
    }

    @Override
    public void endTurn() {
        fight.getCurrentRound().ifPresent(round -> {
            round.endTurn();

            if (round.isFinished()) {
                endRound();
            }
        });
    }

    public void nextTurn() {

        DefaultRound round = fight.getCurrentRound().orElseGet(fight::startRound);

        if (round.getCurrentTurn().isPresent()) {
            round.endTurn();
        }

        if (round.isFinished()) {
            endRound();
            round = startRound();
        }

        round.startTurn();

    }

    @Override
    public FightEventStream on() {
        return this.broker.on();
    }

    @Override
    public void execute(Action<? super Fight> action) {
        action.execute(this.fight);
        this.broker.fire(new ActionExecuted(action));
    }
}
