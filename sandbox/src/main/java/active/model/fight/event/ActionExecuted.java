package active.model.fight.event;

import active.model.action.Action;
import active.model.event.Event;
import active.model.fight.Fight;

/**
 * @author Maarten Van Puymbroeck
 */
public class ActionExecuted implements Event {

    private final Action<? super Fight> action;

    public ActionExecuted(Action<? super Fight> action) {
        this.action = action;
    }

    public Action<? super Fight> getAction() {
        return action;
    }
}
