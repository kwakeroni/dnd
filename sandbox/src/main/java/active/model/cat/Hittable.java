package active.model.cat;

import active.model.action.Hit;
import active.model.value.Score;

/**
 * @author Maarten Van Puymbroeck
 */
public interface Hittable extends Named {
    
    public Score getHP();
    
    public void hit(Hit hit);
    
}
