package be.kwakeroni.dnd.model.fight.event;

import be.kwakeroni.dnd.event.Event;
import be.kwakeroni.dnd.model.actor.Action;
import be.kwakeroni.dnd.model.fight.Fight;

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
