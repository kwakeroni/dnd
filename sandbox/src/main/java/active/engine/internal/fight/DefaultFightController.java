package active.engine.internal.fight;

import active.engine.channel.ChannelAdapter;
import active.engine.event.Event;
import active.engine.event.EventBrokerSupport;
import active.model.fight.Fight;
import active.model.fight.FightController;
import active.model.fight.Turn;
import active.model.fight.event.FightAware;
import active.model.fight.event.FightEventStream;

/**
 * @author Maarten Van Puymbroeck
 */
public class DefaultFightController implements FightController {

    private DefaultFight fight;
    private EventBrokerSupport<Event, ? extends FightEventStream> broker;


    public DefaultFightController(DefaultFight fight) {

        class DefaultFightEventStream extends ChannelAdapter<Event> implements FightEventStream {

        }

        this.fight = fight;
        this.broker = EventBrokerSupport.newInstance()
                                        .supplying(() -> new DefaultFightEventStream())
                                        .preparing((DefaultFightEventStream stream) -> (FightEventStream) stream.peek(
                                            event -> {
                                                if (event instanceof FightAware)
                                                    ((FightAware) event).setFight(this.fight);
                                            }
                                        ));
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
    public FightEventStream onEvent() {
        return this.broker.onEvent();
    }
}
