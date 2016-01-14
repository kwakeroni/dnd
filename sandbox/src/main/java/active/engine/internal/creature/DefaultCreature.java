package active.engine.internal.creature;

import active.model.effect.Hit;
import active.model.creature.Creature;
import active.model.value.Modifier;
import active.model.value.Score;

public class DefaultCreature implements Creature {
    
    private Score maxHP;
    private Score currentHP;
    private Modifier init;
    private String name;
    
    public DefaultCreature(String name, Score maxHP, Modifier init){
        this.name = name;
        this.maxHP = maxHP;
        this.currentHP = maxHP;
        this.init = init;
    }

    @Override
    public Score getHP() {
        return currentHP;
    }

    @Override
    public Modifier getInitiativeModifier() {
        return init;
    }

    @Override
    public String getName() {
        return name;
    }


    @Override
    public void hit(Hit hit) {
        hit.getDamage().forEach( dmg -> this.currentHP = this.currentHP.minus(dmg.getAmount()));
    }

    
}
