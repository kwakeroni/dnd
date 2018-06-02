package be.kwakeroni.dnd.model.fight;

import be.kwakeroni.dnd.model.actor.Actor;

/**
 * @author Maarten Van Puymbroeck
 */
public interface Turn {

    public <AP extends Participant & Actor> AP getActor();

}
