package active.model.fight;

import active.model.cat.Actor;
import active.model.cat.Hittable;
import active.model.cat.Named;
import active.model.die.D20;
import active.model.die.Roll;
import active.model.value.Score;

import java.util.Optional;

/**
 * @author Maarten Van Puymbroeck
 */
public interface Participant extends Named {

    public boolean isActor();

    public Optional<Actor> asActor();

    public boolean isTarget();

    public Optional<Hittable> asTarget();

    public Optional<Score> getInitiative();

    public void setInitiative(Score initiative);

    /**
     *
     * @param roll
     * @pre isActor()
     */
    public void setInitiative(Roll<D20> roll);
}
