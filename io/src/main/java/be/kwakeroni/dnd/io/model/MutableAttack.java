package be.kwakeroni.dnd.io.model;

import be.kwakeroni.dnd.model.effect.Attack;
import be.kwakeroni.dnd.type.die.Die;
import be.kwakeroni.dnd.type.value.Modifier;

public interface MutableAttack<D extends Die> extends Attack<D> {

    public void setName(String name);

    public void setAttackBonus(Modifier attackBonus);

    public void setDamageDie(D damageDie);

}
