package active.engine.internal.fight;

import active.engine.event.EventBrokerSupport;
import active.model.action.ActionType;
import active.model.cat.Description;
import active.model.cat.Observable;
import active.model.creature.Creature;
import active.model.creature.event.CreatureEventStream;
import active.model.effect.Hit;
import active.model.cat.Actor;
import active.model.cat.Hittable;
import active.model.die.D20;
import active.model.event.Event;
import active.model.fight.Participant;
import active.model.die.Roll;
import active.model.fight.event.FightAware;
import active.model.fight.event.FightEventStream;
import active.model.value.Modifier;
import active.model.value.Score;

import java.awt.Desktop;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author Maarten Van Puymbroeck
 */
public final class DefaultParticipant implements Participant, Actor, Hittable {

    private String name;
    private Actor actor;
    private Hittable target;
    private Observable<CreatureEventStream> creature;
    private Score initiative;
    private Collection<ActionType> actionTypes;

//    public static Participant ofUntouchable(String name, Actor actor){
//        return new DefaultParticipant(name, actor, null, null);
//    }
//
//    public static Participant ofObject(String name, Hittable target){
//        return new DefaultParticipant(name, null, target, null);
//    }

    public static Participant ofCreature(Creature character){
        DefaultParticipant participant = new DefaultParticipant();
        participant.setAsCreature(character);

        Roll<D20> roll = Roll.D20();
        participant.setInitiative(roll);
        System.out.println("Rolling initiative for " + participant.getName() + ": " + roll + " " + character.getInitiativeModifier() + " = " + participant.getInitiative());
        return participant;
    }

    public Creature getAsCreature(){
        if (this.target == this.creature
                && this.actor == this.creature
                && this.creature instanceof Creature){
            return (Creature) this.creature;
        }
        return null;
    }

    public void setAsCreature(Creature creature){
        this.name = creature.getName();
        this.target = creature;
        this.actor = creature;
        this.creature = creature;
        this.actionTypes = creature.getActions();
    }

    private DefaultParticipant(){

    }

    private DefaultParticipant(String name, Actor actor, Hittable target, Observable<CreatureEventStream> creature) {
        this.name = name;
        this.actor = actor;
        this.target = target;
        this.creature = creature;
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
    public Score getAC() {
        return target.getAC();
    }

    @Override
    public void hit(Hit hit) {
        target.hit(hit);
    }

    @Override
    public CreatureEventStream on() {
        return this.creature.on();
    }

    public void setActionTypes(Collection<ActionType> types){
        this.actionTypes = types;
    }

    @Override
    public Collection<ActionType> getActions() {
        return this.actionTypes;
    }


}
