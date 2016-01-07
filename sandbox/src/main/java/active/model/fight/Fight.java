package active.model.fight;

import active.model.action.Actor;
import active.model.action.Hittable;

import java.util.stream.Stream;

/**
 * @author Maarten Van Puymbroeck
 */
public interface Fight {

    public void add(Participant participant);

    public Stream<Participant> getActors();

    public Stream<Participant> getTargets();



}
