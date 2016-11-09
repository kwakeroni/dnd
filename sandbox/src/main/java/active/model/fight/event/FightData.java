package active.model.fight.event;

import active.model.event.Datum;
import active.model.event.Event;
import active.model.fight.Fight;
import active.model.fight.Participant;
import active.model.fight.Round;
import active.model.fight.Turn;

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
