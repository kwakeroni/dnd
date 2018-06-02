package be.kwakeroni.dnd.io.model;

import be.kwakeroni.dnd.model.actor.ActionType;
import be.kwakeroni.dnd.model.creature.Creature;
import be.kwakeroni.dnd.model.creature.stats.StatisticEntry;

public interface MutableCreature extends Creature {

    public <S> void addStatistic(StatisticEntry<S> entry);

    public void addAction(ActionType action);

    public void setName(String name);

}
