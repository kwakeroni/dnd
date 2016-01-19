package active.model.value;

import active.model.cat.Describable;
import active.model.cat.Description;

/**
 * @author Maarten Van Puymbroeck
 */
public final /* value */ class Score implements Describable, Comparable<Score> {

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
    
    public Score minus(Score other){
        return Score.of(this.score - other.score);
    }

    @Override
    public int compareTo(Score o) {
        return (this.score == o.score)? 0 :
                    (this.score < o.score)? -1 : 1;
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
