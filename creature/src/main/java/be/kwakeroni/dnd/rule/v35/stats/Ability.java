package be.kwakeroni.dnd.rule.v35.stats;

import be.kwakeroni.dnd.model.creature.stats.Statistic;
import be.kwakeroni.dnd.rule.v35.ref.PlayersHandbook;
import be.kwakeroni.dnd.type.value.Modifier;
import be.kwakeroni.dnd.type.value.Score;

@PlayersHandbook(chapter = "1", page="7-10", title="Abilities")
public enum Ability implements Statistic<Score> {
    STR,
    DEX,
    CON,
    INT,
    WIS,
    CHA;

    @Override
    public Score baseline() {
        return Score.ZERO;
    }

    public static Modifier modifier(Score score) {
        int s = score.toInt();
        int m = (s - s%2 - 10) / 2;
        return Modifier.of(m);
    }

    @Override
    public Score fromString(String value) {
        return Score.fromString(value);
    }
}
