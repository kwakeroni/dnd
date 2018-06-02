package be.kwakeroni.dnd.io.model;

import be.kwakeroni.dnd.event.EventBroker;
import be.kwakeroni.dnd.model.fight.Fight;
import be.kwakeroni.dnd.model.fight.Participant;
import be.kwakeroni.dnd.model.fight.event.FightEventStream;

public interface MutableFight extends Fight {

    public void setLastRoundNumber(int count);

    public void add(Participant participant);

}
