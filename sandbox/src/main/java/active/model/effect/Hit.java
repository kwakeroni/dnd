package active.model.effect;

import java.util.Collection;

import active.model.cat.Actor;
import active.model.cat.Hittable;
import active.model.effect.Damage;

public interface Hit {

    public Collection<Damage> getDamage();
    
}
