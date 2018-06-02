package be.kwakeroni.dnd.engine.action;

import be.kwakeroni.dnd.model.actor.Action;
import be.kwakeroni.dnd.model.actor.Actor;
import be.kwakeroni.dnd.model.target.Hittable;

/**
 * @author Maarten Van Puymbroeck
 */
public abstract class SimpleAction<Context> implements Action<Context> {

    protected final Actor actor;
    protected final Hittable target;
    private boolean isExecuted = false;

    public SimpleAction(Actor actor, Hittable target) {
        this.actor = actor;
        this.target = target;
    }

    public Actor getActor() {
        return actor.unmodifiableActor();
    }

    public Hittable getTarget() {
        return target.unmodifiableHittable();
    }

    @Override
    public final void execute(Context context) {
        if (this.isExecuted){
            throw new IllegalStateException("Action " + this + " was already executed");
        }
        doExecute(context);
        this.isExecuted = true;
    }

    protected abstract void doExecute(Context context);

}
