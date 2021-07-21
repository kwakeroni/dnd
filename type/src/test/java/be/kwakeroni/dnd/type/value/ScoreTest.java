package be.kwakeroni.dnd.type.value;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("a Score")
class ScoreTest {

    @Test
    @DisplayName("has an equality operation")
    void testEquality() {
        Score score = Score.of(18);
        assertThat(score).describedAs("is equal to itself").isEqualTo(score);
        assertThat(score).describedAs("is equal to a score with the same value").isEqualTo(Score.of(18));
        assertThat(score).describedAs("is not equal to a score with another value").isNotEqualTo(Score.of(19));
        assertThat(score).describedAs("is not equal to another type of instance").isNotEqualTo(Modifier.of(18));
    }

}