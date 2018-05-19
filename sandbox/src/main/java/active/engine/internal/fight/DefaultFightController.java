package active.engine.internal.fight;

import active.engine.event.EventBroker;
import active.model.action.Action;
import active.model.creature.Party;
import active.model.fight.Fight;
import active.model.fight.FightController;
import active.model.fight.Participant;
import active.model.fight.Turn;
import active.model.fight.event.ActionExecuted;
import active.model.fight.event.FightEventStream;

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
