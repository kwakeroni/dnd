package be.kwakeroni.dnd.model.effect;

import be.kwakeroni.dnd.type.base.Named;
import be.kwakeroni.dnd.type.die.Die;
import be.kwakeroni.dnd.type.die.Roll;
import be.kwakeroni.dnd.type.value.Modifier;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public interface Attack<D extends Die> extends Named {

    Modifier getAttackBonus();

    D getDamageDie();

    Damage getDamage(Roll<D> roll);

}
