package be.kwakeroni.dnd.engine.model;

import be.kwakeroni.dnd.engine.event.EventBrokerSupport;
import be.kwakeroni.dnd.engine.model.fight.DefaultAttackActionType;
import be.kwakeroni.dnd.io.model.MutableCreature;
import be.kwakeroni.dnd.model.actor.ActionType;
import be.kwakeroni.dnd.model.creature.Creature;
import be.kwakeroni.dnd.model.creature.event.CreatureEventStream;
import be.kwakeroni.dnd.model.creature.event.StatChanged;
import be.kwakeroni.dnd.model.creature.stats.Base;
import be.kwakeroni.dnd.model.creature.stats.Mod;
import be.kwakeroni.dnd.model.creature.stats.Prop;
import be.kwakeroni.dnd.model.creature.stats.Statistic;
import be.kwakeroni.dnd.model.creature.stats.StatisticEntry;
import be.kwakeroni.dnd.model.effect.Attack;
import be.kwakeroni.dnd.model.effect.Hit;
import be.kwakeroni.dnd.type.die.Die;
import be.kwakeroni.dnd.type.value.Modifier;
import be.kwakeroni.dnd.type.value.Score;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static be.kwakeroni.dnd.type.die.Dice.*;

public class DefaultCreature implements Creature, MutableCreature {

    private String name;
    private final Map<Statistic<?>, Object> statistics = new LinkedHashMap<>();
    private EventBrokerSupport<CreatureEventStream> broker;
    private Collection<ActionType> actionTypes;

    public DefaultCreature() {
        this.name = "unknown";
        this.broker = EventBrokerSupport.newInstance().supplying((source) -> (CreatureEventStream) source::event);
        this.actionTypes = new ArrayList<>();
    }

    public DefaultCreature(String name, Score maxHP, Score ac, Modifier init) {
        this.name = name;
        statistics.put(Base.MAX_HP, maxHP);
        statistics.put(Base.HP, maxHP);
        statistics.put(Mod.INIT, init);
        statistics.put(Prop.AC, ac);

        this.broker = EventBrokerSupport.newInstance().supplying((source) -> (CreatureEventStream) source::event);

        this.actionTypes = Arrays.asList(
                new DefaultAttackActionType("Full Attack", attack("Bite", 9, _2(D6)), attack("Claw", 4, D6), attack("Claw", 4, D6)),
                new DefaultAttackActionType("Single Attack", attack("Bite", 9, _2(D6))),
                new DefaultAttackActionType("Ranged Attack", attack("Bow", 5, D8))
        );


    }

    private Attack<?> attack(String name, int modifier, Die die) {
        return new DefaultAttack<>(name, Modifier.of(modifier), die);
    }

    public <S> S get(Statistic<S> stat) {
        S s = (S) statistics.get(stat);
        if (s == null) {
            throw new IllegalArgumentException("Statistic " + stat + " undefined for " + this);
        }
        return s;
    }

    public <S> void addStatistic(StatisticEntry<S> entry) {
        statistics.put(entry.getStat(), entry.getValue());
    }

    public <S> void set(Statistic<S> stat, S value) {
        S oldValue = get(stat);
        statistics.put(stat, value);

        this.broker.fire(new StatChanged<>(stat, oldValue, value));
    }

    private <S> void modify(Statistic<S> stat, UnaryOperator<S> op) {
        set(stat, op.apply(get(stat)));
    }

    @Override
    public Score getHP() {
        return get(Base.HP);
    }

    @Override
    public Score getAC() {
        return get(Prop.AC);
    }

    @Override
    public Modifier getInitiativeModifier() {
        return get(Mod.INIT);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void hit(Hit hit) {
        hit.getDamage().forEach(dmg -> modify(Base.HP, hp -> hp.minus(dmg.getAmount())));
    }

    @Override
    public CreatureEventStream on() {
        return this.broker.on();
    }

    @Override
    public Collection<ActionType> getActions() {
        return this.actionTypes;
    }

    @Override
    public Stream<ActionType> actions() {
        return getActions().stream();
    }

    public void addAction(ActionType action) {
        this.actionTypes.add(action);
    }

    //    public Map<Statistic<?>, Object> getStatistics(){
//        return Collections.unmodifiableMap(this.statistics);
//    }
//

    @Override
    public Stream<StatisticEntry<?>> statistics() {
        return this.statistics.entrySet().stream()
                .map(DefaultCreature::toStat);
    }

    private static <S> StatisticEntry<S> toStat(Map.Entry<Statistic<?>, Object> entry) {
        Statistic<S> stat = (Statistic<S>) entry.getKey();
        S value = (S) entry.getValue();
        return new StatisticEntry<>(stat, value);
    }

    public String toString() {
        return "Creature[" + this.name + "]{" + this.statistics + "}{" + this.actionTypes + "}";
    }
}
