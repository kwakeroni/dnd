package active.engine.internal.fight;

import active.model.cat.Actor;
import active.model.cat.Hittable;
import active.model.die.D20;
import active.model.fight.IsActor;
import active.model.fight.IsTarget;
import active.model.fight.Participant;
import active.model.die.Roll;
import active.model.value.Modifier;
import active.model.value.Score;

import java.util.Optional;
import java.util.OptionalInt;

/**
 * @author Maarten Van Puymbroeck
 */
public final class DefaultParticipant implements Participant, IsActor, IsTarget {

    private final String name;
    private final Optional<Actor> actor;
    private final Optional<Hittable> target;
    private Score initiative;

    public static Participant ofUntouchable(String name, Actor actor){
        return new DefaultParticipant(name, actor, null);
    }

    public static Participant ofObject(String name, Hittable target){
        return new DefaultParticipant(name, null, target);
    }

    public static <P extends Object & Actor & Hittable> Participant ofCharacter(P character){
        return new DefaultParticipant(character.getName(), character, character);
    }

    private DefaultParticipant(String name, Actor actor, Hittable target) {
        this.name = name;
        this.actor = Optional.ofNullable(actor);
        this.target = Optional.ofNullable(target);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean isActor() {
        return this.actor.isPresent();
    }

    @Override
    public Actor actor() {
        return asActor().get();
    }

    @Override
    public Optional<Actor> asActor(){
        return this.actor;
    }

    @Override
    public boolean isTarget() {
        return this.target.isPresent();
    }

    @Override
    public Hittable target() {
        return asTarget().get();
    }

    @Override
    public Optional<Hittable> asTarget(){
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
                asActor().map(Actor::getInitiativeModifier).orElse(Modifier.of(0))));
    }

    @Override
    public String toString(){
        return "Participant[" + getName() + "]";
    }
}
