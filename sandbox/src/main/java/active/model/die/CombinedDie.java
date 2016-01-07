package active.model.die;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Maarten Van Puymbroeck
 */
public abstract class CombinedDie extends Die {

    CombinedDie(){

    }

    @Override
    int doRoll() {
        return dice().mapToInt(Die::doRoll).sum();
    }

    abstract Stream<Die> dice();

    @Override
    public String toString() {
        return dice().map(Object::toString).collect(Collectors.joining("+"));
    }
}
