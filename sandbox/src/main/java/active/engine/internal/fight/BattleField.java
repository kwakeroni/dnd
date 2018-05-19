package active.engine.internal.fight;

import active.engine.event.EventBrokerSupport;
import active.model.creature.Party;
import active.model.event.Event;
import active.model.event.EventStream;
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
    EventBrokerSupport<?> broker;

    public BattleField(EventBrokerSupport<?> broker) {
        this.broker = broker;
    }

    public void add(Party party) {
        party.members()
                .map(DefaultParticipant::ofCreature)
                .forEach(this::add);
    }

    public void add(Participant participant) {
        this.participants.add(participant);
    }

    public Stream<Participant> participants() {
        return this.participants.stream();
    }

    public FightController startFight() {

        DefaultFight fight = new DefaultFight();
        this.participants.forEach(fight::add);

        return loadFight(fight);
    }

    public FightController continueFight(DefaultFight fight) {
        return loadFight(fight);
    }

    private FightController loadFight(DefaultFight fight) {

        EventBrokerSupport<FightEventStream> broker =
                this.broker
                        .supplying((EventStream source) -> (FightEventStream) source::event)
                        .preparing((Consumer<Event>) event -> {
                                    if (event instanceof FightAware)
                                        ((FightAware) event).setFight(fight);

                                }
                        );

        fight.setBroker(broker);

        return new DefaultFightController(fight, broker);
    }

}
