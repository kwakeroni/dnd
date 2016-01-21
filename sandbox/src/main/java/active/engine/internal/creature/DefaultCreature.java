package active.engine.internal.creature;

import active.engine.event.EventBrokerSupport;
import active.model.creature.event.CreatureEventStream;
import active.model.creature.event.StatChanged;
import active.model.creature.stats.Base;
import active.model.creature.stats.Mod;
import active.model.creature.stats.Statistic;
import active.model.effect.Hit;
import active.model.creature.Creature;
import active.model.value.Modifier;
import active.model.value.Score;

import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

public class DefaultCreature implements Creature {
    
    private String name;
    private final Map<Statistic<?>, Object> statistics = new HashMap<>();
    private EventBrokerSupport<CreatureEventStream> broker;

    
    public DefaultCreature(String name, Score maxHP, Modifier init){
        this.name = name;
        statistics.put(Base.MAX_HP, maxHP);
        statistics.put(Base.HP, maxHP);
        statistics.put(Mod.INIT, init);

        this.broker = EventBrokerSupport.newInstance().supplying((source) -> (CreatureEventStream) source::event);

    }

    public <S> S get(Statistic<S> stat){
        S s = (S) statistics.get(stat);
        if (s == null){
            throw new IllegalArgumentException("Statistic " + stat + " undefined for " + this);
        }
        return s;
    }

    private <S> void set(Statistic<S> stat, S value){
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
}
