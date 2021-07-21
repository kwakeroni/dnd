package be.kwakeroni.dnd.rule.v35;

import be.kwakeroni.dnd.rule.v35.model.Race;
import be.kwakeroni.dnd.rule.v35.ref.PlayersHandbook;
import be.kwakeroni.dnd.type.value.Score;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static be.kwakeroni.dnd.rule.v35.stats.Ability.*;
import static be.kwakeroni.dnd.rule.v35.rules.StandardRace.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("The Standard Races")
class StandardRaceTest {

    @Nested
    @DisplayName("[PH-T2.1] adjust Racial Abilities for")
    @PlayersHandbook(chapter="2", page="12", table="2-1", title="Racial Ability Adjustments")
    class RacialAbilityAdjustmentTest {
        @Test
        @DisplayName("[Human] without adjustments")
        void testRacialAbilityAdjustmentForHuman() {
            testRacialAbilityAdjustment(HUMAN, 0, 0, 0, 0, 0, 0);
        }

        @Test
        @DisplayName("[Dwarf] with Constitution +2 and Charisma -2")
        void testRacialAbilityAdjustmentForDwarf() {
            testRacialAbilityAdjustment(DWARF, 0, 0, 2, 0, 0, -2);
        }

        @Test
        @DisplayName("[Elf] with Dexterity +2 and Constitution -2")
        void testRacialAbilityAdjustmentForElf() {
            testRacialAbilityAdjustment(ELF, 0, 2, -2, 0, 0, 0);
        }

        @Test
        @DisplayName("[Gnome] with Constitution +2 and Strength -2")
        void testRacialAbilityAdjustmentForGnome() {
            testRacialAbilityAdjustment(GNOME, -2, 0, 2, 0, 0, 0);
        }

        @Test
        @DisplayName("[Half-Elf] without adjustments")
        void testRacialAbilityAdjustmentForHalfElf() {
            testRacialAbilityAdjustment(HALF_ELF, 0, 0, 0, 0, 0, 0);
        }

        @Test
        @DisplayName("[Half-Orc] with Strength +2, Intelligence -2 and Charisma -2")
        void testRacialAbilityAdjustmentForHalfOrc() {
            testRacialAbilityAdjustment(6, HALF_ORC, 8, 6, 6, 4, 6, 4);
        }

        @Test
        @DisplayName("[Half-Orc^1] with minimal Intelligence of 3")
        void testRacialAbilityAdjustmentForHalfOrcWithMinimalIntelligence() {
            testRacialAbilityAdjustment(0, HALF_ORC, 2, 0, 0, 3, 0, -2);
        }

        @Test
        @DisplayName("[Halfling] with Constitution +2 and Strength -2")
        void testRacialAbilityAdjustmentForHalfling() {
            testRacialAbilityAdjustment(HALFLING, -2, 2, 0, 0, 0, 0);
        }

        void testRacialAbilityAdjustment(Race race, int str, int dex, int con, int intl, int wis, int cha) {
            testRacialAbilityAdjustment(0, race, str, dex, con, intl, wis, cha);
        }

        void testRacialAbilityAdjustment(int baseline, Race race, int str, int dex, int con, int intl, int wis, int cha) {
            assertThat(race.effect(STR, Score.of(baseline))).isEqualTo(Score.of(str));
            assertThat(race.effect(DEX, Score.of(baseline))).isEqualTo(Score.of(dex));
            assertThat(race.effect(CON, Score.of(baseline))).isEqualTo(Score.of(con));
            assertThat(race.effect(INT, Score.of(baseline))).isEqualTo(Score.of(intl));
            assertThat(race.effect(WIS, Score.of(baseline))).isEqualTo(Score.of(wis));
            assertThat(race.effect(CHA, Score.of(baseline))).isEqualTo(Score.of(cha));
        }
    }
    //()

}