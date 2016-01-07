package active.engine.util;

import java.util.stream.Stream;

/**
 * @author Maarten Van Puymbroeck
 */
public interface Streamable<T> {

    public Stream<T> stream();

}
