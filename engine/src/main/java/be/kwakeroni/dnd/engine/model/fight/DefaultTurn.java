package be.kwakeroni.dnd.engine.model.fight;

import be.kwakeroni.dnd.model.actor.Actor;
import be.kwakeroni.dnd.model.fight.Participant;
import be.kwakeroni.dnd.model.fight.Turn;

/**
 * @author Maarten Van Puymbroeck
 */
public class DefaultTurn implements Turn {

    private final Participant actor;

    public <AP extends Participant & Actor> DefaultTurn(AP actor) {
        this.actor = actor;
    }

    @Override
    public <AP extends Participant & Actor> AP getActor() {
        return (AP) this.actor;
    }
}
