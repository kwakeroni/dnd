package be.kwakeroni.dnd.engine.model.fight;

import be.kwakeroni.dnd.io.model.MutableParticipant;
import be.kwakeroni.dnd.model.actor.ActionType;
import be.kwakeroni.dnd.model.creature.Creature;
import be.kwakeroni.dnd.model.creature.event.CreatureEventStream;
import be.kwakeroni.dnd.model.effect.Hit;
import be.kwakeroni.dnd.model.actor.Actor;
import be.kwakeroni.dnd.model.target.Hittable;
import be.kwakeroni.dnd.type.die.D20;
import be.kwakeroni.dnd.model.fight.Participant;
import be.kwakeroni.dnd.type.die.Roll;
import be.kwakeroni.dnd.type.value.Modifier;
import be.kwakeroni.dnd.type.value.Score;

import java.util.Collection;
import java.util.Optional;

/**
 * @author Maarten Van Puymbroeck
 */
public final class DefaultParticipant implements Participant, MutableParticipant, Actor, Hittable {

    private String name;
    private Actor actor;
    private Hittable target;
    private Creature creature;
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

    public DefaultParticipant(){

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
