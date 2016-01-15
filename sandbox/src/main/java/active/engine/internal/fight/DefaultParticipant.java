package active.engine.internal.fight;

import active.model.cat.Description;
import active.model.effect.Hit;
import active.model.cat.Actor;
import active.model.cat.Hittable;
import active.model.die.D20;
import active.model.fight.Participant;
import active.model.die.Roll;
import active.model.value.Modifier;
import active.model.value.Score;

import java.util.Optional;

/**
 * @author Maarten Van Puymbroeck
 */
public final class DefaultParticipant implements Participant, Actor, Hittable {

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

    public static <P extends Object & Actor & Hittable> Participant ofCreature(P character){
        return new DefaultParticipant(character.getName(), character, character);
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
    public <AP extends Participant & Actor> Optional<AP> asActor(){
        return (this.actor != null)? Optional.of((AP) this) : Optional.empty();
    }

    @Override
    public boolean isTarget() {
        return this.target != null;
    }

    @Override
    public <HP extends Participant & Hittable> Optional<HP> asTarget(){
        return (this.target != null)? Optional.of((HP) this) : Optional.empty();
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
                asActor().map(a -> a.getInitiativeModifier()).orElse(Modifier.of(0))));
    }

    @Override
    public String toString(){
        return "Participant[" + getName() + "]";
    }

    @Override
    public Modifier getInitiativeModifier() {
        return actor.getInitiativeModifier();
    }

    @Override
    public Score getHP() {
        return target.getHP();
    }

    @Override
    public void hit(Hit hit) {
        target.hit(hit);
    }


}
