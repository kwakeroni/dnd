package active.model.die;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Maarten Van Puymbroeck
 */
public abstract class CombinedDie extends Die {

    CombinedDie(){

    }

    abstract Stream<Die> dice();

    @Override
    int doRoll() {
        return dice().mapToInt(Die::doRoll).sum();
    }

    @Override
    public int getMaxRoll() {
        return dice().mapToInt(Die::getMaxRoll).sum();
    }

    @Override
    public String toString() {
        return dice().map(Object::toString).collect(Collectors.joining("+"));
    }
}
