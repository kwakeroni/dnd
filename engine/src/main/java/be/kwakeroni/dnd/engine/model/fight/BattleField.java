package be.kwakeroni.dnd.engine.model.fight;

import be.kwakeroni.dnd.engine.event.EventBrokerSupport;
import be.kwakeroni.dnd.event.Event;
import be.kwakeroni.dnd.event.EventStream;
import be.kwakeroni.dnd.model.creature.Party;
import be.kwakeroni.dnd.engine.api.FightController;
import be.kwakeroni.dnd.model.fight.Fight;
import be.kwakeroni.dnd.model.fight.Participant;
import be.kwakeroni.dnd.model.fight.event.FightAware;
import be.kwakeroni.dnd.model.fight.event.FightEventStream;

import java.util.Collection;
import java.util.HashSet;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * @author Maarten Van Puymbroeck
 */
public class BattleField implements be.kwakeroni.dnd.engine.api.BattleField {

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

    public FightController continueFight(Fight fight) {
        // TODO this cast is not good
        return loadFight((DefaultFight) fight);
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
