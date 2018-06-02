package be.kwakeroni.dnd.ui.base.fight;

import be.kwakeroni.dnd.engine.api.FightController;
import be.kwakeroni.dnd.model.fight.Fight;
import be.kwakeroni.dnd.model.fight.Participant;
import be.kwakeroni.dnd.type.description.DecoratedDescription;

import java.util.stream.Stream;

public interface FightLogger {

    public void dump(String header, Stream<? extends Participant> participants);

    public default void dump(String header, FightController fight) {
        dump(header, fight.getState());
    }

    public void dump(String header, Fight fight);

    public DecoratedDescription.Builder log();

}
