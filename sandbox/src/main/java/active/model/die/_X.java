package active.model.die;

import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author Maarten Van Puymbroeck
 */
public class _X<D extends Die> extends CombinedDie {

    private final int nb;
    private final D d;

    _X(int nb, D d) {
        this.nb = nb;
        this.d = d;
    }

    @Override
    Stream<Die> dice() {
        return IntStream.range(0, nb).mapToObj( (int i) -> d );
    }

    @Override
    public String toString() {
        if (d instanceof SingleDie){
        return nb + d.toString();
        } else {
            return nb + "(" + d.toString() + ")";
        }
    }
}
