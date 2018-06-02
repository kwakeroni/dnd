package be.kwakeroni.dnd.io.xml;

import be.kwakeroni.dnd.io.model.ModelImplementation;
import be.kwakeroni.dnd.io.model.MutableAttack;
import be.kwakeroni.dnd.io.model.MutableAttackActionType;
import be.kwakeroni.dnd.io.model.MutableCreature;
import be.kwakeroni.dnd.io.model.MutableFight;
import be.kwakeroni.dnd.io.model.MutableParticipant;
import be.kwakeroni.dnd.io.model.MutableParty;
import be.kwakeroni.dnd.model.actor.ActionType;
import be.kwakeroni.dnd.model.creature.Creature;
import be.kwakeroni.dnd.model.creature.Party;
import be.kwakeroni.dnd.model.creature.stats.Statistic;
import be.kwakeroni.dnd.model.creature.stats.StatisticEntry;
import be.kwakeroni.dnd.model.creature.stats.Statistics;
import be.kwakeroni.dnd.model.effect.Attack;
import be.kwakeroni.dnd.model.fight.AttackActionType;
import be.kwakeroni.dnd.model.fight.Fight;
import be.kwakeroni.dnd.model.fight.FightAction;
import be.kwakeroni.dnd.model.fight.Participant;
import be.kwakeroni.dnd.type.die.Dice;
import be.kwakeroni.dnd.type.die.Die;
import be.kwakeroni.dnd.type.value.Modifier;
import be.kwakeroni.dnd.type.value.Score;
import com.thoughtworks.xstream.XStream;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ServiceLoader;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static be.kwakeroni.dnd.io.xml.ConverterBuilder.converter;

public class XMLFormat {
    private static final Class<StatisticEntry<?>> StatisticEntry_class = (Class<StatisticEntry<?>>) (Class<?>) StatisticEntry.class;
    private static final Supplier<StatisticEntry<?>> StatisticEntry_new = (Supplier<StatisticEntry<?>>) StatisticEntry::new;

    private static final int LAST_VERSION = 1;
    private static final XMLFormat INSTANCE = ServiceLoader.load(ModelImplementation.class)
            .findFirst()
            .map(XMLFormat::new)
            .orElseThrow(() -> new IllegalStateException("No model implementation supplied"));

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
    public XMLFormat(ModelImplementation impl) {

        Function<Creature, Stream<ActionType>> actions = Creature::actions;

        this.xstream = new XStreamBuilder()
                .with(DndData.class)
                .as("dnd-data")
                .using(converter(DndData.class, DndData::new)
                        .withAttribute("version", Integer::parseInt)
                        .withElements(Party.class, impl::newParty, data -> data.parties, DndData::add)
                        .as("party")
                        .done()
                        .withElement("fight", Fight.class, data -> data.fight, (data, fight) -> data.fight = fight)
                )
                .and(Fight.class)
                .<MutableFight>implementedBy(impl.fightImplementationClass())
                .using(converter(Fight.class, impl::newFight)
                        .withAttribute("round", mapToString(Fight::getLastRoundNumber), setFrom(Integer::parseInt, MutableFight::setLastRoundNumber))
                        .withStream(MutableParticipant.class, impl::newParticipant, Fight::getParticipants, MutableFight::add)
                        .as("participant")
                        .done()
                )
                .and(Party.class)
                .<MutableParty>implementedBy(impl.partyImplementationClass())
                .using(converter(Party.class, impl::newParty)
                        .withAttributes("name")
                        .withStream(MutableCreature.class, impl::newCreature, Party::members, MutableParty::add)
                        .as("member")
                        .done())
                .and(Participant.class)
                .<MutableParticipant>implementedBy(impl.participantImplementationClass())
                .using(converter(Participant.class, impl::newParticipant)
                        .withAttribute("init", mapToString(p -> p.getInitiative().orElse(null)), setFrom(Score::fromString, MutableParticipant::setInitiative))
                        .withElement("creature", Creature.class, Participant::getAsCreature, MutableParticipant::setAsCreature)
                )
                .and(Creature.class)
                .<MutableCreature>implementedBy(impl.creatureImplementationClass())
                .using(converter(Creature.class, impl::newCreature)
                        .withAttributes("name")
                        .withStream("stats", StatisticEntry_class, StatisticEntry::new, Creature::statistics, MutableCreature::addStatistic)
                        .as("stat")
                        .done()
                        .withStream("actions", ActionType.class, () -> {
                            throw new UnsupportedOperationException("Cannot create generic ActionType");
                        }, Creature::actions, MutableCreature::addAction)
                        .as("action")
                        .done()
                )
                .and(ActionType.class)
                .using(converter(ActionType.class, (Supplier<ActionType>) () -> {
                            throw new UnsupportedOperationException("Cannot create generic ActionType");
                        })
                                .withSubtypeAttribute("category")
                                .withSubType(FightAction.ATTACK.name(), MutableAttackActionType.class, impl::newAttackActionType)
                                .withAttributes("name")
                                .withElements(MutableAttack.class, impl::newAttack, AttackActionType::getAttacks, MutableAttackActionType::addAttack)
                                .as("attack")
                                .done()
                                .done()
                                .done()
                )
                .and(Attack.class)
                .<MutableAttack>implementedBy(impl.attackImplementationClass())
                .using(converter(Attack.class, impl::newAttack)
                        .withAttributes("name")
                        .withAttribute("bonus", a -> a.getAttackBonus().toString(), (a, b) -> a.setAttackBonus(Modifier.fromString(b)))
                        .withElement("damage", Die.class, Attack::getDamageDie, MutableAttack::setDamageDie)

                )
                .and(StatisticEntry.class)
                .using(converter(StatisticEntry.class, (Supplier<StatisticEntry>) StatisticEntry::new)
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

    public static Consumer<OutputStream> export(Party party) {
        return exportData(new DndData().add(party));
    }

    public static Function<InputStream, Collection<Party>> importParties() {
        return importData().andThen(data -> data.parties);
    }

    public static Function<InputStream, MutableFight> importFight() {
        // TODO This cast is not good
        return importData().andThen(data -> (MutableFight) data.fight);
    }

    static Consumer<OutputStream> exportData(DndData data) {
        return (OutputStream destination) -> INSTANCE.xstream.toXML(data, destination);
    }

    static Function<InputStream, DndData> importData() {
        return (InputStream source) -> ((DndData) INSTANCE.xstream.fromXML(source));
    }

    static <T, S> Function<T, S> map(Function<T, S> func) {
        return func;
    }

    static <T> Function<T, String> mapToString(Function<T, ?> func) {
        return mapTo(func, Object::toString);
    }

    static <T, S1, S2> Function<T, S2> mapTo(Function<T, ? extends S1> func, Function<? super S1, ? extends S2> to) {
        return func.andThen(to);
    }

    static <T, S1, S2> BiConsumer<T, S1> setFrom(Function<? super S1, ? extends S2> fromString, BiConsumer<? super T, ? super S2> reaffecter) {
        return (t, string) -> reaffecter.accept(t, fromString.apply(string));
    }

    static final class DndData {
        private int version;
        private java.util.List<Party> parties = new ArrayList<>();
        private Fight fight;

        public DndData() {
            this.version = LAST_VERSION;
        }

        public DndData(Party party) {
            this();
            add(party);
        }

        public DndData(Fight fight) {
            this();
            this.fight = fight;
        }

        public DndData add(Party party) {
            this.parties.add(party);
            return this;
        }

        public int getVersion() {
            return this.version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

    }
}
