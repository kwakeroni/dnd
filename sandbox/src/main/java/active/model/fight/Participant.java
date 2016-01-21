package active.model.fight;

import active.model.cat.Actor;
import active.model.cat.Hittable;
import active.model.cat.Named;
import active.model.creature.event.CreatureEventStream;
import active.model.die.D20;
import active.model.die.Roll;
import active.model.value.Score;

import java.util.Optional;

/**
 * @author Maarten Van Puymbroeck
 */
public interface Participant extends Named {

    public boolean isActor();

    public <AP extends Participant & Actor> Optional<AP> asActor();

    public boolean isTarget();

    public <HP extends Participant & Hittable> Optional<HP> asTarget();

    public Optional<Score> getInitiative();

    public void setInitiative(Score initiative);

    /**
     *
     * @param roll
     * @pre isActor()
     */
    public void setInitiative(Roll<D20> roll);

    public CreatureEventStream on();
}
