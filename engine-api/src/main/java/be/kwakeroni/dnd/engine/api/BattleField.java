package be.kwakeroni.dnd.engine.api;

import be.kwakeroni.dnd.model.creature.Party;
import be.kwakeroni.dnd.model.fight.Fight;
import be.kwakeroni.dnd.model.fight.Participant;

public interface BattleField {

    public void add(Party party);
    public void add(Participant participant);

    public FightController startFight();
    public FightController continueFight(Fight fight);
}
