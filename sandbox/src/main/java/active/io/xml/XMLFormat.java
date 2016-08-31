package active.io.xml;

import static active.io.xml.ConverterBuilder.converter;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import active.engine.internal.action.type.AttackActionType;
import active.engine.internal.creature.DefaultCreature;
import active.model.action.ActionCategory;
import active.model.action.ActionType;
import active.model.creature.Creature;
import active.model.creature.Party;
import active.model.creature.stats.StatisticEntry;
import active.model.die.Die;
import active.model.effect.Attack;
import active.model.value.Modifier;
import active.model.value.Score;

import com.thoughtworks.xstream.XStream;

public class XMLFormat {

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
        this.xstream = new XStreamBuilder()
            .with(DndData.class)
                .as("dnd-data")
                .using(converter(DndData.class)
                        .withAttributes("version")
                        .withElements(data -> data.parties)
                            .as("party")
                            .done())
            .and(Party.class)
                .using(converter(Party.class)
                        .withAttributes("name")
                        .withStream(Party::members)
                            .as("member")
                            .done())

            .and(Creature.class)
                .using(converter(Creature.class)
                        .withAttributes("name")
                        .withStream("stats", Creature::statistics)
                            .as("stat")
                            .done()
                        .withStream("actions", Creature::actions)
                            .as("action")
                            .done()
                )
            .and(AttackActionType.class)
                .using(converter(AttackActionType.class)
                        .withAttributes("name")
                        .withAttribute("category", a -> a.getCategories().stream().map(ActionCategory::name).collect(Collectors.joining(",")))
                        .withElements(AttackActionType::getAttacks)
                            .as("attack")
                            .done()
                )
                .and(Attack.class)
                    .using(converter(Attack.class)
                            .withAttributes("name")
                            .withAttribute("bonus", a -> a.getAttackBonus().toString())
                            .withElement("damage", Attack::getDamageDie)

                    )
                        .and(StatisticEntry.class)
                .using(converter(StatisticEntry.class)
                        .withAttribute("name", se -> se.getStat().toString())
                        .withValue(se -> String.valueOf(se.getValue()))
                )
            .and(Score.class)
                .asValue(Score::toString, Score::fromString)
            .and(Modifier.class)
                .asValue(Modifier::toString, Modifier::fromString)
            .and(Die.class)
                .asValue(Die::toString, null)
            .build();

    }

    public static Consumer<OutputStream> export(Party party){
        return (OutputStream destination) -> INSTANCE.xstream.toXML(new DndData().add(party), destination);
    }

    public static Function<InputStream, Party> importParty(){
        return (InputStream source) -> ((DndData) INSTANCE.xstream.fromXML(source)).parties.get(0);
    }

    private static final class DndData {
        private int version;
        private java.util.List<Party> parties;

        public DndData(){
            this.version = LAST_VERSION;
        }

        public DndData add(Party party){
            if (parties == null){
                this.parties = new ArrayList<>();
            }
            this.parties.add(party);
            return this;
        }



    }
}
