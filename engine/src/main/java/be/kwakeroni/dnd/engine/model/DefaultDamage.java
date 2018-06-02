package be.kwakeroni.dnd.engine.model;

import be.kwakeroni.dnd.model.effect.Damage;
import be.kwakeroni.dnd.type.value.Score;

public class DefaultDamage implements Damage {

    private final Score amount;

    public DefaultDamage(Score amount) {
        this.amount = amount;
    }

    @Override
    public Score getAmount() {
        return this.amount;
    }
    
}
