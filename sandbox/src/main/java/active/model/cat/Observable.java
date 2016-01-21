package active.model.cat;

import active.model.event.EventStream;

/**
 * @author Maarten Van Puymbroeck
 */
public interface Observable<S extends EventStream> {

    S on();

}
