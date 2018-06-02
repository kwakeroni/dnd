package be.kwakeroni.dnd.io.test.model;

import be.kwakeroni.dnd.io.model.MutableCreature;
import be.kwakeroni.dnd.model.actor.ActionType;
import be.kwakeroni.dnd.model.creature.event.CreatureEventStream;
import be.kwakeroni.dnd.model.creature.stats.Statistic;
import be.kwakeroni.dnd.model.creature.stats.StatisticEntry;
import be.kwakeroni.dnd.model.effect.Hit;
import be.kwakeroni.dnd.type.value.Modifier;
import be.kwakeroni.dnd.type.value.Score;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class TestCreature implements MutableCreature {

    private String name;
    private Map<Statistic<?>, StatisticEntry<?>> statistics = new LinkedHashMap<>();
    private List<ActionType> actions = new ArrayList<>();

    TestCreature(TestModel model){
        // avoid instantiation by reflection
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public <S> void addStatistic(StatisticEntry<S> entry) {
        this.statistics.put(entry.getStat(), entry);
    }

    @Override
    public void addAction(ActionType action) {
        this.actions.add(action);
    }

    @Override
    public <S> S get(Statistic<S> stat) {
        return (S) statistics.get(stat).getValue();
    }

    @Override
    public Stream<StatisticEntry<?>> statistics() {
        return statistics.values().stream();
    }

    @Override
    public Stream<ActionType> actions() {
        return actions.stream();
    }

    @Override
    public CreatureEventStream on() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Modifier getInitiativeModifier() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<ActionType> getActions() {
        return actions;
    }

    @Override
    public Score getAC() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Score getHP() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void hit(Hit hit) {
        throw new UnsupportedOperationException();
    }
}
