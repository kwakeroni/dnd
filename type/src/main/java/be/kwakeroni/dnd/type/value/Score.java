package be.kwakeroni.dnd.type.value;

import be.kwakeroni.dnd.type.base.Describable;
import be.kwakeroni.dnd.type.base.Description;

import java.util.Objects;

/**
 * @author Maarten Van Puymbroeck
 */
public final /* value */ class Score implements Describable, Comparable<Score> {

    public static final Score ZERO = Score.of(0);

    private final int score;

    public static Score of(int score){
        return new Score(score);
    }

    private Score(int score) {
        this.score = score;
    }

    public int toInt(){
        return this.score;
    }

    public Score modify(Modifier modifier) {
        return this.plus(modifier.toInt());
    }

    public Score plus(int other) {
        return Score.of(this.score + other);
    }

    public Score plus(Score other) {
        return plus(other.score);
    }

    public Score minus(int other) {
        return Score.of(this.score - other);
    }

    public Score minus(Score other){
        return minus(other.score);
    }

    @Override
    public boolean equals(Object o) {
        return (this == o) ||
                (o instanceof Score other)
                && score == other.score;
    }

    @Override
    public int hashCode() {
        return Objects.hash(score);
    }

    @Override
    public int compareTo(Score o) {
        return Integer.compare(this.score, o.score);
    }

    @Override
    public void describe(Description description) {
        description.append(String.valueOf(this.score));
    }

    @Override
    public String toString() {
        return String.valueOf(score);
    }
    
    public static Score fromString(String value){
        return of(Integer.parseInt(value));
    }
}
