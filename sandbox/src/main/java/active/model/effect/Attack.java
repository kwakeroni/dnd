package active.model.effect;

import active.model.cat.Named;
import active.model.die.Die;
import active.model.die.Roll;
import active.model.value.Modifier;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public interface Attack<D extends Die> extends Named {

    Modifier getAttackBonus();

    D getDamageDie();

    Damage getDamage(Roll<D> roll);

}
