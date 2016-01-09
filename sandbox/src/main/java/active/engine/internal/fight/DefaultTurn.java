package active.engine.internal.fight;

import active.model.fight.IsActor;
import active.model.fight.Participant;
import active.model.fight.Turn;

/**
 * @author Maarten Van Puymbroeck
 */
public final class DefaultTurn implements Turn {

    private final Participant actor;

    public <AP extends Participant & IsActor> DefaultTurn(AP actor) {
        this.actor = actor;
    }

    @Override
    public <AP extends Participant & IsActor> AP getActor() {
        return (AP) this.actor;
    }
}
