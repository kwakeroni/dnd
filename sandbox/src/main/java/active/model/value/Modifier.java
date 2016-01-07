package active.model.value;

/**
 * @author Maarten Van Puymbroeck
 */
public final /* value */ class Modifier implements Comparable<Modifier> {

    private final int modifier;

    public static Modifier of(int modifier){
        return new Modifier(modifier);
    }

    private Modifier(int modifier) {
        this.modifier = modifier;
    }

    public int toInt(){
    return this.modifier;
    }

    @Override
    public int compareTo(Modifier o) {
        return (this.modifier == o.modifier)? 0 :
                    (this.modifier < o.modifier)? -1 : 1;
    }

    @Override
    public String toString() {
        return
            ((modifier < 0)? "-" : "+") + String.valueOf(modifier);
    }


}
