package be.kwakeroni.dnd.io.model;

import be.kwakeroni.dnd.model.effect.Attack;
import be.kwakeroni.dnd.model.fight.AttackActionType;

public interface MutableAttackActionType extends AttackActionType {

    public void addAttack(Attack<?> attack);

}
