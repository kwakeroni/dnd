package be.kwakeroni.dnd.rule.v35.rules;

import be.kwakeroni.dnd.model.creature.stats.Statistic;
import be.kwakeroni.dnd.rule.StatsRule;
import be.kwakeroni.dnd.rule.v35.model.Race;
import be.kwakeroni.dnd.rule.v35.ref.PlayersHandbook;

import static be.kwakeroni.dnd.rule.v35.stats.Ability.*;
import static be.kwakeroni.dnd.rule.StatsOperators.*;
import static be.kwakeroni.dnd.rule.StatsRule.modify;

// TODO: other racial traits
@PlayersHandbook(chapter="2", page="11-20", title="Races")
public enum StandardRace implements Race {
    @PlayersHandbook(chapter="2", page="14-15", title="Dwarfs")
    DWARF(modify(CON, scorePlus(2)),
          modify(CHA, scoreMinus(2))),

    @PlayersHandbook(chapter="2", page="15-16", title="Elves")
    ELF(modify(DEX, scorePlus(2)),
        modify(CON, scoreMinus(2))),

    @PlayersHandbook(chapter="2", page="16-17", title="Gnomes")
    GNOME(modify(CON, scorePlus(2)),
          modify(STR, scoreMinus(2))),

    @PlayersHandbook(chapter="2", page="18", title="Half-Elves")
    HALF_ELF(),

    @PlayersHandbook(chapter="2", page="18-19", title="Half-Orcs")
    HALF_ORC(modify(STR, scorePlus(2)),
             modify(INT, scoreMinus(2)),
             modify(INT, minimumScoreOf(3)),
             modify(CHA, scoreMinus(2))),

    @PlayersHandbook(chapter="2", page="19-20", title="Halflings")
    HALFLING(modify(DEX, scorePlus(2)),
             modify(STR, scoreMinus(2))),

    @PlayersHandbook(chapter="2", page="12-14", title="Humans")
    HUMAN();

    private final StatsRule rule;

    StandardRace(StatsRule... rules) {
        this.rule = StatsRule.aggregate(rules);
    }

    @Override
    public <S> S effect(Statistic<S> stat, S value) {
        return rule.effect(stat, value);
    }
}
