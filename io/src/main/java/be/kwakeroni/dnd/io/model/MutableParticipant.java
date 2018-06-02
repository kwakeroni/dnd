package be.kwakeroni.dnd.io.model;

import be.kwakeroni.dnd.model.creature.Creature;
import be.kwakeroni.dnd.model.fight.Participant;
import be.kwakeroni.dnd.type.value.Score;

public interface MutableParticipant extends Participant {

    public void setInitiative(Score initiative);

    public void setAsCreature(Creature creature);
}
