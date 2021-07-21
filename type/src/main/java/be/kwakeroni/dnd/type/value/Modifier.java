package be.kwakeroni.dnd.type.value;

/**
 *  * @author Maarten Van Puymbroeck
 */
public final /* value */ class Modifier implements Comparable<Modifier> {

    public static final Modifier ZERO = Modifier.of(0);

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
    public boolean equals(Object o) {
        return (this == o) ||
                o instanceof Modifier other
                && this.modifier == other.modifier;
    }

    @Override
    public int hashCode() {
        return modifier;
    }

    @Override
    public int compareTo(Modifier o) {
        return Integer.compare(this.modifier, o.modifier);
    }

    @Override
    public String toString() {
        return
            (modifier <= 0)? String.valueOf(modifier) : "+" + modifier;
    }

    public static Modifier fromString(String value){
        return Modifier.of(Integer.parseInt(value));
    }

}
