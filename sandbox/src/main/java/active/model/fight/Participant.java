package active.model.fight;

import active.model.action.Actor;
import active.model.action.Hittable;
import active.model.die.D20;
import active.model.die.Roll;
import active.model.value.Score;

import java.util.Optional;

/**
 * @author Maarten Van Puymbroeck
 */
public interface Participant {
    public String getName();

    public boolean isActor();

    public Actor asActor();

    public boolean isTarget();

    public Hittable asTarget();

    public Optional<Score> getInitiative();

    public void setInitiative(Score initiative);

    /**
     *
     * @param roll
     * @pre isActor()
     */
    public void setInitiative(Roll<D20> roll);
}
