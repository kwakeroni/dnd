package be.kwakeroni.dnd.rule;

import be.kwakeroni.dnd.type.value.Score;

import java.util.function.UnaryOperator;

public final class StatsOperators {

    private StatsOperators() {

    }

    public static UnaryOperator<Score> resetScore(int value) {
        return score -> {
            if (! Score.ZERO.equals(score)) {
                System.out.println("Resetting non-zero score");
            }
            return Score.of(value);
        };
    }

    public static UnaryOperator<Score> scorePlus(int value) {
        return score -> score.plus(value);
    }

    public static UnaryOperator<Score> scoreMinus(int value) {
        return score -> score.minus(value);
    }

    public static UnaryOperator<Score> minimumScoreOf(int minimum) {
        return score -> Score.of(Math.max(minimum, score.toInt()));
    }
}
