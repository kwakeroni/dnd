package be.kwakeroni.dnd.rule;

import be.kwakeroni.dnd.model.creature.stats.Statistic;
import be.kwakeroni.dnd.rule.v35.rules.AbilityScores;
import be.kwakeroni.dnd.rule.v35.rules.CharacterImpl;
import be.kwakeroni.dnd.rule.v35.rules.StandardRace;
import be.kwakeroni.dnd.rule.v35.stats.Ability;
import be.kwakeroni.dnd.rule.v35.model.Character;
import be.kwakeroni.dnd.rule.v35.model.ClassLevel;

public class CharacterPrinter {

    public static void main(String[] args) {
        Character c = getCharacter();

        String template = """
        Character name: %-20s
        
            Ability Ability
             Score    Mod
        STR    %2s     %3s
        DEX    %2s     %3s
        CON    %2s     %3s
        INT    %2s     %3s
        WIS    %2s     %3s
        CHA    %2s     %3s
        """;

        String description = template.formatted(
            c.name(),
            c.stat(Ability.STR), Ability.modifier(c.stat(Ability.STR)),
            c.stat(Ability.DEX), Ability.modifier(c.stat(Ability.DEX)),
            c.stat(Ability.CON), Ability.modifier(c.stat(Ability.CON)),
            c.stat(Ability.INT), Ability.modifier(c.stat(Ability.INT)),
            c.stat(Ability.WIS), Ability.modifier(c.stat(Ability.WIS)),
            c.stat(Ability.CHA), Ability.modifier(c.stat(Ability.CHA))
        );

        System.out.println(description);
    }

    private static Character getCharacter() {
        AbilityScores abilities = new AbilityScores(18, 14, 16, 10, 8, 12);
        ClassLevel cclass = new ClassLevel() {
            @Override
            public <S> S effect(Statistic<S> stat, S value) {
                return value;
            }
        };
        return new CharacterImpl("Harun", StandardRace.DWARF, abilities, cclass);
    }

}
