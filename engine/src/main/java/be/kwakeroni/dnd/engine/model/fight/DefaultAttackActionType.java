package be.kwakeroni.dnd.engine.model.fight;

import be.kwakeroni.dnd.engine.model.ActionTypeSupport;
import be.kwakeroni.dnd.io.model.MutableAttackActionType;
import be.kwakeroni.dnd.model.effect.Attack;
import be.kwakeroni.dnd.model.fight.AttackActionType;
import be.kwakeroni.dnd.model.fight.FightAction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class DefaultAttackActionType extends ActionTypeSupport implements AttackActionType, MutableAttackActionType {


    private List<Attack<?>> attacks;

    public DefaultAttackActionType() {
        super(FightAction.ATTACK);
        this.attacks = new ArrayList<>();
    }

    public DefaultAttackActionType(String name, Attack<?>... attacks) {
        this(name, Arrays.asList(attacks));
    }

    public DefaultAttackActionType(String name, List<Attack<?>> attacks) {
        super(name, FightAction.ATTACK);
        this.attacks = attacks;
    }

    public List<Attack<?>> getAttacks() {
        return attacks;
    }

    public void addAttack(Attack<?> attack) {
        this.attacks.add(attack);
    }
}
