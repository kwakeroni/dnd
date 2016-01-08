package active.engine.internal.fight;

import active.model.fight.Participant;
import active.model.fight.Turn;

/**
 * @author Maarten Van Puymbroeck
 */
public class DefaultTurn implements Turn {

    private final Participant actor;

    public DefaultTurn(Participant actor) {
        this.actor = actor;
    }

    @Override
    public Participant getActor() {
        return this.actor;
    }
}
