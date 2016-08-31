package active.engine.internal.action.type;

import active.engine.internal.action.category.FightAction;
import active.model.effect.Attack;

import java.util.Arrays;
import java.util.List;

/**
 * (C) 2016 Maarten Van Puymbroeck
 */
public class AttackActionType extends ActionTypeSupport {


    private List<Attack<?>> attacks;

    public AttackActionType(String name, Attack<?>... attacks) {
        this(name, Arrays.asList(attacks));
    }
    public AttackActionType(String name, List<Attack<?>> attacks) {
        super(name, FightAction.ATTACK);
        this.attacks = attacks;
    }

    public List<Attack<?>> getAttacks() {
        return attacks;
    }
}
