package active.engine.internal.action;

import java.util.Collection;
import java.util.Collections;

import active.engine.internal.effect.DefaultDamage;
import active.model.action.Hit;
import active.model.effect.Damage;
import active.model.value.Score;

public class DefaultHit implements Hit {

    private final Collection<Damage> damage;
    
    public static DefaultHit of(int damage){
        return new DefaultHit(new DefaultDamage(Score.of(damage)));
    }
    
    public static DefaultHit of(Damage damage){
        return new DefaultHit(damage);
    }
    
    private DefaultHit(Damage damage){
        this.damage = Collections.singleton(damage);
    }
    
    @Override
    public Collection<Damage> getDamage() {
        return this.damage;
    }

}
