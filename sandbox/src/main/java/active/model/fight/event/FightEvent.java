package active.model.fight.event;

import active.engine.event.Event;
import active.model.fight.Fight;

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
