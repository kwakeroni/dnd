package active.engine.internal.action;

import active.engine.internal.effect.DefaultHit;
import active.model.cat.Actor;
import active.model.cat.Description;
import active.model.cat.Hittable;
import active.model.effect.Hit;
import active.model.value.Score;

/**
 * @author Maarten Van Puymbroeck
 */
public class HitAction extends SimpleAction<Object> {

    public HitAction(Actor actor, Hittable target) {
        super(actor, target);
    }

    @Override
    public void doExecute(Object o) {
        Hit hit = DefaultHit.of(5);
        this.target.hit(hit);
    }

    @Override
    public void describe(Description description) {
        description.append(this.actor).append(" hits ").append(this.target).append(" for ").append(Score.of(5)).append(" damage");
    }
}
