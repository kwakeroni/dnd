package active.engine.internal.effect;

import active.model.die.Die;
import active.model.die.Roll;
import active.model.effect.Attack;
import active.model.effect.Damage;
import active.model.value.Modifier;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class DefaultAttack<D extends Die> implements Attack<D> {

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
