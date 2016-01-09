package active.model.action;

import java.util.Collection;

import active.model.effect.Damage;

public interface Hit {

    public Collection<Damage> getDamage();
    
}
