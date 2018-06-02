package be.kwakeroni.dnd.io.test.model;

import be.kwakeroni.dnd.io.model.MutableParticipant;
import be.kwakeroni.dnd.model.actor.ActionType;
import be.kwakeroni.dnd.model.actor.Actor;
import be.kwakeroni.dnd.model.creature.Creature;
import be.kwakeroni.dnd.model.creature.event.CreatureEventStream;
import be.kwakeroni.dnd.model.effect.Hit;
import be.kwakeroni.dnd.model.fight.Participant;
import be.kwakeroni.dnd.model.target.Hittable;
import be.kwakeroni.dnd.type.die.D20;
import be.kwakeroni.dnd.type.die.Roll;
import be.kwakeroni.dnd.type.value.Modifier;
import be.kwakeroni.dnd.type.value.Score;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class TestParticipant implements MutableParticipant, Actor, Hittable {

    private Score initiative;
    private Creature creature;

    TestParticipant(TestModel model){
        // avoid instantiation by reflection
    }

    @Override
    public void setInitiative(Score initiative) {
        this.initiative = initiative;
    }

    @Override
    public void setAsCreature(Creature creature) {
        this.creature = creature;
    }

    @Override
    public boolean isActor() {
        return creature != null;
    }

    @Override
    public <AP extends Participant & Actor> Optional<AP> asActor() {
        return (Optional<AP>) Optional.of(this).filter(TestParticipant::isActor);
    }

    @Override
    public boolean isTarget() {
        return creature != null;
    }

    @Override
    public <HP extends Participant & Hittable> Optional<HP> asTarget() {
        return (Optional<HP>) Optional.of(this).filter(TestParticipant::isTarget);
    }

    @Override
    public Optional<Score> getInitiative() {
        return Optional.ofNullable(this.initiative);
    }

    @Override
    public void setInitiative(Roll<D20> roll) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CreatureEventStream on() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Creature getAsCreature() {
        return creature;
    }

    @Override
    public Modifier getInitiativeModifier() {
        return creature.getInitiativeModifier();
    }

    @Override
    public Collection<ActionType> getActions() {
        return creature.actions().collect(Collectors.toList());
    }

    @Override
    public Score getAC() {
        return creature.getAC();
    }

    @Override
    public Score getHP() {
        return creature.getHP();
    }

    @Override
    public void hit(Hit hit) {
        throw new UnsupportedOperationException();
    }
}
