package active.engine.internal.creature;

import static active.model.die.Dice.*;
import active.engine.event.EventBrokerSupport;
import active.engine.internal.action.type.AttackActionType;
import active.engine.internal.effect.DefaultAttack;
import active.model.action.ActionType;
import active.model.creature.event.CreatureEventStream;
import active.model.creature.event.StatChanged;
import active.model.creature.stats.*;
import active.model.die.Die;
import active.model.effect.Attack;
import active.model.effect.Hit;
import active.model.creature.Creature;
import active.model.value.Modifier;
import active.model.value.Score;

import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class DefaultCreature implements Creature {
    
    private String name;
    private final Map<Statistic<?>, Object> statistics = new HashMap<>();
    private EventBrokerSupport<CreatureEventStream> broker;
    private Collection<ActionType> actionTypes;

    public DefaultCreature(){
        this.name="unknown";
        this.broker = EventBrokerSupport.newInstance().supplying((source) -> (CreatureEventStream) source::event);
        this.actionTypes = new ArrayList<>();
    }

    public DefaultCreature(String name, Score maxHP, Score ac, Modifier init){
        this.name = name;
        statistics.put(Base.MAX_HP, maxHP);
        statistics.put(Base.HP, maxHP);
        statistics.put(Mod.INIT, init);
        statistics.put(Prop.AC, ac);

        this.broker = EventBrokerSupport.newInstance().supplying((source) -> (CreatureEventStream) source::event);

        this.actionTypes = Arrays.asList(
                new AttackActionType("Full Attack", attack("Bite", 9, _2(D6)), attack("Claw", 4, D6), attack("Claw", 4, D6)),
                new AttackActionType("Single Attack", attack("Bite", 9, _2(D6))),
                new AttackActionType("Ranged Attack", attack("Bow", 5, D8))
        );


    }

    private Attack<?> attack(String name, int modifier, Die die){
        return new DefaultAttack<>(name, Modifier.of(modifier), die);
    }

    public <S> S get(Statistic<S> stat){
        S s = (S) statistics.get(stat);
        if (s == null){
            throw new IllegalArgumentException("Statistic " + stat + " undefined for " + this);
        }
        return s;
    }

    public <S> void addStatistic(StatisticEntry<S> entry){
        statistics.put(entry.getStat(), entry.getValue());
    }

    public <S> void set(Statistic<S> stat, S value){
        S oldValue = get(stat);
        statistics.put(stat, value);

        this.broker.fire(new StatChanged<>(stat, oldValue, value));
    }

    private <S> void modify(Statistic<S> stat, UnaryOperator<S> op){
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
    public void hit(Hit hit) {
        hit.getDamage().forEach( dmg -> modify(Base.HP, hp -> hp.minus(dmg.getAmount())));
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

    public void addAction(ActionType action){
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

    private static <S> StatisticEntry<S> toStat(Map.Entry<Statistic<?>, Object> entry){
        Statistic<S> stat = (Statistic<S>) entry.getKey();
        S value = (S) entry.getValue();
        return new StatisticEntry<>(stat, value);
    }

    public String toString(){
        return "Creature["+this.name+"]{"+this.statistics+"}{"+this.actionTypes+"}";
    }
}
