package active.model.cat;

import active.model.value.Modifier;

/**
 * @author Maarten Van Puymbroeck
 */
public interface Actor extends Named {

    public Modifier getInitiativeModifier();

}
