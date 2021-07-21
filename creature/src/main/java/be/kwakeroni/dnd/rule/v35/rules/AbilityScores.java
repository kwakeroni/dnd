package be.kwakeroni.dnd.rule.v35.rules;

import be.kwakeroni.dnd.rule.StatsRule;
import be.kwakeroni.dnd.rule.v35.stats.Ability;
import be.kwakeroni.dnd.model.creature.stats.Statistic;
import be.kwakeroni.dnd.rule.v35.ref.PlayersHandbook;
import be.kwakeroni.dnd.type.value.Score;

import java.util.EnumMap;
import java.util.Map;

import static be.kwakeroni.dnd.rule.v35.stats.Ability.*;

@PlayersHandbook(chapter = "1", page="7-10", title="Abilities")
public class AbilityScores implements StatsRule {

    private final Map<Ability, Score> scores;

    public AbilityScores(int str, int dex, int con, int intl, int wis, int cha) {
        this(Score.of(str), Score.of(dex), Score.of(con), Score.of(intl), Score.of(wis), Score.of(cha));
    }

    public AbilityScores(Score str, Score dex, Score con, Score intl, Score wis, Score cha) {
        this.scores = new EnumMap<>(Ability.class);
        this.scores.put(STR, str);
        this.scores.put(DEX, dex);
        this.scores.put(CON, con);
        this.scores.put(INT, intl);
        this.scores.put(WIS, wis);
        this.scores.put(CHA, cha);
    }

    @Override
    public <S> S effect(Statistic<S> stat, S value) {
        if (! (stat instanceof Ability ability)) {
            return value;
        }
        return (S) scores.get(ability);
    }
}
