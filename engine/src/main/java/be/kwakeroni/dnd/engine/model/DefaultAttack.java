package be.kwakeroni.dnd.engine.model;

import be.kwakeroni.dnd.io.model.MutableAttack;
import be.kwakeroni.dnd.type.die.Die;
import be.kwakeroni.dnd.type.die.Roll;
import be.kwakeroni.dnd.model.effect.Attack;
import be.kwakeroni.dnd.model.effect.Damage;
import be.kwakeroni.dnd.type.value.Modifier;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class DefaultAttack<D extends Die> implements Attack<D>, MutableAttack<D> {

    private String name;
    private Modifier attackBonus;
    private D damageDie;

    public DefaultAttack(){

    }

    public DefaultAttack(String name, Modifier attackBonus, D damageDie) {
        this.name = name;
        this.attackBonus = attackBonus;
        this.damageDie = damageDie;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Modifier getAttackBonus() {
        return attackBonus;
    }

    @Override
    public D getDamageDie() {
        return damageDie;
    }

    @Override
    public Damage getDamage(Roll<D> roll) {
        return new DefaultDamage(roll.toScore());
    }

    public void setAttackBonus(Modifier attackBonus) {
        this.attackBonus = attackBonus;
    }

    public void setDamageDie(D damageDie) {
        this.damageDie = damageDie;
    }
}
