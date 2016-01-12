package active.engine.internal.fight;

import active.model.cat.Actor;
import active.model.fight.Participant;
import active.model.fight.Turn;

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
