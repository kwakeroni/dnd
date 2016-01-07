package active.engine.internal.fight;

import active.model.action.Actor;
import active.model.action.Hittable;
import active.model.die.D20;
import active.model.fight.Participant;
import active.model.die.Roll;
import active.model.value.Score;

import java.util.Optional;
import java.util.OptionalInt;

/**
 * @author Maarten Van Puymbroeck
 */
public class DefaultParticipant implements Participant {

    private final String name;
    private final Actor actor;
    private final Hittable target;
    private Score initiative;

    public static Participant ofUntouchable(String name, Actor actor){
        return new DefaultParticipant(name, actor, null);
    }

    public static Participant ofObject(String name, Hittable target){
        return new DefaultParticipant(name, null, target);
    }

    public static <P extends Object & Actor & Hittable> Participant ofCharacter(String name, P character){
        return new DefaultParticipant(name, character, character);
    }

    private DefaultParticipant(String name, Actor actor, Hittable target) {
        this.name = name;
        this.actor = actor;
        this.target = target;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean isActor() {
        return this.actor != null;
    }

    @Override
    public Actor asActor(){
        if (this.actor == null){
            throw new IllegalStateException(toString() + " is not an actor");
        }
        return this.actor;
    }

    @Override
    public boolean isTarget() {
        return this.target != null;
    }

    @Override
    public Hittable asTarget(){
        if (this.target == null){
            throw new IllegalStateException(toString() + " is not a target");
        }
        return this.target;
    }

    @Override
    public Optional<Score> getInitiative() {
        return Optional.ofNullable(this.initiative);
    }

    @Override
    public void setInitiative(Score initiative) {
        this.initiative = initiative;
    }

    @Override
    public void setInitiative(Roll<D20> roll) {
        setInitiative(
            roll.modify(
                asActor()
                    .getInitiativeModifier()));
    }

    @Override
    public String toString(){
        return "Participant[" + getName() + "]";
    }
}
