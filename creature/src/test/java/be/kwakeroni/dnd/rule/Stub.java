package be.kwakeroni.dnd.rule;

import be.kwakeroni.dnd.model.creature.stats.Statistic;
import be.kwakeroni.dnd.rule.v35.model.Race;

public class Stub {

    public static Race NIL_RACE = new Race() {
        @Override
        public <S> S effect(Statistic<S> stat, S value) {
            return value;
        }
    };

}
