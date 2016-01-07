package active.model.value;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author Maarten Van Puymbroeck
 */
public final /* value */ class Score implements Comparable<Score> {

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

    @Override
    public int compareTo(Score o) {
        return (this.score == o.score)? 0 :
                    (this.score < o.score)? -1 : 1;
    }

    @Override
    public String toString() {
        return String.valueOf(score);
    }
}
