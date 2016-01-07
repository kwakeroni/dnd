package active.model.creature;

import active.model.action.Actor;
import active.model.action.Hittable;
import active.model.value.Modifier;

/**
 * @author Maarten Van Puymbroeck
 */
public interface Creature extends Actor, Hittable {

    @Override
    default Modifier getInitiativeModifier(){
        return Modifier.of(0);
    }
}
