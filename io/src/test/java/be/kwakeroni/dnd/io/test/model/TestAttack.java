package be.kwakeroni.dnd.io.test.model;

import be.kwakeroni.dnd.io.model.MutableAttack;
import be.kwakeroni.dnd.model.effect.Damage;
import be.kwakeroni.dnd.type.die.Die;
import be.kwakeroni.dnd.type.die.Roll;
import be.kwakeroni.dnd.type.value.Modifier;

public class TestAttack<D extends Die> implements MutableAttack<D> {

    private String name;
    private Modifier attackBonus;
    private D damageDie;

    TestAttack(TestModel model){
        // avoid instantiation by reflection
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setAttackBonus(Modifier attackBonus) {
        this.attackBonus = attackBonus;
    }

    @Override
    public void setDamageDie(D damageDie) {
        this.damageDie = damageDie;
    }

    @Override
    public Modifier getAttackBonus() {
        return attackBonus;
    }

    @Override
    public D getDamageDie() {
        return this.damageDie;
    }

    @Override
    public Damage getDamage(Roll<D> roll) {
        throw new IllegalArgumentException();
    }
}
