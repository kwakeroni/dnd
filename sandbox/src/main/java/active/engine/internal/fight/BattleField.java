package active.engine.internal.fight;

import active.engine.event.EventBrokerSupport;
import active.model.fight.Fight;
import active.model.fight.FightController;
import active.model.fight.Participant;
import active.model.fight.event.FightEventStream;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Stream;

/**
 * @author Maarten Van Puymbroeck
 */
public class BattleField {

    Collection<Participant> participants = new HashSet<>();

    public void add(Participant participant){
        this.participants.add(participant);
    }

    public Stream<Participant> participants() { return this.participants.stream(); }

    public FightController startFight(){
        
        EventBrokerSupport<FightEventStream> broker = EventBrokerSupport.newInstance()
                .supplying((source) -> () -> source.event())
//                .preparing((DefaultFightEventStream stream) -> (FightEventStream) stream.peek(
//                    event -> {
//                        if (event instanceof FightAware)
//                            ((FightAware) event).setFight(this.fight);
//                    }
//                ))
                ;

        DefaultFight fight = new DefaultFight(broker);
        this.participants.forEach(fight::add);

        return new DefaultFightController(fight, broker);
    }

}
