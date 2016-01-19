package active.io.xml;

import static active.io.xml.ConverterBuilder.converter;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.function.Consumer;

import active.engine.internal.creature.DefaultCreature;
import active.model.creature.Party;
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
                        .withAttribute("version", data -> String.valueOf(data.version))
                        .withElements(data -> data.parties)
                            .as("party")
                            .done())
            .and(Party.class)
                .using(converter(Party.class)
                        .withAttribute("name", Party::getName)
                        .withStream(Party::members)
                            .as("member")
                            .done())
                            
            .and(DefaultCreature.class)
                .withAttributes("name")
            .and(Score.class)
                .asValue(Score::toString, Score::fromString)
            .and(Modifier.class)
                .asValue(Modifier::toString, Modifier::fromString)
            .build();
        
    }
    
    public static Consumer<OutputStream> export(Party party){
        return (OutputStream destination) -> INSTANCE.xstream.toXML(new DndData().add(party), destination);
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
