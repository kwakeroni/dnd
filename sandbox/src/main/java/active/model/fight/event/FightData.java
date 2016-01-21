package active.model.fight.event;

import active.model.event.Datum;
import active.model.event.Event;
import active.model.fight.Fight;
import active.model.fight.Participant;

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

}
