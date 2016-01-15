package active.engine.internal.fight;

import active.engine.event.EventBrokerSupport;
import active.model.creature.Party;
import active.model.event.Event;
import active.model.fight.Fight;
import active.model.fight.FightController;
import active.model.fight.Participant;
import active.model.fight.event.FightAware;
import active.model.fight.event.FightEventStream;

import java.util.Collection;
import java.util.HashSet;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * @author Maarten Van Puymbroeck
 */
public class BattleField {

    Collection<Participant> participants = new HashSet<>();

    public void add(Party party){
        party.members()
             .map(DefaultParticipant::ofCreature)
             .forEach(this::add);
    }
    
    public void add(Participant participant){
        this.participants.add(participant);
    }

    public Stream<Participant> participants() { return this.participants.stream(); }

    public FightController startFight(){

        DefaultFight fight = new DefaultFight();

        
        EventBrokerSupport<FightEventStream> broker =
            EventBrokerSupport.newInstance()
                .supplying((source) -> (FightEventStream) source::event)
                .preparing((Consumer<Event>) event -> {
                        if (event instanceof FightAware)
                            ((FightAware) event).setFight(fight);

                    }
                )
                ;

        fight.setBroker(broker);
        this.participants.forEach(fight::add);

        return new DefaultFightController(fight, broker);
    }

}
