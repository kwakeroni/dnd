package be.kwakeroni.dnd.rule.v35.rules;

import be.kwakeroni.dnd.model.creature.stats.Statistic;
import be.kwakeroni.dnd.rule.StatsRule;
import be.kwakeroni.dnd.rule.v35.model.Character;
import be.kwakeroni.dnd.rule.v35.model.ClassLevel;
import be.kwakeroni.dnd.rule.v35.model.Race;

public class CharacterImpl implements Character {

    private final String name;
    private final StatsRule stats;

    public CharacterImpl(String name, Race race, AbilityScores abilities, ClassLevel classLevel) {
        this.name = name;
        this.stats = StatsRule.aggregate(race, abilities, classLevel);
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public <S> S stat(Statistic<S> stat) {
        return this.stats.effect(stat);
    }
}
