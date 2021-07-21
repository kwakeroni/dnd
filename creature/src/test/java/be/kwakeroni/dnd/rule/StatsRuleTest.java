package be.kwakeroni.dnd.rule;

import be.kwakeroni.dnd.rule.v35.stats.Ability;
import be.kwakeroni.dnd.type.value.Score;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static be.kwakeroni.dnd.rule.StatsOperators.resetScore;
import static be.kwakeroni.dnd.rule.StatsOperators.scorePlus;
import static org.assertj.core.api.Assertions.assertThat;

class StatsRuleTest {

    @Nested
    @DisplayName("a simple StatsRule")
    class SimpleTest {
        StatsRule rule = StatsRule.modify(Ability.STR, scorePlus(18));

        @Test
        @DisplayName("Affects a statistic")
        public void testStatistic() {
            assertThat(rule.effect(Ability.STR, Score.ZERO)).isEqualTo(Score.of(18));
        }

        @Test
        @DisplayName("Does not effect another statistic")
        public void testOtherStatistic() {
            assertThat(rule.effect(Ability.DEX, Score.ZERO)).isEqualTo(Score.ZERO);
        }
    }

    @Nested
    @DisplayName("an aggregate StatsRule")
    class AggregateTest {

        @Nested
        @DisplayName("with a single rule")
        class SingleRuleTest {
            final StatsRule singleton = StatsRule.aggregate(StatsRule.modify(Ability.STR, scorePlus(3)));

            @Test
            @DisplayName("only has an effect on the concerned statistic")
            public void testStatistic() {
                assertThat(singleton.effect(Ability.STR, Score.ZERO)).isEqualTo(Score.of(3));
            }

            @Test
            @DisplayName("has no effect on another statistic")
            public void testOtherStatistic() {
                assertThat(singleton.effect(Ability.DEX, Score.ZERO)).isEqualTo(Score.ZERO);
            }
        }

        @Nested
        @DisplayName("with rules for distinct statistics")
        class DisjointRuleTest {
            final StatsRule aggregate = StatsRule.aggregate(
                    StatsRule.modify(Ability.STR, scorePlus(3)),
                    StatsRule.modify(Ability.DEX, scorePlus(4))
            );

            @Test
            @DisplayName("has one effect on one statistic")
            public void testOneStatistic() {
                assertThat(aggregate.effect(Ability.STR, Score.ZERO)).isEqualTo(Score.of(3));
            }

            @Test
            @DisplayName("has another effect on the other statistic")
            public void testOtherStatistic() {
                assertThat(aggregate.effect(Ability.DEX, Score.ZERO)).isEqualTo(Score.of(4));
            }

            @Test
            @DisplayName("has no effect on any other statistic")
            public void testAnyOtherStatistic() {
                assertThat(aggregate.effect(Ability.CON, Score.ZERO)).isEqualTo(Score.ZERO);
            }
        }

        @Nested
        @DisplayName("with multiple rules for one statistic")
        class ConjointRuleTest {
            final StatsRule aggregate = StatsRule.aggregate(
                    StatsRule.modify(Ability.STR, resetScore(7)),
                    StatsRule.modify(Ability.STR, scorePlus(3)),
                    StatsRule.modify(Ability.DEX, scorePlus(4))
            );

            @Test
            @DisplayName("has an accumulated effect on one statistic")
            public void testOneStatistic() {
                assertThat(aggregate.effect(Ability.STR, Score.ZERO)).isEqualTo(Score.of(10));
            }

            @Test
            @DisplayName("has another effect on the other statistic")
            public void testOtherStatistic() {
                assertThat(aggregate.effect(Ability.DEX, Score.ZERO)).isEqualTo(Score.of(4));
            }
        }
    }
}