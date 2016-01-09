package active.engine.internal.effect;

import active.model.effect.Damage;
import active.model.value.Score;

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
