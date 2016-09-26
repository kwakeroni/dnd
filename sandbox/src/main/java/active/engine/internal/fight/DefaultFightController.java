package active.engine.internal.fight;

import java.util.function.Supplier;

import active.engine.channel.ChannelAdapter;
import active.engine.event.EventBroker;
import active.engine.event.EventBrokerSupport;
import active.model.action.Action;
import active.model.creature.Creature;
import active.model.creature.Party;
import active.model.die.Roll;
import active.model.event.Event;
import active.model.fight.Fight;
import active.model.fight.FightController;
import active.model.fight.Participant;
import active.model.fight.Turn;
import active.model.fight.event.ActionExecuted;
import active.model.fight.event.FightAware;
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

    private void

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
        if (! fight.getCurrentRound().isPresent()){
            fight.startRound();
        }
        return fight.getCurrentRound().get().startTurn();
    }

    @Override
    public void endTurn() {
        fight.getCurrentRound().get().endTurn();
    }

    public void nextTurn(){

        DefaultRound round = fight.getCurrentRound().orElseGet(() -> fight.startRound());

        if (round.getCurrentTurn().isPresent()){
            round.endTurn();
        }

        if (round.isFinished()){
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
