package active.model.fight;

import active.model.cat.Actor;

/**
 * @author Maarten Van Puymbroeck
 */
public interface Turn {

    public <AP extends Participant & Actor> AP getActor();

}
