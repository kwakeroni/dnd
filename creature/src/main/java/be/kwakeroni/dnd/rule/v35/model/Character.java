package be.kwakeroni.dnd.rule.v35.model;

import be.kwakeroni.dnd.model.creature.stats.Statistic;

public interface Character {

    String name();
    <S> S stat(Statistic<S> stat);
}
