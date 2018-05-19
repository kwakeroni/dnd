package active.engine.internal.action;

import active.engine.internal.effect.DefaultHit;
import active.model.cat.Actor;
import active.model.cat.Description;
import active.model.cat.Hittable;
import active.model.effect.Damage;
import active.model.effect.Hit;

/**
 * @author Maarten Van Puymbroeck
 */
public class HitAction extends SimpleAction<Object> {

    private final Damage damage;

    public HitAction(Actor actor, Hittable target, Damage dmg) {
        super(actor, target);
        this.damage = dmg;
    }

    @Override
    public void doExecute(Object o) {
        Hit hit = DefaultHit.of(damage);
        this.target.hit(hit);
    }

    @Override
    public void describe(Description description) {
        description.append(this.actor).append(" hits ").append(this.target).append(" for ").append(damage.getAmount()).append(" damage");
    }
}
