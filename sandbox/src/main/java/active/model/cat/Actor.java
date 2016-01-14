package active.model.cat;

import active.model.value.Modifier;

/**
 * @author Maarten Van Puymbroeck
 */
public interface Actor extends Named {

    public Modifier getInitiativeModifier();


    public default Actor unmodifiableActor() {
        Actor self = this;
        return new Actor() {
            @Override
            public Modifier getInitiativeModifier() {
                return self.getInitiativeModifier();
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
