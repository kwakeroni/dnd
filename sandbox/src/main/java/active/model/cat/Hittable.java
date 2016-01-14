package active.model.cat;

import active.model.effect.Hit;
import active.model.value.Score;

/**
 * @author Maarten Van Puymbroeck
 */
public interface Hittable extends Named {
    
    public Score getHP();
    
    public void hit(Hit hit);

    public default Hittable unmodifiableHittable(){
        Hittable self = this;
        return new Hittable() {
            @Override
            public Score getHP() {
                return self.getHP();
            }

            @Override
            public void hit(Hit hit) {
                throw new UnsupportedOperationException("Modification of Hittable not allowed");
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
