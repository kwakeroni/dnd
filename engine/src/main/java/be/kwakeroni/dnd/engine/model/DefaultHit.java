package be.kwakeroni.dnd.engine.model;

import java.util.Collection;
import java.util.Collections;

import be.kwakeroni.dnd.model.effect.Hit;
import be.kwakeroni.dnd.model.effect.Damage;
import be.kwakeroni.dnd.type.value.Score;

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
