package active.io.xml;

import static active.io.xml.ConverterBuilder.converter;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import active.engine.internal.action.category.FightAction;
import active.engine.internal.action.type.AttackActionType;
import active.engine.internal.creature.DefaultCreature;
import active.engine.internal.creature.DefaultParty;
import active.engine.internal.effect.DefaultAttack;
import active.engine.internal.fight.DefaultFight;
import active.engine.internal.fight.DefaultParticipant;
import active.model.action.Action;
import active.model.action.ActionCategory;
import active.model.action.ActionType;
import active.model.creature.Creature;
import active.model.creature.Party;
import active.model.creature.stats.*;
import active.model.die.D20;
import active.model.die.Dice;
import active.model.die.Die;
import active.model.effect.Attack;
import active.model.fight.Fight;
import active.model.fight.Participant;
import active.model.value.Modifier;
import active.model.value.Score;

import com.thoughtworks.xstream.XStream;

public class XMLFormat {
    private static final Class<StatisticEntry<?>> StatisticEntry_class = (Class<StatisticEntry<?>>) (Class<?>) StatisticEntry.class;

    private static final int LAST_VERSION = 1;
    private static XMLFormat INSTANCE = new XMLFormat();
    private final XStream xstream;

//    T party = ;
//    
//    ToAttribute<Party> name = new ToAttribute<>(Party::getName, "name");
//    
//    InElement<Creature> member = new InElement<>("member");
//    ForEach<Party, Creature> members = new ForEach<>(p -> p.members().iterator(), member);
//    
//    name.marshal(party, writer, context);
//    members.marshal(party, writer, context);
    public XMLFormat(){

        Function<Creature, Stream<ActionType>> actions = Creature::actions;

        this.xstream = new XStreamBuilder()
            .with(DndData.class)
                .as("dnd-data")
                .using(converter(DndData.class)
                        .withAttribute("version", Integer::parseInt)
                        .withElements(Party.class, data -> data.parties, DndData::add)
                            .as("party")
                            .done()
                        .withElement("fight", Fight.class, data -> data.fight, (data, fight) -> data.fight = fight)
                )
            .and(Fight.class)
                .implementedBy(DefaultFight.class)
                .using(converter(Fight.class, DefaultFight.class)
                        .withAttribute("round", mapToString(Fight::getLastRoundNumber), setFrom(Integer::parseInt, DefaultFight::setLastRoundNumber))
                        .withStream(DefaultParticipant.class, Fight::getParticipants, DefaultFight::add)
                            .as("participant")
                            .done()
                )
            .and(Party.class)
                .implementedBy(DefaultParty.class)
                .using(converter(Party.class, DefaultParty.class)
                        .withAttributes("name")
                        .withStream(Creature.class, Party::members, DefaultParty::add)
                            .as("member")
                            .done())
            .and(Participant.class)
                .implementedBy(DefaultParticipant.class)
                .using(converter(Participant.class, DefaultParticipant.class)
                    .withAttribute("init", mapToString(p -> p.getInitiative().orElse(null)), setFrom(Score::fromString, DefaultParticipant::setInitiative))
                    .withElement("creature", Creature.class, p -> ((DefaultParticipant) p).getAsCreature(), DefaultParticipant::setAsCreature)
                )

            .and(Creature.class)
                .implementedBy(DefaultCreature.class)
                .using(converter(Creature.class, DefaultCreature.class)
                        .withAttributes("name")
                        .withStream("stats", StatisticEntry_class, Creature::statistics, DefaultCreature::addStatistic)
                            .as("stat")
                            .done()
                        .withStream("actions", ActionType.class, Creature::actions, DefaultCreature::addAction)
                            .as("action")
                            .done()
                )
            .and(ActionType.class)
                .using(converter(ActionType.class)
                        .withSubtypeAttribute("category")
                            .withSubType(FightAction.ATTACK.name(), AttackActionType.class)
                                        .withAttributes("name")
                                        .withElements(Attack.class, AttackActionType::getAttacks, AttackActionType::addAttack)
                                        .as("attack")
                                    .done()
                        .done()
                .done()
                )
                .and(Attack.class)
                    .implementedBy(DefaultAttack.class)
                    .using(converter(Attack.class, DefaultAttack.class)
                            .withAttributes("name")
                            .withAttribute("bonus", a -> a.getAttackBonus().toString(), (a, b) -> a.setAttackBonus(Modifier.fromString(b)))
                            .withElement("damage", Die.class, Attack::getDamageDie, DefaultAttack::setDamageDie)

                    )
                .and(StatisticEntry.class)
                .using(converter(StatisticEntry.class)
                        .withAttribute("name",
                                se -> Statistics.toString(se.getStat()),
                                (se, st) -> se.setStat(Statistics.fromString(st))
                        )
                        .withValue(se -> String.valueOf(se.getValue()), StatisticEntry::setStringValue)
                )
            .and(Score.class)
                .asValue(Score::toString, Score::fromString)
            .and(Modifier.class)
                .asValue(Modifier::toString, Modifier::fromString)
            .and(Die.class)
                .asValue(Die::toString, Dice::fromString)
            .and(Statistic.class)
                .asValue(Statistics::toString, Statistics::fromString)
            .build();

    }

    public static Consumer<OutputStream> export(Party party){
        return exportData(new DndData().add(party));
    }

    public static Function<InputStream, Collection<Party>> importParties(){
        return importData().andThen(data -> data.parties);
    }

    static Consumer<OutputStream> exportData(DndData data){
        return (OutputStream destination) -> INSTANCE.xstream.toXML(data, destination);
    }

    static Function<InputStream, DndData> importData(){
        return (InputStream source) -> ((DndData) INSTANCE.xstream.fromXML(source));
    }

    static <T, S> Function<T, S> map(Function<T, S> func){
        return func;
    }

    static <T> Function<T, String> mapToString(Function<T, ?> func){
        return mapTo(func, Object::toString);
    }

    static <T, S1, S2> Function<T, S2> mapTo(Function<T, ? extends S1> func, Function<? super S1, ? extends S2> to){
        return func.andThen(to);
    }

    static <T, S1, S2> BiConsumer<T, S1> setFrom(Function<? super S1, ? extends S2> fromString, BiConsumer<? super T, ? super S2> reaffecter){
        return (t, string) -> reaffecter.accept(t, fromString.apply(string));
    }

    static final class DndData {
        private int version;
        private java.util.List<Party> parties = new ArrayList<>();
        private Fight fight;

        public DndData(){
            this.version = LAST_VERSION;
        }

        public DndData(Party party){
            this();
            add(party);
        }

        public DndData(Fight fight){
            this();
            this.fight = fight;
        }

        public DndData add(Party party){
            this.parties.add(party);
            return this;
        }

        public int getVersion(){return this.version;}
        public void setVersion(int version){ this.version = version; }

    }
}
