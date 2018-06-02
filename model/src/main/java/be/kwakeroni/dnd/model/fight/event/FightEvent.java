package be.kwakeroni.dnd.model.fight.event;

import be.kwakeroni.dnd.event.Event;
import be.kwakeroni.dnd.model.fight.Fight;

/**
 * @author Maarten Van Puymbroeck
 */
public abstract class FightEvent implements Event, FightAware {

    private Fight fight;

    public FightEvent(){

    }

    public FightEvent(Fight fight) {
        this.fight = fight;
    }

    public Fight getFight() {
        return fight;
    }

    @Override
    public void setFight(Fight fight) {
        this.fight = fight;
    }
}
