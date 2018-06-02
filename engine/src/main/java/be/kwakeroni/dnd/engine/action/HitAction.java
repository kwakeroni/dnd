package be.kwakeroni.dnd.engine.action;

import be.kwakeroni.dnd.engine.model.DefaultHit;
import be.kwakeroni.dnd.model.actor.Actor;
import be.kwakeroni.dnd.type.base.Description;
import be.kwakeroni.dnd.model.target.Hittable;
import be.kwakeroni.dnd.model.effect.Damage;
import be.kwakeroni.dnd.model.effect.Hit;

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
