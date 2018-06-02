package be.kwakeroni.dnd.io.model;

import be.kwakeroni.dnd.model.creature.Creature;
import be.kwakeroni.dnd.model.creature.Party;

public interface MutableParty extends Party {

    public void add(Creature creature);
    public void setName(String name);
}
