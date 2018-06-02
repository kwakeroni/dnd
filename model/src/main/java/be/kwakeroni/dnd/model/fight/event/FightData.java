package be.kwakeroni.dnd.model.fight.event;

import be.kwakeroni.dnd.event.Datum;
import be.kwakeroni.dnd.event.Event;
import be.kwakeroni.dnd.model.fight.Fight;
import be.kwakeroni.dnd.model.fight.Participant;
import be.kwakeroni.dnd.model.fight.Round;
import be.kwakeroni.dnd.model.fight.Turn;


import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * @author Maarten Van Puymbroeck
 */
public class FightData {

    private final Fight fight;

    public FightData(Fight fight) {
        this.fight = fight;
    }

    public Fight getFight(){
        return this.fight;
    }

    public Datum<Stream<Participant>> participants(){
        return new Datum<Stream<Participant>>() {
            @Override
            public Stream<Participant> get() {
                return fight.getParticipants();
            }

            @Override
            public void onChanged(Consumer<Event> consumer) {
                fight.on().participantsChanged().forEach(consumer);
            }
        };
    }
    
    public Datum<Optional<Turn>> turn() {
        return new Datum<Optional<Turn>>() {

            @Override
            public Optional<Turn> get() {
                return fight.getCurrentRound().flatMap(Round::getCurrentTurn);
            }

            @Override
            public void onChanged(Consumer<Event> consumer) {
                fight.on().turnStarted().forEach(consumer);
                fight.on().turnEnded().forEach(consumer);
            }
            
        };
    }

}
