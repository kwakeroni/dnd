package be.kwakeroni.dnd.model.actor;

import be.kwakeroni.dnd.type.value.Modifier;
import be.kwakeroni.dnd.type.base.Named;

import java.util.Collection;

/**
 * @author Maarten Van Puymbroeck
 */
public interface Actor extends Named {

    public Modifier getInitiativeModifier();

    public Collection<ActionType> getActions();

    public default Actor unmodifiableActor() {
        Actor self = this;
        return new Actor() {
            @Override
            public Modifier getInitiativeModifier() {
                return self.getInitiativeModifier();
            }

            @Override
            public Collection<ActionType> getActions() {
                return self.getActions();
            }

            @Override
            public String getName() {
                return self.getName();
            }

            @Override
            public boolean equals(Object obj) {
                return self.equals(obj);
            }

            @Override
            public int hashCode() {
                return self.hashCode();
            }

            @Override
            public String toString() {
                return self.toString();
            }
        };
    }

}
