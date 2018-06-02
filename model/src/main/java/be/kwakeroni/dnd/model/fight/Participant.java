package be.kwakeroni.dnd.model.fight;

import be.kwakeroni.dnd.model.actor.Actor;
import be.kwakeroni.dnd.model.creature.Creature;
import be.kwakeroni.dnd.model.target.Hittable;
import be.kwakeroni.dnd.type.base.Named;
import be.kwakeroni.dnd.model.creature.event.CreatureEventStream;
import be.kwakeroni.dnd.type.die.D20;
import be.kwakeroni.dnd.type.die.Roll;
import be.kwakeroni.dnd.type.value.Score;

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

    @Deprecated
    public void setInitiative(Score initiative);

    /**
     *
     * @param roll
     * @pre isActor()
     */
    @Deprecated
    public void setInitiative(Roll<D20> roll);

    public CreatureEventStream on();

    public Creature getAsCreature();
}
