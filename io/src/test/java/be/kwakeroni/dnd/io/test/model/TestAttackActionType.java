package be.kwakeroni.dnd.io.test.model;

import be.kwakeroni.dnd.io.model.MutableAttackActionType;
import be.kwakeroni.dnd.model.actor.ActionCategory;
import be.kwakeroni.dnd.model.effect.Attack;
import be.kwakeroni.dnd.model.fight.AttackActionType;

import java.util.ArrayList;
import java.util.List;

public class TestAttackActionType implements AttackActionType, MutableAttackActionType {

    private ActionCategory category;
    private List<Attack<?>> attacks = new ArrayList<>();

    @Override
    public void addAttack(Attack<?> attack) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Attack<?>> getAttacks() {
        return null;
    }

    @Override
    public ActionCategory getCategory() {
        return this.category;
    }
}
