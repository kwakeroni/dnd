package be.kwakeroni.dnd.io.test.model;

import be.kwakeroni.dnd.io.model.MutableFight;
import be.kwakeroni.dnd.model.actor.Actor;
import be.kwakeroni.dnd.model.fight.Participant;
import be.kwakeroni.dnd.model.fight.Round;
import be.kwakeroni.dnd.model.fight.event.FightEventStream;
import be.kwakeroni.dnd.model.target.Hittable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public class TestFight implements MutableFight {

    private int lastRoundNumber = -1;
    private List<Participant> actors = new ArrayList<>();
    private List<Participant> targets = new ArrayList<>();

    TestFight(TestModel model){
        // avoid instantiation by reflection
    }

    @Override
    public void setLastRoundNumber(int count) {
        this.lastRoundNumber = count;
    }

    @Override
    public void add(Participant participant) {
        if (! (participant.isActor() || participant.isTarget())){
            throw new IllegalArgumentException("Participant not Actor or Hittable");
        }
        if (participant.isActor()){
            actors.add(participant);
        }
        if (participant.isTarget()){
            targets.add(participant);
        }
    }

    @Override
    public <AP extends Participant & Actor> Stream<AP> getActors() {
        return (Stream<AP>) (Stream<?>) this.actors.stream();
    }

    @Override
    public <HP extends Participant & Hittable> Stream<HP> getTargets() {
        return (Stream<HP>) (Stream<?>) this.targets.stream();
    }

    @Override
    public Optional<? extends Round> getCurrentRound() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getLastRoundNumber() {
        return lastRoundNumber;
    }

    @Override
    public FightEventStream on() {
        throw new UnsupportedOperationException();
    }
}
